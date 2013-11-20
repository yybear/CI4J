package controllers;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.ci4j.util.LogUtils;
import org.ci4j.web.Controller;
import org.ci4j.web.render.JsonView;
import org.ci4j.web.render.JspView;
import org.ci4j.web.render.Model;
import org.ci4j.web.upload.MultipartRequest;
import org.ci4j.web.upload.UploadFile;

public class User extends Controller {
	
	private static final Logger logger = LogUtils.getLogger(User.class.getName());
	public void login() {
		LogUtils.info(logger, "login");
		//JspView view = new JspView();
		Model model = new Model("dd", "htllp");
		//view.load("login", model);
		//HtmlView view = new HtmlView();
		JsonView view = new JsonView();
		
		view.load(model.getValues());
	}
	
	public void doLogin(HttpServletRequest request) {
		String name = request.getParameter("name");
		String passwd = request.getParameter("passwd");
		
		LogUtils.info(logger, name);
		LogUtils.info(logger, passwd);
	}
	
	public void upload() {
		JspView view = new JspView();
		view.load("upload");
	}
	
	public void doUpload(MultipartRequest request) {
		List<UploadFile> files = request.getFiles();
		
		for(UploadFile file : files) {
			LogUtils.info(logger, file.getFileName());
		}
	}
}
