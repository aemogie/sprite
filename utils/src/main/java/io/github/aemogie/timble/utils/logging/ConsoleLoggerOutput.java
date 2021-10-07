package io.github.aemogie.timble.utils.logging;

import com.google.gson.JsonObject;

import java.util.Arrays;

import static io.github.aemogie.timble.utils.logging.Logger.IS_ANSI_SUPPORTED;

public class ConsoleLoggerOutput extends LoggerOutput {
	
	public ConsoleLoggerOutput(Logger.Level logLevel, String pattern, JsonObject config) {
		super(logLevel, pattern, config);
	}
	
	@Override
	protected boolean print(String[] msg) {
		try {
			for (String s : msg) level.out.print(s);
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected String[] colourise(String[] out, Logger.Level level) {
		return IS_ANSI_SUPPORTED ? Arrays.stream(out).map(s -> "\u001b[" + level.colour + "m" + s + "\u001b[0m").toArray(String[]::new) : out;
	}
}
