package io.github.aemogie.timble.utils.events

open class Event {
	open fun initFire(): Boolean {
		return true
	}
	
	open fun fire(listeners: ArrayList<Listener<Event?>>): Boolean {
		return listeners.stream().allMatch { listener: Listener<Event?> -> listener.fire(this) || !listener.important }
	}
	
	abstract class Listener<T : Event?> protected constructor(val important: Boolean) {
		abstract fun fire(event: T): Boolean
	}
}