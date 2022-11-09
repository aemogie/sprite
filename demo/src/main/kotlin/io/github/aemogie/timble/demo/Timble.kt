package io.github.aemogie.timble.demo

import io.github.aemogie.timble.gl.OpenGLWindow
import io.github.aemogie.timble.gl.utils.legacyTriangle
import io.github.aemogie.timble.graphics.window.FakeWindow
import io.github.aemogie.timble.graphics.window.Window
import org.lwjgl.glfw.GLFW.*
import kotlin.time.Duration

fun main() {
	class DemoWindow : OpenGLWindow(title = "Window") {
		override fun init() {
			super.init()
			glfwSetKeyCallback(ptr) { _, key, _, action, _ ->
				if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE) addChild(DemoWindow())
			}
		}

		override fun loop(lastFrameDuration: Duration) {
			legacyTriangle()
			super.loop(lastFrameDuration)
		}
	}

	val window = object : FakeWindow() {
		override fun init() {
			super.init()
			addChild(DemoWindow())
			addChild(DemoWindow())
			addChild(DemoWindow())
		}
	}
	Window.Main(window).run()
}