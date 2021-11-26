package io.github.aemogie.timble.utils.logging

import com.google.gson.JsonObject
import java.lang.System.lineSeparator
import java.util.*

abstract class LoggerOutput internal constructor(config: JsonObject) {
	val level = config["level"]?.asString?.uppercase(Locale.ENGLISH).let {
		if (it == null) Logger.Level.values()[0]
		else Logger.Level.valueOf(it)
	}
	
	//called once per log
	protected abstract fun print(text: String): Boolean
	
	//called per line in log content
	abstract fun format(record: LogRecord, current: String): String
	
	internal fun log(record: LogRecord): Boolean {
		return if (record.level.ordinal >= level.ordinal) {
			print(record.content.flatMap {
				//if u use `\n` on Windows, that's on you.
				it.toString().split(lineSeparator())
			}.joinToString {
				format(record, it)
			})
		} else false
	}
	
	//optional destructor. for closing streams and writers.
	internal open fun destroy(): Boolean = true
}