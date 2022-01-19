package io.github.aemogie.timble.demo

import io.github.aemogie.timble.gl.useOpenGL
import io.github.aemogie.timble.gl.utils.legacyTriangle
import io.github.aemogie.timble.graphics.Window
import io.github.aemogie.timble.graphics.utils.fpsTitle
import io.github.aemogie.timble.utils.application.application
import io.github.aemogie.timble.utils.logging.info
import io.github.aemogie.timble.utils.logging.startLogger

private val window: Window = Window()

private fun scream() {
	window.subscribe<Window.InitEvent> { info("initializing your window!") }
	window.subscribe<Window.DestroyEvent> { info("sorry! we have to destroy your window...") }
}

fun main(vararg args: String) = application {
	startLogger()
	if (args.contains("--scream")) scream()
	window.useOpenGL()
	window.subscribe(::fpsTitle)
	window.subscribe(::legacyTriangle)
	window.run()
}