package io.github.aemogie.timble.utils.logging;

import java.io.PrintStream;
import java.util.Arrays;

import static io.github.aemogie.timble.utils.logging.Logger.IS_ANSI_SUPPORTED;

public class ConsoleLoggerOutput extends LoggerOutput {
	private static final PrintStream SYS_OUT = System.out;
	
	public ConsoleLoggerOutput(final Logger LOGGER, String PATTERN, Logger.Level level) {
		super(LOGGER, PATTERN, level);
	}
	
	@Override
	protected boolean print(String[] msg) {
		try {
			for (String s : msg) SYS_OUT.print(s);
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected String[] colourise(String[] out, Logger.Level level) {
		return IS_ANSI_SUPPORTED ? Arrays.stream(out).map(s -> "\u001b[" + level.COLOUR + "m" + s + "\u001b[0m").toArray(String[]::new) : out;
	}
}
