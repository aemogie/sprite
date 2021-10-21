package io.github.aemogie.timble.utils.logging

import com.google.gson.JsonObject

class ConsoleLoggerOutput(logLevel: Logger.Level, pattern: String, config: JsonObject?) :
	LoggerOutput(logLevel, pattern, config) {
	override fun print(msg: Array<String?>): Boolean {
		return try {
			for (s in msg) level.out.print(s)
			true
		} catch (exception: Exception) {
			exception.printStackTrace()
			false
		}
	}
	
	override fun colourise(out: Array<String?>, level: Logger.Level): Array<String?> {
		return if (Logger.IS_ANSI_SUPPORTED) {
			out.map { s: String? -> "\u001b[" + level.colour + "m" + s + "\u001b[0m" }.toTypedArray()
		} else out
	}
}