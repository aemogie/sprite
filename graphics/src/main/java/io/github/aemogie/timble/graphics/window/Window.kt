package io.github.aemogie.timble.graphics.window

import io.github.aemogie.timble.utils.EventNode
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil.NULL
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.FutureTask
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

open class Window(
	title: String,
	open var width: Int = 480,
	open var height: Int = 480,
): EventNode() {

	open var title: String = title
		set(value) {
			runOnMain { glfwSetWindowTitle(ptr, value) }
			field = value
		}

	protected var ptr by Delegates.notNull<Long>()
		private set

	protected open fun init() {
		ptr = runOnMain { glfwCreateWindow(width, height, title, NULL, NULL) }.get() ?: throw WindowCreationException()
		title = title
	}

	protected open fun loop(lastFrameDuration: Duration) {}

	protected open fun destroy() {
		children.forEach { glfwSetWindowShouldClose(it.ptr, true) }
		runOnMain {
			glfwFreeCallbacks(ptr)
			glfwDestroyWindow(ptr)
		}
		parent?.apply { children.remove(this@Window) }
	}

	protected val isRunning = AtomicBoolean(false)

	@OptIn(ExperimentalTime::class)
	protected open fun run() {
		isRunning.set(true)
		init()
		var lastFrameDuration: Duration = Duration.ZERO
		while (!glfwWindowShouldClose(ptr)) {
			lastFrameDuration = measureTime { loop(lastFrameDuration) }
		}
		destroy()
		isRunning.set(false)
	}

	private val children = arrayListOf<Window>()
	protected var parent: Window? = null; private set

	fun addChild(child: Window) {
		children.add(child)
		child.parent = this
		child.title = "$title:${child.title}"
		thread(name = child.title, block = child::run)
	}

	fun printChildren() {
		children.forEach {
			println(it.title)
			it.printChildren()
		}
	}

	companion object { private val QUEUE = ArrayBlockingQueue<FutureTask<*>>(16) }
	private fun <T> runOnMain(block: () -> T) = FutureTask(block).also(QUEUE::put)

	class Main(private val win: Window) {
		fun run() {
			check(Thread.currentThread().id == 1L) { "Method `Main.run()` can only be called from the main thread!" }

			val err = GLFWErrorCallback.createPrint().set()
			glfwInit()
			thread(name = "Window:${win.title}", block = win::run)
			win.isRunning.set(true)
			while (win.isRunning.get()) {
				glfwPollEvents()
				QUEUE.poll()?.run()
			}
			glfwTerminate()
			err.free()
		}
	}
}