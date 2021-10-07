package io.github.aemogie.timble.utils.logging;

import com.google.gson.*;
import io.github.aemogie.timble.utils.StreamUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Logger {
	public static final boolean IS_ANSI_SUPPORTED = true;
	public static final PrintStream SYS_OUT = System.out;
	public static final PrintStream SYS_ERR = System.err;
	final List<LoggerOutput> outputs = new ArrayList<>();
	
	Logger() {
		try (InputStream file = getClass().getResourceAsStream("/META-INF/timble-logger.json")) {
			if (file == null) {
				SYS_ERR.println("Unable to locate file - META-INF/timble-logger.json");
				System.exit(-1);
			}
			
			JsonObject object = new JsonStreamParser(new String(file.readAllBytes())).next().getAsJsonObject();
			for (JsonElement element : object.getAsJsonArray("outputs")) {
				JsonObject output = element.getAsJsonObject();
				Level level = Level.valueOf(output.get("level").getAsString().toUpperCase(Locale.ENGLISH));
				String pattern = output.get("pattern").getAsString();
				JsonElement confElem = output.get("config");
				JsonObject config = confElem != null ? confElem.getAsJsonObject() : null;
				outputs.add(getLoggerOutput(output.get("class").getAsString(), level, pattern, config));
			}
			
			System.setOut(new PrintStream(new LoggerOutputStream(this::infoln)));
			System.setErr(new PrintStream(new LoggerOutputStream(this::errorln)));
		} catch (IOException e) {
			SYS_ERR.println("Unable to read from file - META-INF/timble-logger.json");
			System.exit(-1);
		}
	}
	
	private @NotNull LoggerOutput getLoggerOutput(String className, Level level, String pattern, @Nullable JsonObject config) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			SYS_ERR.printf("Logger Class \"%s\" does not exist!%n", className);
			System.exit(-1);
		}
		if (!LoggerOutput.class.isAssignableFrom(clazz)) {
			SYS_ERR.printf("Logger Class \"%s\" does not extend LoggerOutput!%n", className);
			System.exit(-1);
		}
		Constructor<?> constructor = null;
		try {
			constructor = clazz.getConstructor(Level.class, String.class, JsonObject.class);
		} catch (NoSuchMethodException e) {
			SYS_ERR.printf("Logger Class \"%s\" does not have a valid constructor!%n", className);
			SYS_ERR.println("It should have a constructor with the parameters (Level logLevel, String pattern, @Nullable JsonObject config)");
			System.exit(-1);
		}
		LoggerOutput instance = null;
		try {
			instance = (LoggerOutput) constructor.newInstance(level, pattern, config);
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException | ClassCastException e) {
			SYS_ERR.println("An error occurred while trying to create the Logger.");
			e.printStackTrace();
			System.exit(-1);
		}
		return instance;
	}
	
	
	public enum Level {
		ALL(39, System.out),
		INFO(32, System.out),
		DEBUG(36, System.out),
		WARN(33, System.out),
		ERROR(31, System.err)
		;
		
		public final int colour;
		public final PrintStream out;
		
		Level(int colour, PrintStream out) {
			this.colour = colour;
			this.out = out;
		}
	}
	
	public boolean    info(Object msg) {return StreamUtils.runAllAndEval(outputs, output -> output.   info(msg));}
	public boolean  infoln(Object msg) {return StreamUtils.runAllAndEval(outputs, output -> output. infoln(msg));}
	public boolean   debug(Object msg) {return StreamUtils.runAllAndEval(outputs, output -> output.  debug(msg));}
	public boolean debugln(Object msg) {return StreamUtils.runAllAndEval(outputs, output -> output.debugln(msg));}
	public boolean    warn(Object msg) {return StreamUtils.runAllAndEval(outputs, output -> output.   warn(msg));}
	public boolean  warnln(Object msg) {return StreamUtils.runAllAndEval(outputs, output -> output. warnln(msg));}
	public boolean   error(Object msg) {return StreamUtils.runAllAndEval(outputs, output -> output.  error(msg));}
	public boolean errorln(Object msg) {return StreamUtils.runAllAndEval(outputs, output -> output.errorln(msg));}
	
	boolean destroy() {
		boolean destroySuccess = StreamUtils.runAllAndEval(outputs, LoggerOutput::destroy);
		System.setOut(SYS_OUT);
		System.setErr(SYS_ERR);
		return destroySuccess && System.out == SYS_OUT && System.err == SYS_ERR;
	}
}