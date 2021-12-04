package io.github.aemogie.timble.graphics.utils

import io.github.aemogie.timble.graphics.Window

private const val TITLE_REFRESH_RATE = 0.175
private var fpsProgress = 0.0
fun fpsTitle(event: Window.FrameLoopEvent) {
	fpsProgress += event.deltaTime
	if (fpsProgress > TITLE_REFRESH_RATE) {
		fpsProgress = 0.0
		event.window.title = (1 / event.deltaTime).toInt().toString()
	}
}