package io.github.aemogie.timble.demo

import io.github.aemogie.timble.gl.useOpenGL
import io.github.aemogie.timble.gl.utils.legacyTriangle
import io.github.aemogie.timble.graphics.Window
import io.github.aemogie.timble.graphics.Window.DestroyEvent
import io.github.aemogie.timble.graphics.Window.InitEvent
import io.github.aemogie.timble.graphics.utils.fpsTitle
import io.github.aemogie.timble.utils.application.application
import io.github.aemogie.timble.utils.logging.debug
import io.github.aemogie.timble.utils.logging.startLogger

private val window: Window = Window()

private fun scream() {
	window.subscribe<InitEvent> { debug("initializing your window!") }
	window.subscribe<DestroyEvent> { debug("sorry! we have to destroy your window...") }
}

//MAYBE: use custom entry point
fun main(vararg args: String) = application {
	startLogger()
	if (args.contains("--scream")) scream()
	window.useOpenGL()
	window.subscribe(::fpsTitle)
	window.subscribe(::legacyTriangle)
	window.run()
}