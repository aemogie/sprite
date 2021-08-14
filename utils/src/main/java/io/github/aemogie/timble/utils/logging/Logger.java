package io.github.aemogie.timble.utils.logging;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Logger {
	public static final transient boolean IS_ANSI_SUPPORTED = true;
	private final static PrintStream SYS_OUT = System.out;
	private final static PrintStream SYS_ERR = System.err;
	final transient List<LoggerOutput> OUTPUTS = new ArrayList<>();
	
	Logger() {
		try (InputStream file = getClass().getResourceAsStream("/META-INF/timble-logger.json")) {
			JsonObject object = new JsonStreamParser(new String(requireNonNull(file).readAllBytes())).next().getAsJsonObject();
			for (JsonElement element : object.getAsJsonArray("outputs")) {
				JsonObject output = element.getAsJsonObject();
				String type = output.get("type").getAsString();
				Level level = Level.valueOf(output.get("level").getAsString().toUpperCase(Locale.ENGLISH));
				String pattern = output.get("pattern").getAsString();
				switch (type) {
					case "file" -> {
						String path = output.get("path").getAsString();
						OUTPUTS.add(new FileLoggerOutput(path, this, pattern, level));
					}
					case "console" -> OUTPUTS.add(new ConsoleLoggerOutput(this, pattern, level));
				}
			}
			
			System.setOut(new PrintStream(new LoggerOutputStream(this::infoln)));
			System.setErr(new PrintStream(new LoggerOutputStream(this::errorln)));
		} catch (IOException e) {
			System.err.println("Unable to read from file - META-INF/timble-logger.json");
		}
	}
	

	
	public enum Level {
		ALL(0, 39),
		INFO(1, 32),
		DEBUG(2, 36),
		WARN(3, 33),
		ERROR(4, 31),
		NONE(-1, 39),
		;
		public int PRIORITY;
		public int COLOUR;
		
		Level(int PRIORITY, int COLOUR) {
			this.PRIORITY = PRIORITY;
			this.COLOUR = COLOUR;
		}
		
	}
	
	public boolean safeError(String msg) {
		Optional<LoggerOutput> out = OUTPUTS.stream().filter(output -> output instanceof ConsoleLoggerOutput).findFirst();
		if (out.isPresent()) {
			out.get().error(msg);
			return true;
		}
		return false;
	}
	
	public boolean info(String msg) {return OUTPUTS.stream().allMatch(output -> output.info(msg));}
	public boolean infoln(String msg) {return OUTPUTS.stream().allMatch(output -> output.infoln(msg));}
	public boolean debug(String msg) {return OUTPUTS.stream().allMatch(output -> output.debug(msg));}
	public boolean debugln(String msg) {return OUTPUTS.stream().allMatch(output -> output.debugln(msg));}
	public boolean warn(String msg) {return OUTPUTS.stream().allMatch(output -> output.warn(msg));}
	public boolean warnln(String msg) {return OUTPUTS.stream().allMatch(output -> output.warnln(msg));}
	public boolean error(String msg) {return OUTPUTS.stream().allMatch(output -> output.error(msg));}
	public boolean errorln(String msg) {return OUTPUTS.stream().allMatch(output -> output.errorln(msg));}
	
	@Deprecated //Use timble-logger.json instead
	boolean setLevelForAll(Level level) {
		return OUTPUTS.stream().allMatch(output -> output.setLevel(level));
	}
	
	boolean destroy() {
		boolean destroySuccess = OUTPUTS.stream().allMatch(LoggerOutput::destroy);
		System.setOut(SYS_OUT);
		System.setErr(SYS_ERR);
		return destroySuccess && System.out == SYS_OUT && System.err == SYS_ERR;
	}
}