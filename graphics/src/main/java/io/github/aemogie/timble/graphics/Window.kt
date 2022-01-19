package io.github.aemogie.timble.graphics

import io.github.aemogie.timble.graphics.utils.GLFWException
import io.github.aemogie.timble.graphics.utils.GLFWInitializationException
import io.github.aemogie.timble.graphics.utils.WindowCreationException
import io.github.aemogie.timble.utils.Event
import io.github.aemogie.timble.utils.EventNode
import io.github.aemogie.timble.utils.application.ApplicationExitEvent
import io.github.aemogie.timble.utils.application.EventBus
import io.github.aemogie.timble.utils.application.runOnMain
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil.NULL
import kotlin.concurrent.thread

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

	val windowPointer by lazy {
		val ptr = runOnMain { glfwCreateWindow(width, height, title, NULL, NULL) }.awaitNotNull()
		if (ptr == NULL) throw WindowCreationException()
		return@lazy ptr
	}

	var width = width; private set
	var height = height; private set

	//it isnt guranteed that the title is set as soon as assigned
	var title: String = title
		set(value) = if (value != field) {
			runOnMain { glfwSetWindowTitle(windowPointer, value) }
			field = value
		} else Unit

	var elapsedTime = 0.0; private set

	fun run() = thread(name = "Window: $title") {
		fire(InitEvent())
		while (!glfwWindowShouldClose(windowPointer)) {
			runOnMain { glfwPollEvents() }.await()
			fire(FrameLoopEvent((glfwGetTime() - elapsedTime).also { elapsedTime += it }))
		}
		fire(DestroyEvent())
		runOnMain {
			glfwFreeCallbacks(windowPointer)
			glfwDestroyWindow(windowPointer)
		}.await()
	}

	open inner class WindowEvent : Event() {
		val window = this@Window
	}

	inner class InitEvent : WindowEvent()
	inner class FrameLoopEvent(val deltaTime: Double) : WindowEvent()
	inner class DestroyEvent : WindowEvent()
}