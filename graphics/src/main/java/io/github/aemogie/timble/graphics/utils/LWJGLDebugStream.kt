package io.github.aemogie.timble.graphics.utils

import io.github.aemogie.timble.utils.logging.Level
import io.github.aemogie.timble.utils.logging.LoggerPrintStream
import io.github.aemogie.timble.utils.logging.debug
import java.io.PrintStream
import java.util.function.Supplier

@Suppress("unused")
class LWJGLDebugStream : LoggerPrintStream(Level.DEBUG), Supplier<PrintStream> {
	override fun get() = this
	override fun write(content: String) {
		if (content != "[LWJGL] ") debug(content)
	}
}