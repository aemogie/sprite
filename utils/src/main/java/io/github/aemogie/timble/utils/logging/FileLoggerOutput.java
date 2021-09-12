package io.github.aemogie.timble.utils.logging;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Paths;

public class FileLoggerOutput extends LoggerOutput {
	private final String path;
	private final FileWriter file;
	
	public FileLoggerOutput(Logger.Level logLevel, String pattern, @Nullable JsonObject config) {
		super(logLevel, pattern, config);
		if (this.config == null) {
			Logger.SYS_ERR.printf("Could not locate config for \"%s\"! Please check your \"META-INF/timble-logger.json\"%n", getClass().getSimpleName());
			System.exit(-1);
		}
		JsonElement configPath = this.config.get("path");
		if (configPath == null) {
			Logger.SYS_ERR.printf("Could not locate config value \"path\" for \"%s\"! Please check your \"META-INF/timble-logger.json\"%n", getClass().getSimpleName());
			System.exit(-1);
		}
		path = configPath.getAsString();
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
					Logger.SYS_ERR.println("Could not create log file at: " + Paths.get(path).toAbsolutePath());
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
			Logger.SYS_ERR.println("Unable to write to log file.");
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
			Logger.SYS_ERR.println("Unable to close log file - " + Paths.get(path).toAbsolutePath());
			return false;
		}
	}
}
