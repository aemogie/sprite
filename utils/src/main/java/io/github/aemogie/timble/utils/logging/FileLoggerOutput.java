package io.github.aemogie.timble.utils.logging;

import java.io.*;
import java.nio.file.Paths;

public class FileLoggerOutput extends LoggerOutput {
	private final String path;
	private final FileWriter file;
	
	public FileLoggerOutput(String path, Logger logger, String pattern, Logger.Level level) {
		super(logger, pattern, level);
		this.path = path;
		FileWriter tempFile = null;
		try {
			tempFile = new FileWriter(path);
		} catch (IOException e) {
			int i = path.lastIndexOf('/') + 1;
			File directory = new File(path.substring(0, i));
			if (!directory.exists() && directory.mkdir()) {
				try {
					tempFile = new FileWriter(path);
				} catch (IOException ioException) {
					logger.safeError("Could not create log file at: " + Paths.get(path).toAbsolutePath());
				}
			}
		}
		file = tempFile;
	}
	
	@Override
	protected boolean print(String[] msg) {
		try {
			for (String s : msg) file.write(s);
			file.flush();
			return true;
		} catch (IOException e) {
			logger.safeError("Unable to write to log file.");
			return false;
		}
	}
	
	@Override
	protected String[] colourise(String[] out, Logger.Level level) {
		return out;
	}
	
	@Override
	boolean destroy() {
		try {
			file.close();
			return true;
		} catch (IOException e) {
			logger.safeError("Unable to close log file - " + Paths.get(path).toAbsolutePath());
			return false;
		}
	}
}
