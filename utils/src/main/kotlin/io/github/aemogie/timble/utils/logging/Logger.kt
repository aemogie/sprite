package io.github.aemogie.timble.utils.logging

import com.google.gson.JsonObject
import com.google.gson.JsonStreamParser
import io.github.aemogie.timble.utils.StreamUtils
import io.github.aemogie.timble.utils.logging.LoggerOutput
import java.io.IOException
import java.io.PrintStream
import java.lang.reflect.Constructor
import java.util.*
import kotlin.system.exitProcess

class Logger internal constructor() {
	val outputs: MutableList<LoggerOutput> = ArrayList()
	private fun getLoggerOutput(className: String, level: Level, pattern: String, config: JsonObject?): LoggerOutput {
		val clazz: Class<*>
		try {
			clazz = Class.forName(className)
		} catch (e: ClassNotFoundException) {
			SYS_ERR.printf("Logger Class \"%s\" does not exist!%n", className)
			exitProcess(-1)
		}
		if (!LoggerOutput::class.java.isAssignableFrom(clazz)) {
			SYS_ERR.printf("Logger Class \"%s\" does not extend LoggerOutput!%n", className)
			exitProcess(-1)
		}
		val constructor: Constructor<*>
		try {
			constructor = clazz.getConstructor(Level::class.java, String::class.java, JsonObject::class.java)
		} catch (e: NoSuchMethodException) {
			SYS_ERR.printf("Logger Class \"%s\" does not have a valid constructor!%n", className)
			SYS_ERR.println("It should have a constructor with the parameters (Level logLevel, String pattern, @Nullable JsonObject config)")
			exitProcess(-1)
		}
		val instance: LoggerOutput
		try {
			instance = constructor.newInstance(level, pattern, config) as LoggerOutput
		} catch (e: Exception) {
			SYS_ERR.println("An error occurred while trying to create the Logger.")
			e.printStackTrace()
			exitProcess(-1)
		}
		return instance
	}
	
	enum class Level(val colour: Int, val out: PrintStream) {
		ALL(39, System.out), INFO(32, System.out), DEBUG(36, System.out), WARN(33, System.out), ERROR(31, System.err);
	}
	
	fun info(msg: Any): Boolean {
		return StreamUtils.runAllAndEval(outputs) { output: LoggerOutput -> output.info(msg) }
	}
	
	fun infoln(msg: Any): Boolean {
		return StreamUtils.runAllAndEval(outputs) { output: LoggerOutput -> output.infoln(msg) }
	}
	
	fun debug(msg: Any): Boolean {
		return StreamUtils.runAllAndEval(outputs) { output: LoggerOutput -> output.debug(msg) }
	}
	
	fun debugln(msg: Any): Boolean {
		return StreamUtils.runAllAndEval(outputs) { output: LoggerOutput -> output.debugln(msg) }
	}
	
	fun warn(msg: Any): Boolean {
		return StreamUtils.runAllAndEval(outputs) { output: LoggerOutput -> output.warn(msg) }
	}
	
	fun warnln(msg: Any): Boolean {
		return StreamUtils.runAllAndEval(outputs) { output: LoggerOutput -> output.warnln(msg) }
	}
	
	fun error(msg: Any): Boolean {
		return StreamUtils.runAllAndEval(outputs) { output: LoggerOutput -> output.error(msg) }
	}
	
	fun errorln(msg: Any): Boolean {
		return StreamUtils.runAllAndEval(outputs) { output: LoggerOutput -> output.errorln(msg) }
	}
	
	fun destroy(): Boolean {
		val destroySuccess = StreamUtils.runAllAndEval(outputs) { obj: LoggerOutput -> obj.destroy() }
		System.setOut(SYS_OUT)
		System.setErr(SYS_ERR)
		return destroySuccess && System.out === SYS_OUT && System.err === SYS_ERR
	}
	
	companion object {
		const val IS_ANSI_SUPPORTED = true
		val SYS_OUT = System.out
		val SYS_ERR = System.err
	}
	
	init {
		try {
			javaClass.getResourceAsStream("/META-INF/timble-logger.json").use { file ->
				if (file == null) {
					SYS_ERR.println("Unable to locate file - META-INF/timble-logger.json")
					exitProcess(-1)
				}
				val `object` = JsonStreamParser(String(file.readAllBytes())).next().asJsonObject
				for (element in `object`.getAsJsonArray("outputs")) {
					val output = element.asJsonObject
					val level = Level.valueOf(output["level"].asString.uppercase(Locale.ENGLISH))
					val pattern = output["pattern"].asString
					val confElem = output["config"]
					val config = confElem?.asJsonObject
					outputs.add(getLoggerOutput(output["class"].asString, level, pattern, config))
				}
				System.setOut(PrintStream(LoggerOutputStream { msg: String -> infoln(msg) }))
				System.setErr(PrintStream(LoggerOutputStream { msg: String -> errorln(msg) }))
			}
		} catch (e: IOException) {
			SYS_ERR.println("Unable to read from file - META-INF/timble-logger.json")
			exitProcess(-1)
		}
	}
}