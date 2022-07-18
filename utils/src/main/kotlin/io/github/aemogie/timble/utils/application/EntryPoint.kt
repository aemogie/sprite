package io.github.aemogie.timble.utils.application

import io.github.aemogie.timble.utils.console.ANSIModifier
import io.github.aemogie.timble.utils.console.ANSIModifier.CYAN_FG
import io.github.aemogie.timble.utils.console.ANSIModifier.WHITE_FG
import kotlin.system.exitProcess

sealed class ApplicationScope /*: QueuedThreadCreationScope()*/ {
	internal companion object : ApplicationScope()
}

fun application(run: ApplicationScope.() -> Unit) = try {
	if (Thread.currentThread().id == 1L) {
		ApplicationScope.run()
	} else throw IllegalStateException(
		ANSIModifier(CYAN_FG) +
		"application{}" +
		ANSIModifier(WHITE_FG) +
		" can only be called from the main thread."
	)
} catch (any: Exception) {
	any.printStackTrace()
	exitProcess(-1)
}