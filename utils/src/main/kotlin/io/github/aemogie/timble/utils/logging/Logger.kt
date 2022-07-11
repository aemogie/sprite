@file:Suppress("unused") //for unused log methods.
package io.github.aemogie.timble.utils.logging

import io.github.aemogie.timble.utils.ApplicationExitEvent
import io.github.aemogie.timble.utils.EventBus
import io.github.aemogie.timble.utils.ResourceLoader
import io.github.aemogie.timble.utils.logging.Level.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import java.io.PrintStream
import java.time.Instant
import java.util.*
import kotlin.concurrent.thread

private val outputs = Collections.synchronizedList(arrayListOf<LoggerOutput>())
private val records = ArrayDeque<LogRecord>()

// psst! try using a lambda...
fun info(content: Any?) = log(content, INFO)
fun debug(content: Any?) = log(content, DEBUG)
fun warn(content: Any?) = log(content, WARN)
fun error(content: Any?) = log(content, ERROR)
fun fatal(content: Any?) = log(content, FATAL)

internal fun log(
	content: Any?,
	level: Level,
	// 0 - Thread.getStackTrace
	// 1 - current method
	// 2 - wrapper log methods
	// 2 - Caller.method
	thread: Thread = Thread.currentThread(),
	caller: StackTraceElement = thread.stackTrace[3],
	instant: Instant = Instant.now()
) {
	if (content != null) LogRecord(level, thread, caller, instant, content).also {
		synchronized(records) { records += it }
	}
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

@Volatile private var stayAlive = true

fun startLogger(replace: Boolean = true) {
	outputs += ResourceLoader.text(CONFIG_PATH) {
		Json.parseToJsonElement(it)
	}.jsonArray.map {
		LoggerOutput(it)
	}.toList()

	if (replace) replaceStandardOut()

	EventBus.subscribe<ApplicationExitEvent> {
		stayAlive = false
		synchronized(records) {
			records.forEach { outputs.forEach { out -> out.log(it) } }
		}
		outputs.forEach(LoggerOutput::destroy)
		if (replace) restoreStandardOut()
	}

	thread(start = true, name = "Logger") {
		while (stayAlive) if (records.isNotEmpty()) synchronized(records) {
			records.forEach { outputs.forEach { out -> out.log(it) } }
			records.clear()
		}
	}
}

enum class Level { ALL, INFO, DEBUG, WARN, ERROR, FATAL }