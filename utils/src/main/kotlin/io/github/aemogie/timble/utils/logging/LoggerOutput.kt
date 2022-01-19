package io.github.aemogie.timble.utils.logging

import io.github.aemogie.timble.utils.console.STD_ERR
import io.github.aemogie.timble.utils.exitIfNull
import io.github.aemogie.timble.utils.println
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.System.lineSeparator
import java.util.*
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaType
import kotlin.system.exitProcess

abstract class LoggerOutput internal constructor(config: JsonObject) {
	internal companion object {

		private val throwableWriter = object : PrintWriter(StringWriter()) {
			fun get(throwable: Throwable): String {
				throwable.printStackTrace(this)
				val buffer = (out as StringWriter).buffer
				return synchronized(buffer) {
					val ret = buffer.toString()
					buffer.setLength(0)
					ret
				}
			}
		}

		fun of(config: JsonObject): LoggerOutput {
			val className = config["class"].exitIfNull {
				STD_ERR.println("Logger config doesn't have a \"class\" value.")
			}.jsonPrimitive.contentOrNull.exitIfNull {
				STD_ERR.println("Logger Class can't be `null`.")
			}

			return runCatching {
				Class.forName(className)
			}.getOrElse {
				STD_ERR.println("Logger Class \"$className\" could not be found.")
				exitProcess(-1)
			}.kotlin.takeIf {
				it.isSubclassOf(LoggerOutput::class)
			}.exitIfNull {
				STD_ERR.println("Logger Class \"$className\" does not extend LoggerOutput.")
			}.constructors.singleOrNull {
				it.parameters.size == 1 && it.parameters[0].type.javaType == JsonObject::class.java
			}.exitIfNull {
				STD_ERR.println("Logger Class \"$className\" does not have a valid constructor.")
			}.call(config) as LoggerOutput
		}
	}

	val level = config["level"]?.jsonPrimitive?.contentOrNull?.uppercase(Locale.ENGLISH).let {
		if (it == null) Level.values()[0]
		else Level.valueOf(it)
	}

	//called once per log
	protected abstract fun print(text: String)

	//called per line in log content
	abstract fun format(record: LogRecord, current: String): String

	internal fun log(record: LogRecord) {

		fun LogRecord.getPrintable() = when (content) {
			null -> "null"
			is () -> Any? -> (content)().toString()
			is Throwable -> throwableWriter.get(content)
			is Array<*> -> content.contentToString()
			else -> content.toString()
		}

		if (record.level.ordinal >= level.ordinal) {
			//if u use `\n` on Windows, that's on you.
			print(record.getPrintable().split(lineSeparator()).joinToString { format(record, it) })
		}
	}

	//optional destructor. for closing streams and writers.
	internal open fun destroy() = Unit
}