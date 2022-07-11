package io.github.aemogie.timble.utils

import java.util.concurrent.FutureTask
import kotlin.concurrent.thread


@Volatile private var stayAlive = true
private val tasks = ArrayDeque<FutureTask<*>>()

fun application(run: () -> Unit) {
	if (Thread.currentThread().id != 1L) error("application{} can only be called from the main thread.")

	thread(name = "Application") {
		run()
		EventBus.fire(ApplicationExitEvent)
		stayAlive = false
	}

	while (stayAlive) synchronized(tasks) {
		if (tasks.isNotEmpty()) repeat(tasks.size) {
			tasks.removeFirst().run()
		}
	}

	synchronized(tasks) {
		tasks.forEach { it.run() }
		tasks.clear()
	}
}

fun <T> runOnMain(block: () -> T) = FutureTask(block).also {
	synchronized(tasks) { tasks += it }
}

object ApplicationExitEvent : Event()

object EventBus : EventNode() {
	//increase visibility from protected
	public override fun <T : Event> fire(event: T) = super.fire(event)
}