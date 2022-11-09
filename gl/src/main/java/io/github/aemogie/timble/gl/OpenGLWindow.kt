package io.github.aemogie.timble.gl

import io.github.aemogie.timble.graphics.window.Window
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.opengl.GL
import kotlin.time.Duration

open class OpenGLWindow(
	title: String,
	width: Int = 480,
	height: Int = 480,
) : Window(title, width, height) {
	override fun init() {
		super.init()
		glfwMakeContextCurrent(ptr)
		GL.createCapabilities()
	}

	override fun loop(lastFrameDuration: Duration) {
		glfwSwapBuffers(ptr)
	}
}