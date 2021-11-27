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

fun info(msg: Any?): Boolean = synchronized(records) {
	if (msg != null) records.add(LogRecord(INFO, currentThread(), currentThread().stackTrace[2], now(), msg))
	true
}

fun debug(msg: Any?): Boolean = synchronized(records) {
	if (msg != null) records.add(LogRecord(DEBUG, currentThread(), currentThread().stackTrace[2], now(), msg))
	true
}

fun warn(msg: Any?): Boolean = synchronized(records) {
	if (msg != null) records.add(LogRecord(WARN, currentThread(), currentThread().stackTrace[2], now(), msg))
	true
}

fun error(msg: Any?): Boolean = synchronized(records) {
	if (msg != null) records.add(LogRecord(ERROR, currentThread(), currentThread().stackTrace[2], now(), msg))
	true
}

fun fatal(msg: Any?): Boolean = synchronized(records) {
	if (msg != null) records.add(LogRecord(FATAL, currentThread(), currentThread().stackTrace[2], now(), msg))
	true
}

private val SYS_OUT: PrintStream = System.out
private val SYS_ERR: PrintStream = System.err

fun replaceDefault(): Boolean {
	System.setOut(LoggerPrintStream(INFO))
	System.setErr(LoggerPrintStream(ERROR))
	return true
}

fun restoreDefault(): Boolean {
	System.setOut(SYS_OUT)
	System.setErr(SYS_ERR)
	return true
}

const val CONFIG_PATH: String = "/META-INF/timble-logger.json"

fun start(): Boolean {
	//init
	outputs += getResourceReader(CONFIG_PATH, ::JsonStreamParser).next().asJsonArray.map {
		LoggerOutput.of(it.asJsonObject)
	}
	replaceDefault()
	
	//run
	thread(start = true, isDaemon = true, name = "Logger") {
		//NOTE: locking even when the value is empty might be bad. also might have to lock `outputs`
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
	return true
}

enum class Level {
	ALL, INFO, DEBUG, WARN, ERROR, FATAL;
}