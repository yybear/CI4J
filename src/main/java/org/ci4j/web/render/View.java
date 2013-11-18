package org.ci4j.web.render;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ci4j.core.Context;

public abstract class View implements Serializable {
	private static final long serialVersionUID = -265235795849978855L;
	
	protected String viewDir = "/WEB-INF/views";
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	
	public View() {
		Context context = Context.getContext();
		request = context.get("request");
		response = context.get("response");
	}
}
