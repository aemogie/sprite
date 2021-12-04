@file:JvmName("Logger")
@file:Suppress("unused") //for unused log methods.
package io.github.aemogie.timble.utils.logging

import com.google.gson.JsonStreamParser
import io.github.aemogie.timble.utils.getResourceReader
import io.github.aemogie.timble.utils.logging.Level.*
import java.io.PrintStream
import java.lang.Thread.currentThread
import java.time.Instant.now
import java.util.*
import kotlin.concurrent.thread

private val outputs = ArrayList<LoggerOutput>()
internal val records = ArrayDeque<LogRecord>()

//[0] = Thread#getStackTrace
//[1] = Logger#(*)
//[2] = Caller#method

fun info(msg: Any?) = synchronized(records) {
	if (msg != null) records.add(LogRecord(INFO, currentThread(), currentThread().stackTrace[2], now(), msg))
}

fun debug(msg: Any?) = synchronized(records) {
	if (msg != null) records.add(LogRecord(DEBUG, currentThread(), currentThread().stackTrace[2], now(), msg))
}

fun warn(msg: Any?) = synchronized(records) {
	if (msg != null) records.add(LogRecord(WARN, currentThread(), currentThread().stackTrace[2], now(), msg))
}

fun error(msg: Any?) = synchronized(records) {
	if (msg != null) records.add(LogRecord(ERROR, currentThread(), currentThread().stackTrace[2], now(), msg))
}

fun fatal(msg: Any?) = synchronized(records) {
	if (msg != null) records.add(LogRecord(FATAL, currentThread(), currentThread().stackTrace[2], now(), msg))
}

private val SYS_OUT: PrintStream = System.out
private val SYS_ERR: PrintStream = System.err

fun replaceDefault() {
	System.setOut(LoggerPrintStream(INFO))
	System.setErr(LoggerPrintStream(ERROR))
}

fun restoreDefault() {
	System.setOut(SYS_OUT)
	System.setErr(SYS_ERR)
}

const val CONFIG_PATH: String = "/META-INF/timble-logger.json"

fun start() {
	//init
	outputs += getResourceReader(CONFIG_PATH, ::JsonStreamParser).next().asJsonArray.map {
		LoggerOutput.of(it.asJsonObject)
	}
	replaceDefault()
	
	//run
	thread(start = true, isDaemon = true, name = "Logger") {
		//todo: replace w/ blocking queue (`LinkedBlockingQueue`)
		while (true) synchronized(records) {
			records.poll()?.also { outputs.forEach { out -> out.log(it) } }
		}
	}
	
	//cleanup
	Runtime.getRuntime().addShutdownHook(thread(start = false, name = "Logger.ShutDown") {
		synchronized(records) {
			records.forEach { outputs.forEach { out -> out.log(it) } }
		}
		outputs.forEach(LoggerOutput::destroy)
		restoreDefault()
	})
}

enum class Level {
	ALL, INFO, DEBUG, WARN, ERROR, FATAL;
}