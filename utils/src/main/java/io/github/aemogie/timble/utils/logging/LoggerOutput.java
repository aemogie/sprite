package io.github.aemogie.timble.utils.logging;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static io.github.aemogie.timble.utils.StringUtils.integerWithSpecifiedDigits;
import static io.github.aemogie.timble.utils.logging.Logger.Level.*;

public abstract class LoggerOutput {
	private static final HashMap<String, Supplier<String>> UNIVERSAL_VARIABLES = new HashMap<>();
	
	static {
		addVariables("hour", () -> integerWithSpecifiedDigits(LocalDateTime.now().getHour(), 2));
		addVariables("min", () -> integerWithSpecifiedDigits(LocalDateTime.now().getMinute(), 2));
		addVariables("sec", () -> integerWithSpecifiedDigits(LocalDateTime.now().getSecond(), 2));
		addVariables("thread", () -> Thread.currentThread().getName());
	}
	
	protected final transient Logger LOGGER;
	protected final String PATTERN;
	protected Logger.Level level;
	
	protected LoggerOutput(final Logger LOGGER, final String PATTERN, Logger.Level level) {
		this.LOGGER = LOGGER;
		this.PATTERN = PATTERN;
		this.level = level;
	}
	
	public static boolean addVariables(String variable, Supplier<String> valueSupplier) {
		UNIVERSAL_VARIABLES.put(variable, valueSupplier);
		return UNIVERSAL_VARIABLES.get(variable).equals(valueSupplier);
	}
	
	protected abstract boolean print(String[] msg);
	
	protected boolean println(String[] msg) {
		return print(Arrays.stream(msg).map(s -> s + "\n").toArray(String[]::new));
	}
	
	private String[] getFormatted(String message, Logger.Level level) {
		String[] in = message.split("\n");
		String[] out = new String[in.length];
		Arrays.fill(out, PATTERN);
		for (int i = 0; i < out.length; i++) {
			if (in[i].endsWith("\r")) in[i] = in[i].substring(0, in.length - 1);
			for (Map.Entry<String, Supplier<String>> entry : UNIVERSAL_VARIABLES.entrySet()) {
				out[i] = out[i].replace("!!" + entry.getKey(), String.valueOf(entry.getValue().get()));
			}
			out[i] = out[i].replace("!!level", String.valueOf(level)).replace("!!msg", in[i]);
		}
		return colourise(out, level);
	}
	
	protected abstract String[] colourise(String[] out, Logger.Level level);
	
	private boolean log(String message, Logger.Level error) {
		if (error.PRIORITY >= level.PRIORITY) return print(getFormatted(message, error));
		return false;
	}
	
	private boolean logln(String message, Logger.Level error) {
		if (error.PRIORITY >= level.PRIORITY) return println(getFormatted(message, error));
		return false;
	}
	
	boolean info(String message) {
		return log(message, INFO);
	}
	
	boolean infoln(String message) {
		return logln(message, INFO);
	}
	
	boolean debug(String message) {
		return log(message, DEBUG);
	}
	
	boolean debugln(String message) {
		return logln(message, DEBUG);
	}
	
	boolean warn(String message) {
		return log(message, WARN);
	}
	
	boolean warnln(String message) {
		return logln(message, WARN);
	}
	
	boolean error(String message) {
		return log(message, ERROR);
	}
	
	boolean errorln(String message) {
		return logln(message, ERROR);
	}
	
	@Deprecated //Use timble-logger.json instead
	boolean setLevel(Logger.Level level) {
		try {
			this.level = level;
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}
	
	boolean destroy() {
		return true;
	}
}