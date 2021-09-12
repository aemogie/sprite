package io.github.aemogie.timble.utils.logging;

public class LogManager {
	private LogManager() {}
	
	private static Logger logger = null;
	
	private static Logger createLogger() {
		logger = new Logger();
		return logger;
	}
	
	public static boolean nullifyLogger() {
		if (logger.destroy()) {
			logger = null;
			return true;
		}
		return false;
	}
	
	public static Logger getLogger() {
		return logger == null ? createLogger() : logger;
	}
}