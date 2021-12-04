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
	fun <T : Event> subscribe(clazz: KClass<T>, listener: (T) -> Unit) = listener.also {
		listeners[clazz] += it as (Event) -> Unit
	}
}

//singleton instance with widened visibility
@Suppress("unused") object EventBus : EventNode() {
	public override fun <T : Event> fire(event: T) = super.fire(event)
}