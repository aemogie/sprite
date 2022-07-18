package io.github.aemogie.timble.gl

import io.github.aemogie.timble.graphics.Window
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.opengl.GL

fun Window.useOpenGL() {
	subscribe<Window.InitEvent> {
		glfwMakeContextCurrent(windowPointer)
		GL.createCapabilities()
	}
	subscribe<Window.FrameLoopEvent> {
		glfwSwapBuffers(windowPointer)
	}
}