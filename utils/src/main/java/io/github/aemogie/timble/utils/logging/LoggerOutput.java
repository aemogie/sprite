package io.github.aemogie.timble.utils.logging;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

import static io.github.aemogie.timble.utils.StringUtils.integerWithSpecifiedDigits;
import static io.github.aemogie.timble.utils.logging.Logger.Level.*;

public abstract class LoggerOutput {
	private static final HashMap<String, Supplier<String>> UNIVERSAL_VARIABLES = new HashMap<>();
	
	static {
		addVariables("hour", () -> integerWithSpecifiedDigits(LocalDateTime.now().getHour(), 2));
		addVariables("min", () -> integerWithSpecifiedDigits(LocalDateTime.now().getMinute(), 2));
		addVariables("sec", () -> integerWithSpecifiedDigits(LocalDateTime.now().getSecond(), 2));
		addVariables("thread", Thread.currentThread()::getName);
	}
	
	protected final String pattern;
	protected Logger.Level level;
	protected final JsonObject config;
	
	protected LoggerOutput(Logger.Level logLevel, String pattern, @Nullable JsonObject config) {
		this.pattern = pattern;
		this.level = logLevel;
		this.config = config;
	}
	
	public static boolean addVariables(String variable, Supplier<String> valueSupplier) {
		if (!variable.matches("[a-zA-Z0-9-_]+")) return false;
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
		Arrays.fill(out, pattern);
		for (int i = 0; i < out.length; i++) {
			if (in[i].endsWith("\r")) in[i] = in[i].substring(0, in[i].length() - 1);
			for (Map.Entry<String, Supplier<String>> entry : UNIVERSAL_VARIABLES.entrySet()) {
				out[i] = out[i].replaceAll("(?<!\\\\)!!" + entry.getKey(), String.valueOf(entry.getValue().get()));
			}
			out[i] = out[i].replaceAll("(?<!\\\\)!!level", String.valueOf(level)).replaceAll("(?<!\\\\)!!msg", in[i]);
			out[i] = out[i].replace("\\!!", "!!");
		}
		return colourise(out, level);
	}
	
	protected abstract String[] colourise(String[] out, Logger.Level level);
	
	private boolean log(Object message, Logger.Level error) {
		if (error.ordinal() >= level.ordinal()) return print(getFormatted(String.valueOf(message), error));
		return false;
	}
	
	private boolean logln(Object message, Logger.Level error) {
		if (error.ordinal() >= level.ordinal()) return println(getFormatted(String.valueOf(message), error));
		return false;
	}
	
	boolean info(Object message) {
		return log(message, INFO);
	}
	
	boolean infoln(Object message) {
		return logln(message, INFO);
	}
	
	boolean debug(Object message) {
		return log(message, DEBUG);
	}
	
	boolean debugln(Object message) {
		return logln(message, DEBUG);
	}
	
	boolean warn(Object message) {
		return log(message, WARN);
	}
	
	boolean warnln(Object message) {
		return logln(message, WARN);
	}
	
	boolean error(Object message) {
		return log(message, ERROR);
	}
	
	boolean errorln(Object message) {
		return logln(message, ERROR);
	}
	
	boolean destroy() {
		return true;
	}
}