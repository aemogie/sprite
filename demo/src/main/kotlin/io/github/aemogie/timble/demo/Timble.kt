package io.github.aemogie.timble.demo

import io.github.aemogie.timble.gl.OpenGL
import io.github.aemogie.timble.gl.utils.legacyTriangle
import io.github.aemogie.timble.graphics.Window
import io.github.aemogie.timble.graphics.Window.*
import io.github.aemogie.timble.graphics.utils.fpsTitle
import io.github.aemogie.timble.utils.logging.debug
import io.github.aemogie.timble.utils.logging.startLogger

private val window: Window = Window(OpenGL())

private fun scream() {
	window.subscribe(InitEvent::class) { debug("initializing your window!") }
	window.subscribe(DestroyEvent::class) { debug("sorry! we have to destroy your window...") }
}

fun main(vararg args: String) {
	try {
		startLogger()
		if (args.contains("--scream")) scream()
		window.subscribe(FrameLoopEvent::class, ::fpsTitle)
		window.subscribe(FrameLoopEvent::class) { legacyTriangle() }
		window.run()
	} catch (e: Exception) {
		e.printStackTrace()
	}
}
