package io.github.aemogie.timble.graphics

import io.github.aemogie.timble.graphics.utils.GLFWException
import io.github.aemogie.timble.graphics.utils.GLFWInitializationException
import io.github.aemogie.timble.graphics.utils.WindowCreationException
import io.github.aemogie.timble.utils.Event
import io.github.aemogie.timble.utils.EventNode
import io.github.aemogie.timble.utils.application.ApplicationExitEvent
import io.github.aemogie.timble.utils.application.EventBus
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil.NULL

open class Window(
	width: Int = 480,
	height: Int = 480,
	title: String = "timble. engine. (by aemogie.)",
) : EventNode() {
	companion object {
		private val errorCallback = GLFWErrorCallback.create { e, d -> throw GLFWException(e, d) }

		init {
			if (!glfwInit()) throw GLFWInitializationException()
			errorCallback.set()
			EventBus.subscribe<ApplicationExitEvent> {
				errorCallback.free()
				glfwTerminate()
			}
		}
	}

	var windowPointer = NULL
		get() = if (field == NULL) glfwCreateWindow(width, height, title, NULL, NULL).also {
			if (it == NULL) throw WindowCreationException() else field = it
		} else field
		private set

	var width = width; private set
	var height = height; private set
	var title: String = title
		set(value) = if (value != field) {
			glfwSetWindowTitle(windowPointer, value.also { field = it })
		} else Unit

	var elapsedTime = 0.0; private set

	fun run() {
		fire(InitEvent())
		while (!glfwWindowShouldClose(windowPointer)) {
			glfwPollEvents()
			fire(FrameLoopEvent((glfwGetTime() - elapsedTime).also { elapsedTime += it }))
		}
		fire(DestroyEvent())
		glfwFreeCallbacks(windowPointer)
		glfwDestroyWindow(windowPointer)
	}

	open inner class WindowEvent : Event() {
		val window = this@Window
	}

	inner class InitEvent : WindowEvent()
	inner class FrameLoopEvent(val deltaTime: Double) : WindowEvent()
	inner class DestroyEvent : WindowEvent()
}