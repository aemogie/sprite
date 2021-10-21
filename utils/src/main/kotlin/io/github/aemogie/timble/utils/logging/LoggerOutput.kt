package io.github.aemogie.timble.utils.logging

import com.google.gson.JsonObject
import io.github.aemogie.timble.utils.StringUtils
import java.time.LocalDateTime
import java.util.*
import java.util.function.Supplier

abstract class LoggerOutput protected constructor(
	protected var level: Logger.Level,
	protected val pattern: String,
	protected val config: JsonObject?
) {
	companion object {
		private val UNIVERSAL_VARIABLES = HashMap<String, Supplier<String?>>()
		fun addVariables(variable: String, valueSupplier: Supplier<String?>): Boolean {
			if (!variable.matches("[a-zA-Z0-9-_]+".toRegex())) return false
			UNIVERSAL_VARIABLES[variable] = valueSupplier
			return UNIVERSAL_VARIABLES[variable] == valueSupplier
		}
		
		init {
			addVariables("hour") { StringUtils.integerWithSpecifiedDigits(LocalDateTime.now().hour, 2) }
			addVariables("min") { StringUtils.integerWithSpecifiedDigits(LocalDateTime.now().minute, 2) }
			addVariables("sec") { StringUtils.integerWithSpecifiedDigits(LocalDateTime.now().second, 2) }
			addVariables("thread") { Thread.currentThread().name }
		}
	}
	
	protected abstract fun print(msg: Array<String?>): Boolean
	protected fun println(msg: Array<String?>): Boolean {
		return this.print(msg.map { "$it\n" }.toTypedArray())
	}
	
	private fun getFormatted(message: String, level: Logger.Level): Array<String?> {
		val `in` = message.split("\n".toRegex()).toTypedArray()
		val out = arrayOfNulls<String>(`in`.size)
		Arrays.fill(out, pattern)
		for (i in out.indices) {
			if (`in`[i].endsWith("\r")) `in`[i] = `in`[i].substring(0, `in`[i].length - 1)
			for ((key, value) in UNIVERSAL_VARIABLES) {
				out[i] = out[i]!!.replace("(?<!\\\\)!!$key".toRegex(), value.get().toString())
			}
			out[i] = out[i]!!
				.replace("(?<!\\\\)!!level".toRegex(), level.toString()).replace("(?<!\\\\)!!msg".toRegex(), `in`[i])
			out[i] = out[i]!!.replace("\\!!", "!!")
		}
		return colourise(out, level)
	}
	
	protected abstract fun colourise(out: Array<String?>, level: Logger.Level): Array<String?>
	private fun log(message: Any, error: Logger.Level): Boolean {
		return if (error.ordinal >= level.ordinal) print(getFormatted(message.toString(), error)) else false
	}
	
	private fun logln(message: Any, error: Logger.Level): Boolean {
		return if (error.ordinal >= level.ordinal) println(getFormatted(message.toString(), error)) else false
	}
	
	fun info(message: Any): Boolean {
		return log(message, Logger.Level.INFO)
	}
	
	fun infoln(message: Any): Boolean {
		return logln(message, Logger.Level.INFO)
	}
	
	fun debug(message: Any): Boolean {
		return log(message, Logger.Level.DEBUG)
	}
	
	fun debugln(message: Any): Boolean {
		return logln(message, Logger.Level.DEBUG)
	}
	
	fun warn(message: Any): Boolean {
		return log(message, Logger.Level.WARN)
	}
	
	fun warnln(message: Any): Boolean {
		return logln(message, Logger.Level.WARN)
	}
	
	fun error(message: Any): Boolean {
		return log(message, Logger.Level.ERROR)
	}
	
	fun errorln(message: Any): Boolean {
		return logln(message, Logger.Level.ERROR)
	}
	
	open fun destroy(): Boolean {
		return true
	}
}