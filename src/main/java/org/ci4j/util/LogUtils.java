package org.ci4j.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtils {
	public static final Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);
		logger.setLevel(Level.INFO);
		return logger;
	}
	
	public static final void info(Logger logger, String strFormat, Object... args) {
		logger.info(String.format(strFormat, args));
	}
	
	public static final void info(Logger logger, String msg) {
		logger.info(msg);
	}
	
	public static final void warning(Logger logger, String msg) {
		logger.warning(msg);
	}
	
	public static final void warning(Logger logger, String strFormat, Object... args) {
		logger.warning(String.format(strFormat, args));
	}
	
	public static final void severe(Logger logger, String msg, Throwable e) {
		logger.log(Level.SEVERE, msg, e);
	}
}
