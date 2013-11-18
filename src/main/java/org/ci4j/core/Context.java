package org.ci4j.core;

import java.util.HashMap;
import java.util.Map;

public class Context {
	private static ThreadLocal<Context> LOCAL = new InheritableThreadLocal<Context>() {
        @Override
        protected Context initialValue() {
            return new Context();
        }
    };
    
    private final Map<String, Object> values = new HashMap<String, Object>();
    
    private Context() {
    }

    public static Context getContext() {
        return LOCAL.get();
    }

    public static void clearContext() {
        LOCAL.remove();
    }

    public Context set(String key, Object value) {
        if (value == null) {
            values.remove(key);
        } else {
            values.put(key, value);
        }
        return this;
    }

    public Context remove(String key) {
        values.remove(key);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) values.get(key);
    }
}
