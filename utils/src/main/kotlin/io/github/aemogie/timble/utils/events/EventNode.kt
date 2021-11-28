package io.github.aemogie.timble.utils.events

import kotlin.reflect.KClass

open class EventNode {
	private val listeners = object : HashMap<Class<Event>, ArrayList<(Event) -> Boolean>>() {
		override operator fun get(key: Class<Event>) = super.get(key).let {
			it ?: ArrayList<(Event) -> Boolean>().apply { super.put(key, this) }
		}
	}
	
	fun <T : Event> fire(event: T) = listeners[event.javaClass].all { it(event) }
	
	@Suppress("UNCHECKED_CAST")
	fun <T : Event> subscribe(clazz: Class<T>, listener: (T) -> Boolean) = listener.also {
		listeners[clazz as Class<Event>] += it as (Event) -> Boolean
	}
	
	//kotlin QOL
	fun <T : Event> subscribe(clazz: KClass<T>, listener: (T) -> Boolean) = subscribe(clazz.java, listener)
}