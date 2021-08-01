package io.github.aemogie.timble.utils.logging;

import java.io.PrintStream;

import static io.github.aemogie.timble.utils.logging.Logger.IS_ANSI_SUPPORTED;

public class ConsoleLoggerOutput extends LoggerOutput {
	private static final PrintStream SYS_OUT = System.out;
	
	public ConsoleLoggerOutput(final Logger LOGGER, String PATTERN, Logger.Level level) {
		super(LOGGER, PATTERN, level);
	}
	
	@Override
	protected boolean print(String msg) {
		try {
			SYS_OUT.print(msg);
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected String colourise(String out, Logger.Level level) {
		return IS_ANSI_SUPPORTED ? "\u001b[" + level.COLOUR + "m" + out + "\u001b[0m" : out;
	}
}
