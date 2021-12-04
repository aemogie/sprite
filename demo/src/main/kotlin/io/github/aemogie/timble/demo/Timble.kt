package io.github.aemogie.timble.demo

import io.github.aemogie.timble.gl.OpenGL
import io.github.aemogie.timble.gl.utils.legacyTriangle
import io.github.aemogie.timble.graphics.Window
import io.github.aemogie.timble.graphics.Window.*
import io.github.aemogie.timble.graphics.utils.fpsTitle
import io.github.aemogie.timble.utils.logging.*

private val window: Window = Window(OpenGL())

private fun scream(): Boolean {
	window.subscribe(InitEvent::class) { debug("initializing your window!") }
	window.subscribe(DestroyEvent::class) { debug("sorry! we have to destroy your window...") }
	return true
}

fun main(vararg args: String) {
	try {
		start()
		if (args.contains("--scream")) scream()
		window.subscribe(FrameLoopEvent::class, ::fpsTitle)
		window.subscribe(FrameLoopEvent::class) { legacyTriangle() }
		if (!window.run()) error("oh no! we got an error while running the window :(")
	} catch (e: Exception) {
		e.printStackTrace()
	}
}
