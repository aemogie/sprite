package io.github.aemogie.timble.utils

import kotlin.reflect.KClass

open class Event // :smiley:

open class EventNode {
	private val listeners = object : HashMap<KClass<out Event>, ArrayList<(Event) -> Boolean>>() {
		override operator fun get(key: KClass<out Event>) = super.get(key).let {
			it ?: ArrayList<(Event) -> Boolean>().apply { super.put(key, this) }
		}
	}
	
	fun <T : Event> fire(event: T) = listeners[event::class].all { it(event) }
	@Suppress("UNCHECKED_CAST")
	fun <T : Event> subscribe(clazz: KClass<T>, listener: (T) -> Boolean) = listener.also {
		listeners[clazz] += it as (Event) -> Boolean
	}
}

@Suppress("unused") object EventBus : EventNode()