@file:JvmName("Logger") @file:Suppress("unused") //for unused log methods.
package io.github.aemogie.timble.utils.logging

import io.github.aemogie.timble.utils.getResourceText
import io.github.aemogie.timble.utils.logging.Level.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.io.PrintStream
import java.lang.Thread.currentThread
import java.time.Instant.now
import java.util.*
import kotlin.concurrent.thread

private val outputs = arrayListOf<LoggerOutput>()
internal val records = ArrayDeque<LogRecord>()

//[0] = Thread#getStackTrace
//[1] = Logger#(*)
//[2] = Caller#method

fun info(msg: Any?) = synchronized(records) {
	if (msg != null) records += LogRecord(INFO, currentThread(), currentThread().stackTrace[2], now(), msg)
}

fun debug(msg: Any?) = synchronized(records) {
	if (msg != null) records += LogRecord(DEBUG, currentThread(), currentThread().stackTrace[2], now(), msg)
}

fun warn(msg: Any?) = synchronized(records) {
	if (msg != null) records += LogRecord(WARN, currentThread(), currentThread().stackTrace[2], now(), msg)
}

fun error(msg: Any?) = synchronized(records) {
	if (msg != null) records += LogRecord(ERROR, currentThread(), currentThread().stackTrace[2], now(), msg)
}

fun fatal(msg: Any?) = synchronized(records) {
	if (msg != null) records += LogRecord(FATAL, currentThread(), currentThread().stackTrace[2], now(), msg)
}

private val SYS_OUT: PrintStream = System.out
private val SYS_ERR: PrintStream = System.err

fun replaceStandardOut() {
	System.setOut(LoggerPrintStream(INFO))
	System.setErr(LoggerPrintStream(ERROR))
}

fun restoreStandardOut() {
	System.setOut(SYS_OUT)
	System.setErr(SYS_ERR)
}

const val CONFIG_PATH: String = "/META-INF/timble-logger.json"

fun startLogger(replace: Boolean = true): Thread {
	var parent = currentThread()
	return thread(start = true, name = "Logger") {
		if (parent.isDaemon) parent = Thread.getAllStackTraces().keys.single { it.id == 1L }

		outputs += getResourceText(CONFIG_PATH, Json::parseToJsonElement).jsonArray.mapNotNull {
			LoggerOutput.of(it.jsonObject)
		}

		if (replace) replaceStandardOut()

		//blocking queue doesn't work because it blocks inside while loop
		//even when logger is over, making it not possible to exit out of while loop.
		while (!currentThread().isInterrupted && parent.isAlive) if (records.isNotEmpty()) {
			synchronized(records) { records.poll()?.also { outputs.forEach { out -> out.log(it) } } }
		}

		records.forEach { for (out in outputs) out.log(it) }
		outputs.forEach(LoggerOutput::destroy)
		if (replace) restoreStandardOut()
	}
}

enum class Level { ALL, INFO, DEBUG, WARN, ERROR, FATAL }