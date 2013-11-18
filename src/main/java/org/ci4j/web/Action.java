package org.ci4j.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.ci4j.util.LogUtils;

public class Action {
	private static final Logger logger = LogUtils.getLogger(Action.class.getName());
	private Object controller;

	private Method method;
	
	private Class<?>[] parameterTypes;

	public Action(Object controller, Method method, Class<?>[] parameterTypes) {
		this.controller = controller;
		this.method = method;
		this.parameterTypes = parameterTypes;
	}

	public void work(Object[] args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		method.invoke(controller, args);
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	
}
