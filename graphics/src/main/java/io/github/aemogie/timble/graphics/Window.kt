package io.github.aemogie.timble.graphics

import io.github.aemogie.timble.graphics.utils.*
import io.github.aemogie.timble.utils.Event
import io.github.aemogie.timble.utils.EventNode
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil.NULL

open class Window(
	private val api: GraphicsAPI,
	width: Int = 480,
	height: Int = 480,
	title: String = "timble. engine. (by aemogie.)",
) : EventNode() {
	private companion object {
		init {
			if (!glfwInit()) throw GLFWInitializationException()
		}
	}
	
	var windowPointer = NULL
		private set(value) {
			if (value == NULL) throw WindowCreationException()
			else field = value
		}
	
	var width = width; private set
	var height = height; private set
	var title: String = title
		set(value) = if (value != field) {
			glfwSetWindowTitle(windowPointer, value.also { field = it })
		} else Unit
	
	private fun init() {
		glfwSetErrorCallback { e, d -> throw GLFWException(e, d) }
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
		windowPointer = glfwCreateWindow(width, height, title, NULL, NULL)
		api.init(this)
		glfwShowWindow(windowPointer)
		fire(InitEvent())
	}
	
	private fun destroy() {
		fire(DestroyEvent())
		glfwFreeCallbacks(windowPointer)
		glfwDestroyWindow(windowPointer)
		glfwTerminate()
		glfwSetErrorCallback(null)?.free()
	}
	
	var elapsedTime = 0.0; private set
	
	fun run() {
		init()
		while (!glfwWindowShouldClose(windowPointer)) {
			val dt = glfwGetTime() - elapsedTime
			elapsedTime += dt
			//todo: call is OpenGL specific, so move it
			//and maybe even replace `api` w/ events entirely
			glfwSwapBuffers(windowPointer)
			glfwPollEvents()
			fire(FrameLoopEvent(dt))
		}
		destroy()
	}
	
	open inner class WindowEvent : Event() {
		val window = this@Window
	}
	inner class InitEvent : WindowEvent()
	inner class FrameLoopEvent(val deltaTime: Double) : WindowEvent()
	inner class DestroyEvent : WindowEvent()
}