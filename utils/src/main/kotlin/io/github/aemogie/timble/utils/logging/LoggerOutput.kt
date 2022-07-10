package io.github.aemogie.timble.utils.logging

import io.github.aemogie.timble.utils.console.STD_ERR
import kotlinx.serialization.json.*
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.System.lineSeparator
import java.util.*
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaType
import kotlin.system.exitProcess

abstract class LoggerOutput internal constructor(config: JsonObject) {
	internal companion object {

		private val THROWABLE_WRITER = object : PrintWriter(StringWriter()) {
			fun get(throwable: Throwable): String {
				throwable.printStackTrace(this)
				val buffer = (out as StringWriter).buffer
				return synchronized(buffer) {
					val ret = buffer.toString()
					buffer.setLength(0)
					return@synchronized ret
				}
			}
		}

		operator fun invoke(config: JsonElement): LoggerOutput {
			val className = (config.jsonObject["class"] ?: run {
				STD_ERR.write("Logger config doesn't have a \"class\" value.${lineSeparator()}".toByteArray())
				exitProcess(-1)
			}).jsonPrimitive.contentOrNull ?: run {
				STD_ERR.write("Logger Class is a non-null value.${lineSeparator()}".toByteArray())
				exitProcess(-1)
			}

			val clazz = runCatching {
				Class.forName(className)
			}.getOrElse {
				STD_ERR.write("Logger Class \"$className\" could not be found.${lineSeparator()}".toByteArray())
				exitProcess(-1)
			}.kotlin.takeIf {
				it.isSubclassOf(LoggerOutput::class)
			} ?: run {
				STD_ERR.write("Logger Class \"$className\" does not extend LoggerOutput.${lineSeparator()}".toByteArray())
				exitProcess(-1)
			}

			val constructor = clazz.constructors.singleOrNull {
				it.parameters.size == 1 && it.parameters[0].type.javaType == JsonObject::class.java
			} ?: run {
				STD_ERR.write("Logger Class \"$className\" does not have a valid constructor.${lineSeparator()}".toByteArray())
				exitProcess(-1)
			}

			return constructor.call(config) as LoggerOutput
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
			is Throwable -> THROWABLE_WRITER.get(content)
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