package io.github.aemogie.timble.demo

import io.github.aemogie.timble.gl.useOpenGL
import io.github.aemogie.timble.gl.utils.legacyTriangle
import io.github.aemogie.timble.graphics.Window
import io.github.aemogie.timble.graphics.Window.*
import io.github.aemogie.timble.graphics.utils.fpsTitle
import io.github.aemogie.timble.utils.logging.debug
import io.github.aemogie.timble.utils.logging.startLogger

private val window: Window = Window()

private fun scream() {
	window.subscribe(InitEvent::class) { debug("initializing your window!") }
	window.subscribe(DestroyEvent::class) { debug("sorry! we have to destroy your window...") }
}

//MAYBE: use custom entry point
fun main(vararg args: String) {
	try {
		startLogger()
		if (args.contains("--scream")) scream()
		//MAYBE: convert all init() destroy() pairs to scopes.
		Window.init()
		window.useOpenGL()
		window.subscribe(FrameLoopEvent::class, ::fpsTitle)
		window.subscribe(FrameLoopEvent::class) { legacyTriangle() }
		window.run()
		Window.destroy()
	} catch (e: Exception) {
		e.printStackTrace()
	}
}
