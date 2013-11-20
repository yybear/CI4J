package org.ci4j.web.render;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Logger;

import org.ci4j.util.LogUtils;

import com.alibaba.fastjson.JSON;

public class JsonView extends View {
	private static final Logger logger = LogUtils.getLogger(JsonView.class
			.getName());
	
	private static final String contentType = "application/json;charset=UTF-8";
	//private static final String contentTypeForIE = "text/html;charset=UTF-8";

	public JsonView() {
		super();
	}

	public void load(Map<String, ?> map) {
		String jsonText = JSON.toJSONString(map);
		PrintWriter writer = null;
		try {
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);

			response.setContentType(contentType);
			writer = response.getWriter();
			writer.write(jsonText);
			writer.flush();
		} catch (IOException e) {
			LogUtils.severe(logger, e.getMessage(), e);
			//throw new RenderException(e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}
}
