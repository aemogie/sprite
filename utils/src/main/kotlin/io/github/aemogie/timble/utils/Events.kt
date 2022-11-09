package io.github.aemogie.timble.utils

import java.util.function.Consumer
import kotlin.reflect.KClass

interface Event // :smiley:

abstract class EventNode {
	private val listeners = HashMap<KClass<out Event>, ArrayList<(Event) -> Unit>>()

	protected open fun <T : Event> fire(event: T) = listeners.getOrPut(event::class, ::arrayListOf).forEach { it(event) }

	@Suppress("UNCHECKED_CAST")
	fun <T : Event> subscribe(clazz: KClass<T>, listener: (T) -> Unit) {
	listeners[clazz]!! += listener as (Event) -> Unit
}

	inline fun <reified T: Event> subscribe(noinline listener: (T) -> Unit) = subscribe(T::class, listener)
}