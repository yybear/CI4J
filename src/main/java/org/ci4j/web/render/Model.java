package org.ci4j.web.render;

import java.util.HashMap;
import java.util.Map;

public class Model {
	private Map<String, Object> values = new HashMap<String, Object>();
	
	public Map<String, Object> getValues() {
		return values;
	}

	public Model(String key, Object value) {
		values.put(key, value);
	}
	
	public Model(Map<String, Object> map) {
		this.values.putAll(map);
	}
}
