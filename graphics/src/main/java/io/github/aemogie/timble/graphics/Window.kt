/*
package io.github.aemogie.timble.graphics

import io.github.aemogie.timble.graphics.window.WindowCreationException
import io.github.aemogie.timble.utils.*
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.APIUtil
import org.lwjgl.system.MemoryUtil.NULL
import kotlin.concurrent.thread

open class Window(
	var width: Int = 480,
	var height: Int = 480,
	title: String = "timble. engine. (by aemogie.)",
) : EventNode() {
	private companion object {
		val ERROR_CODES: MutableMap<Int, String> = APIUtil.apiClassTokens(
			//errors enums are in the 0x1XXXX range.
			{ _, value -> value in 0x10000..0x1FFFF }, null, org.lwjgl.glfw.GLFW::class.java
		)

		init {
			val errorCallback = GLFWErrorCallback.create { e, d ->
				error("${ERROR_CODES[e]} - ${GLFWErrorCallback.getDescription(d)}")
			}
			val success = runOnMain { glfwInit() }.get()
			if (!success) error("") */
/*throw InitializationException()*//*

			errorCallback.set()
			EventBus.subscribe<ApplicationExitEvent> {
				errorCallback.free()
				runOnMain { glfwTerminate() }
			}
		}
	}

	val windowPointer: Long by lazy {
		val ptr = runOnMain { glfwCreateWindow(width, height, title, NULL, NULL) }.get()
		if (ptr == NULL) throw WindowCreationException()
		return@lazy ptr
	}

	//it isnt guranteed that the title is set as soon as assigned
	var title: String = title
		set(value) {
			runOnMain { glfwSetWindowTitle(windowPointer, value) }
			field = value
		}

	var elapsedTime = 0.0; private set

	fun run() = thread(name = "Window: $title") {
//		fire(InitEvent())
		while (!glfwWindowShouldClose(windowPointer)) {
			runOnMain { glfwPollEvents() }
//			fire(FrameLoopEvent(
			elapsedTime += (glfwGetTime() - elapsedTime)
//			))
		}
//		fire(DestroyEvent())
		runOnMain {
			glfwFreeCallbacks(windowPointer)
			glfwDestroyWindow(windowPointer)
		}
	}
}
*/
