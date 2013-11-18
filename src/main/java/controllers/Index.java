package controllers;

import java.util.logging.Logger;

import org.ci4j.util.LogUtils;
import org.ci4j.web.Controller;
import org.ci4j.web.annotation.Path;
import org.ci4j.web.render.HtmlView;
import org.ci4j.web.render.JspView;

public class Index extends Controller {
	private static final Logger logger = LogUtils.getLogger("Index");
	public void index() {
		JspView view = new JspView();
		view.load("index");
	}
	
	public void login() {
		LogUtils.info(logger, "login");
		JspView view = new JspView();
		//HtmlView view = new HtmlView();
		view.load("login");
	}
}
