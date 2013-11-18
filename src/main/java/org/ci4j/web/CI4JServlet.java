package org.ci4j.web;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.ci4j.core.Context;
import org.ci4j.util.LogUtils;
import org.ci4j.web.annotation.Path;

@SuppressWarnings("serial")
public class CI4JServlet extends HttpServlet {
	private static final Logger logger = LogUtils.getLogger("CI4JServlet");

	/**
	 * controller的package名
	 */
	private static final String CONTROLLER_PACKAGE = "controllers";

	/**
	 * 根目录的处理controller key
	 */
	private static final String INDEX_CONTROLLER = "index";

	/**
	 * controller 默认方法
	 */
	private static final String INDEX_ACTION = "index";

	/**
	 * 静态文件目录
	 */
	private static final String ASSETS_DIR = "/assets";

	private static final String COMMON_DEFAULT_SERVLET_NAME = "default";

	/**
	 * url分割符号
	 */
	private static final String URL_SEQ = "/";

	/**
	 * controller集合
	 */
	private static final Set<Class<?>> controllers = new HashSet<Class<?>>();

	/**
	 * action map
	 */
	private static final Map<String, Action> actionMap = new HashMap<String, Action>();

	@Override
	public void init() throws ServletException {
		LogUtils.info(logger, "init...");

		// 扫描controllers下面所有的类
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader()
					.getResources(CONTROLLER_PACKAGE);

			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddControllersInPackageByFile(CONTROLLER_PACKAGE,
							filePath, false, controllers);
				}
			}

			LogUtils.info(logger, "controllers size is: " + controllers.size());
		} catch (IOException e) {
			LogUtils.severe(logger, e.getMessage(), e);
		}
		try {
			for (Class<?> controller : controllers) {

				Object controllerObj = controller.newInstance();

				Method[] methods = controller.getDeclaredMethods();
				for (Method method : methods) {
					// 只有public void 非static final的方法才可以是action，其他的忽略
					int mod = method.getModifiers();
					if (!Modifier.isPublic(mod)
							|| !method.getReturnType().equals(Void.TYPE))
						continue;

					Action action = new Action(controllerObj, method,
							getParameterTypes(method));

					Path path = method.getAnnotation(Path.class);
					if (null != path) {
						// 设置path注解
						actionMap.put(path.value(), action);
					} else {
						StringBuilder sb = new StringBuilder(URL_SEQ);
						sb.append(lowCaseFirstChar(controller.getSimpleName()));

						String methodName = method.getName();
						if (INDEX_ACTION.equals(methodName)) { // index方法为controller的默认方法
							actionMap.put(sb.toString(), action);
						}

						actionMap.put(sb.append(URL_SEQ).append(methodName)
								.toString(), action);
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			LogUtils.severe(logger, e.getMessage(), e);
		}

		LogUtils.info(logger, actionMap.keySet().toString());
	}

	private static Class<?>[] getParameterTypes(Method method) {
		Class<?>[] types = method.getParameterTypes();
		if (types != null && types.length > 0) {
			for (Class<?> t : types)
				LogUtils.info(logger, "method %s parameter %s",
						method.getName(), t.getName());
			return types;
		} else {
			return null;
		}
	}

	private static String lowCaseFirstChar(String str) {
		byte[] items = str.getBytes();
		items[0] = (byte) ((char) items[0] - 'A' + 'a');
		return new String(items);
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddControllersInPackageByFile(String packageName,
			String packagePath, final boolean recursive,
			Set<Class<?>> controllers) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			LogUtils.warning(logger, "controller not exists");
			return;
		}
		File[] dirfiles = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				findAndAddControllersInPackageByFile(
						packageName + "." + file.getName(),
						file.getAbsolutePath(), recursive, controllers);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					Class<?> clazz = Thread.currentThread()
							.getContextClassLoader()
							.loadClass(packageName + '.' + className);
					if (Controller.class.isAssignableFrom(clazz)) {
						LogUtils.info(logger, "add controller %s",
								clazz.getCanonicalName());
						controllers.add(clazz);
					}

				} catch (ClassNotFoundException e) {
					LogUtils.severe(logger, e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		// 将request 和response放到threadload里面
		Context context = Context.getContext();
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		context.set("request", request);
		context.set("response", response);

		String servletPath = request.getServletPath();
		LogUtils.info(logger, "servletPath is %s", servletPath);

		Action action = null;
		String[] args = null;
		if (StringUtils.isBlank(servletPath)) {
			// 首页
			action = actionMap.get(URL_SEQ + INDEX_CONTROLLER);
		} else if (StringUtils.startsWith(servletPath, ASSETS_DIR)) {
			// 静态文件
			RequestDispatcher rd = this.getServletContext().getNamedDispatcher(
					COMMON_DEFAULT_SERVLET_NAME);
			rd.forward(request, response);
		} else {
			String[] strs = StringUtils.split(servletPath, '/');
			int size = strs.length;
			if (size == 1) {
				action = actionMap.get(URL_SEQ + strs[0]);
			} else {
				action = actionMap.get(URL_SEQ + strs[0] + URL_SEQ + strs[1]);
			}

			if (size > 2) { // rest方式参数
				args = new String[size - 2];
				for (int i = 2; i < size; i++) {
					args[i - 2] = strs[i];
				}
			}
		}

		if (action == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			try {
				action.work(convertParameters(action.getParameterTypes(),
						request, response, args));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LogUtils.severe(logger, e.getMessage(), e);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
		
	}

	private Object[] convertParameters(Class<?>[] types,
			HttpServletRequest request, HttpServletResponse response,
			String[] args) {
		Object[] parameters = new Object[0];
		// Class<?>[] types = action.getParameterTypes();
		if (types != null) {
			parameters = new Object[types.length];
			int i = 0;
			// action有参数，根据参数类型设置参数
			for (Class<?> type : types) {
				if (type == HttpServletRequest.class)
					parameters[i] = request;
				else if (type == HttpServletResponse.class)
					parameters[i] = response;
				else if (type == String.class)
					parameters[i] = (String) args[i];
				else if (type == Integer.class || type == int.class)
					parameters[i] = Integer.valueOf((String) args[i]);
				else if (type == Long.class || type == long.class)
					parameters[i] = Long.valueOf((String) args[i]);
				else if (type == Short.class || type == short.class)
					parameters[i] = Short.valueOf((String) args[i]);
				else if (type == Float.class || type == float.class)
					parameters[i] = Float.valueOf((String) args[i]);
				else if (type == Double.class || type == double.class)
					parameters[i] = Double.valueOf((String) args[i]);
				i++;
			}
		}
		return parameters;
	}
}
