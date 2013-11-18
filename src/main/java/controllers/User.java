package controllers;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.ci4j.util.LogUtils;
import org.ci4j.web.Controller;
import org.ci4j.web.render.JspView;

public class User extends Controller {
	
	private static final Logger logger = LogUtils.getLogger(User.class.getName());
	public void login() {
		LogUtils.info(logger, "login");
		JspView view = new JspView();
		//HtmlView view = new HtmlView();
		view.load("login");
	}
	
	public void doLogin(HttpServletRequest request) {
		String name = request.getParameter("name");
		String passwd = request.getParameter("passwd");
		
		LogUtils.info(logger, name);
		LogUtils.info(logger, passwd);
	}
	
	public void home() {
		
	}
}
