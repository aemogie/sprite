package io.github.aemogie.timble.utils.console

import com.google.gson.JsonObject
import io.github.aemogie.timble.utils.console.ANSIModifier.*
import io.github.aemogie.timble.utils.logging.LogRecord
import io.github.aemogie.timble.utils.logging.Logger.Level.WARN
import io.github.aemogie.timble.utils.logging.LoggerOutput

@Suppress("unused") //used via reflection, from the json config
class ConsoleLoggerOutput(config: JsonObject) : LoggerOutput(config) {
	
	//a hashmap would be syntactically cleaner, but eh.
	enum class LogLevelColours(internal val colour: String) {
		ALL(ANSIModifier.of(BOLD, DEFAULT_FG)),
		INFO(ANSIModifier.of(BOLD, GREEN_FG)),
		DEBUG(ANSIModifier.of(BOLD, BLUE_FG)),
		WARN(ANSIModifier.of(BOLD, YELLOW_FG)),
		ERROR(ANSIModifier.of(BOLD, RED_FG))
	}
	
	override fun print(text: String): Boolean {
		(if (level.ordinal >= WARN.ordinal) STD_ERR else STD_OUT).also {
			it.write(text.toByteArray())
			it.flush()
		}
		return true
	}
	
	override fun format(record: LogRecord, current: String): String = String.format(
		"%1\$s[%2\$tH:%2\$tM:%2\$tS] [%3$5s|%4$5s] [%5\$s]%6\$s %7\$s%n",
		LogLevelColours.valueOf(record.level.name).colour,
		record.instant.toEpochMilli(),
		record.thread.name.removePrefix("Thread-"),
		record.level,
		record.caller.className.substringAfterLast('.'),
		ANSIModifier.of(RESET),
		current
	)
}