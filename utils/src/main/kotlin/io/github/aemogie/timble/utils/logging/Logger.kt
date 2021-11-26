package io.github.aemogie.timble.utils.logging

import com.google.gson.JsonObject
import com.google.gson.JsonStreamParser
import io.github.aemogie.timble.utils.ResourceLoader
import io.github.aemogie.timble.utils.console.STD_ERR
import io.github.aemogie.timble.utils.logging.Logger.Level.*
import java.io.PrintStream
import java.lang.Thread.currentThread
import java.time.Instant.now
import java.util.*
import kotlin.concurrent.thread
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaType
import kotlin.system.exitProcess

@Suppress("unused") //for the log methods `info` and `warn`.
object Logger {
	private val outputs: List<LoggerOutput>
	private val records: Queue<LogRecord> = ArrayDeque()
	
	//[0] = Thread#getStackTrace
	//[1] = Logger#(*)
	//[2] = Caller#method
	
	@JvmStatic fun info(vararg msg: Any?): Boolean = synchronized(records) {
		records.add(LogRecord(INFO, currentThread(), currentThread().stackTrace[2], now(), msg.toList()))
	}
	
	@JvmStatic fun debug(vararg msg: Any?): Boolean = synchronized(records) {
		records.add(LogRecord(DEBUG, currentThread(), currentThread().stackTrace[2], now(), msg.toList()))
	}
	
	@JvmStatic fun warn(vararg msg: Any?): Boolean = synchronized(records) {
		records.add(LogRecord(WARN, currentThread(), currentThread().stackTrace[2], now(), msg.toList()))
	}
	
	@JvmStatic fun error(vararg msg: Any?): Boolean = synchronized(records) {
		records.add(LogRecord(ERROR, currentThread(), currentThread().stackTrace[2], now(), msg.toList()))
	}
	
	@JvmStatic fun fatal(vararg msg: Any?): Boolean = synchronized(records) {
		records.add(LogRecord(FATAL, currentThread(), currentThread().stackTrace[2], now(), msg.toList()))
	}
	
	//beware: reflection
	private fun createOutput(config: JsonObject): LoggerOutput {
		return config["class"]?.asString.let {
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
	
	private val SYS_OUT: PrintStream = System.out
	private val SYS_ERR: PrintStream = System.err
	
	@JvmStatic fun replaceDefault(): Boolean {
		System.setOut(LoggerPrintStream { thread, caller, instant, content ->
			synchronized(records) { records.add(LogRecord(INFO, thread, caller, instant, listOf(content))) }
		})
		System.setErr(LoggerPrintStream { thread, caller, instant, content ->
			synchronized(records) { records.add(LogRecord(ERROR, thread, caller, instant, listOf(content))) }
		})
		return true
	}
	
	@JvmStatic fun restoreDefault(): Boolean {
		System.setOut(SYS_OUT)
		System.setErr(SYS_ERR)
		return true
	}
	
	const val CONFIG_PATH: String = "/META-INF/timble-logger.json"
	
	init {
		//init
		outputs = ResourceLoader.getResourceReader(CONFIG_PATH) { reader ->
			JsonStreamParser(reader).next().asJsonArray.map { createOutput(it.asJsonObject) }
		}
		
		//run
		thread(isDaemon = true, name = "Logger") {
			while (true) synchronized(records) {
				//NOTE: locking even when the value is empty might be bad. also might have to lock `outputs`
				records.poll()?.also { outputs.forEach { out -> out.log(it) } }
			}
		}
		
		//cleanup
		Runtime.getRuntime().addShutdownHook(thread(start = false, name = "Logger.ShutDown") {
			synchronized(records) {
				records.forEach { outputs.forEach { out -> out.log(it) } }
			}
			outputs.forEach(LoggerOutput::destroy)
		})
	}
	
	enum class Level {
		ALL, INFO, DEBUG, WARN, ERROR, FATAL;
	}
}