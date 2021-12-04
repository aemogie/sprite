package io.github.aemogie.timble.utils.logging

import com.google.gson.JsonObject
import io.github.aemogie.timble.utils.console.STD_ERR
import java.lang.System.lineSeparator
import java.util.*
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaType
import kotlin.system.exitProcess

abstract class LoggerOutput internal constructor(config: JsonObject) {
	internal companion object {
		fun of(config: JsonObject) = config["class"]?.asString.let {
			if (it == null) {
				STD_ERR.write("Logger config doesn't have a \"class\" value.\n".toByteArray())
				exitProcess(-1)
			} else it
		}.let { name ->
			Class.forName(name).kotlin.let {
				if (it.isSubclassOf(LoggerOutput::class)) it
				else null
			}.let {
				if (it == null) {
					STD_ERR.write("Logger Class \"$name\" does not extend LoggerOutput.\n".toByteArray())
					exitProcess(-1)
				} else it
			}.constructors.singleOrNull {
				it.parameters.size == 1 && it.parameters[0].type.javaType == JsonObject::class.java
			}.let {
				if (it == null) {
					STD_ERR.write("Logger Class \"$name\" does not have a valid constructor.\n".toByteArray())
					exitProcess(-1)
				} else it
			}.call(config) as LoggerOutput
		}
	}
	
	val level = config["level"]?.asString?.uppercase(Locale.ENGLISH).let {
		if (it == null) Level.values()[0]
		else Level.valueOf(it)
	}
	
	//called once per log
	protected abstract fun print(text: String)
	
	//called per line in log content
	abstract fun format(record: LogRecord, current: String): String
	
	internal fun log(record: LogRecord) {
		if (record.level.ordinal >= level.ordinal) {
			//if u use `\n` on Windows, that's on you.
			print(record.content.toString().split(lineSeparator()).joinToString {
				format(record, it)
			})
		}
	}
	
	//optional destructor. for closing streams and writers.
	internal open fun destroy() = Unit
}