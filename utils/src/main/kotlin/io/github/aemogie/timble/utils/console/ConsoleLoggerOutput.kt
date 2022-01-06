package io.github.aemogie.timble.utils.console

import com.google.gson.JsonObject
import io.github.aemogie.timble.utils.console.ANSIModifier.*
import io.github.aemogie.timble.utils.logging.Level.WARN
import io.github.aemogie.timble.utils.logging.LogRecord
import io.github.aemogie.timble.utils.logging.LoggerOutput

@Suppress("unused") //used via reflection, from the json config
class ConsoleLoggerOutput(config: JsonObject) : LoggerOutput(config) {

	//a hashmap would be syntactically cleaner, but eh.
	enum class LogLevelColours(internal val colour: String) {
		ALL(ANSIModifier(BOLD, DEFAULT_FG)),
		INFO(ANSIModifier(BOLD, GREEN_FG)),
		DEBUG(ANSIModifier(BOLD, BLUE_FG)),
		WARN(ANSIModifier(BOLD, YELLOW_FG)),
		ERROR(ANSIModifier(BOLD, RED_FG))
	}

	override fun print(text: String) {
		(if (level.ordinal >= WARN.ordinal) STD_ERR else STD_OUT).also {
			it.write(text.toByteArray())
			it.flush()
		}
	}

	override fun format(record: LogRecord, current: String): String = record.instant.toEpochMilli().let {
		"%s[%tH:%tM:%tS] [%5s|%5s] [%s]%s %s%n".format(
			LogLevelColours.valueOf(record.level.name).colour,
			it, it, it,
			record.thread.name.removePrefix("Thread-"),
			record.level,
			record.caller.className.substringAfterLast('.'),
			ANSIModifier(RESET),
			current
		)
	}
}