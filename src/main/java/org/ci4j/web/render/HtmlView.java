package org.ci4j.web.render;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;


import org.ci4j.util.LogUtils;

public class HtmlView extends View{
	private static final Logger logger = LogUtils.getLogger("HtmlView");
	private static final long serialVersionUID = -2640873349994508637L;
	
	private static final String EXT = ".html";
	
	public HtmlView() {
		super();
	}

	public void load(String view) {
		PrintWriter writer = null;
		try {
			response.setHeader("Pragma", "no-cache");	// HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
	        response.setHeader("Cache-Control", "no-cache");
	        response.setDateHeader("Expires", 0);
	        
			response.setContentType("text/html;charset=UTF-8");
	        writer = response.getWriter();
	        writer.write("test html");
	        writer.flush();
		} catch (IOException e) {
			//throw new RenderException(e);
		}
	}
	
	public void load(String view, Model model) {
		
	}
}
