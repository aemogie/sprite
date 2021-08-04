package io.github.aemogie.timble.utils.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import static io.github.aemogie.timble.utils.Annotations.Relative;

public class FileLoggerOutput extends LoggerOutput {
	private final String PATH;
	private final transient FileWriter file;
	public FileLoggerOutput(@Relative String PATH, Logger LOGGER, String PATTERN, Logger.Level level) {
		super(LOGGER, PATTERN, level);
		this.PATH = PATH;
		FileWriter tempFile = null;
		try {
			tempFile = new FileWriter(PATH);
		} catch (IOException e) {
			int i = PATH.lastIndexOf('/') + 1;
			File directory = new File(PATH.substring(0, i));
			if (!directory.exists() && directory.mkdir()) {
				try {
					tempFile = new FileWriter(PATH);
				} catch (IOException ioException) {
					LOGGER.safeError("Could not create log file at: " + Paths.get(PATH).toAbsolutePath());
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
			LOGGER.safeError("Unable to write to log file.");
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
			LOGGER.safeError("Unable to close log file - " + Paths.get(PATH).toAbsolutePath());
			return false;
		}
	}
}
