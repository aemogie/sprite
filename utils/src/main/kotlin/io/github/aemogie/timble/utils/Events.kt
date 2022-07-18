package io.github.aemogie.timble.utils

import kotlin.reflect.KClass

open class Event // :smiley:

abstract class EventNode {
	//todo: jobs.
	private val listeners = object : HashMap<KClass<out Event>, ArrayList<(Event) -> Unit>>() {
		override operator fun get(key: KClass<out Event>) = super.get(key).let {
			it ?: ArrayList<(Event) -> Unit>().apply { super.put(key, this) }
		}
	}

	protected open fun <T : Event> fire(event: T) = listeners[event::class].forEach { it(event) }

	@Suppress("UNCHECKED_CAST")
	fun <T : Event> subscribe(clazz: KClass<T>, listener: (T) -> Unit) {
		listeners[clazz] += listener as (Event) -> Unit
	}

	inline fun <reified T: Event> subscribe(noinline listener: (T) -> Unit) = subscribe(T::class, listener)
}