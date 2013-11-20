package org.ci4j.web.render;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.ci4j.util.LogUtils;

public class JspView extends View{
	private static final Logger logger = LogUtils.getLogger("JspView");
	private static final long serialVersionUID = -2640873349994508637L;
	
	private static final String EXT = ".jsp";
	
	public JspView() {
		super();
	}

	public void load(String view) {
		try {
			RequestDispatcher rd = request.getRequestDispatcher(viewDir + File.separator + view + EXT);
			rd.forward(request, response);
		} catch (ServletException | IOException e) {
			LogUtils.severe(logger, e.getMessage(), e);
		}
	}
	
	public void load(String view, Model model) {
		for(String key : model.getValues().keySet()) {
			request.setAttribute(key, model.getValues().get(key));
		}
		
		load(view);
	}
}
