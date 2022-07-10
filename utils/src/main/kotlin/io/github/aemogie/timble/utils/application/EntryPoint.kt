package io.github.aemogie.timble.utils.application

import kotlin.concurrent.thread
import kotlin.system.exitProcess

sealed class ApplicationScope {
	internal companion object : ApplicationScope()
}


private val tasks = mutableListOf<Job<*>>()

//TODO: see if i can change this to a more generalised approach
fun <T> runOnMain(block: () -> T) = Job(block).also { synchronized(tasks) { tasks += it } }

class Job<T> internal constructor(private val block: () -> T) {
	@Volatile
	private var result: T? = null
	@Volatile
	private var completed: Boolean = false

	internal operator fun invoke() {
		result = block()
		completed = true
	}

	fun await(): T? {
		while (!completed) Thread.yield()
		return result
	}

	fun awaitNotNull() = await()!!
}

@Volatile
private var stayAlive = true

fun application(run: ApplicationScope.() -> Unit) {
	if (Thread.currentThread().id != 1L) throw IllegalStateException(
		"application{} can only be called from the main thread."
	)
	thread(name = "Application") {
		try {
			@Suppress("RemoveRedundantQualifierName")
			ApplicationScope.run()
			EventBus.fire(ApplicationExitEvent)
		} catch (any: Exception) {
			any.printStackTrace()
			exitProcess(-1)
		}
	}
	EventBus.subscribe<ApplicationExitEvent> {
		stayAlive = false
	}
	while (stayAlive) if (tasks.isNotEmpty()) synchronized(tasks) {
		tasks.forEach { it() }
		tasks.clear()
	}
	synchronized(tasks) {
		tasks.forEach { it() }
		tasks.clear()
	}
}