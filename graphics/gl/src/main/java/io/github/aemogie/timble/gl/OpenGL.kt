package io.github.aemogie.timble.gl

import io.github.aemogie.timble.graphics.GraphicsAPI
import io.github.aemogie.timble.graphics.Window
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.opengl.GL

class OpenGL : GraphicsAPI() {
	override fun init(window: Window) {
		glfwMakeContextCurrent(window.windowPointer)
		GL.createCapabilities()
	}
}