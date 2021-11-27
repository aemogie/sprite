package io.github.aemogie.timble.utils.logging

import java.time.Instant

data class LogRecord(
	val level: Level,
	val thread: Thread,
	val caller: StackTraceElement,
	val instant: Instant,
	val content: Any?,
)