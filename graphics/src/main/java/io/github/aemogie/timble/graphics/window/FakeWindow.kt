package io.github.aemogie.timble.graphics.window

import kotlin.time.Duration

open class FakeWindow(title: String = "FakeWindow") : Window(title, 0, 0) {
	override var width: Int
		get() = error("FakeWindow doesn't have a window width.")
		set(_) = error("FakeWindow doesn't have a window width.")
	override var height: Int
		get() = error("FakeWindow doesn't have a window height.")
		set(_) = error("FakeWindow doesn't have a window width.")
	override var title: String
		get() = super.title
		set(_) = error("FakeWindow window title isn't modifiable.")

	override fun init() {}
	override fun loop(lastFrameDuration: Duration) {}
	override fun destroy() {}

	override fun run() {
		isRunning.compareAndSet(false, true)
		init()
		while (isRunning.get()) loop(Duration.ZERO) // duration shouldnt matter
		destroy()
		isRunning.set(false)
	}
}