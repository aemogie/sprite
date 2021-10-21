package io.github.aemogie.timble.utils.events

object EventBus {
	val LISTENERS = HashMap<Class<Event>, ArrayList<Event.Listener<Event?>>>()
	@JvmStatic fun <T : Event?> fireEvent(event: T): Boolean {
		return event!!.initFire() && event.fire(LISTENERS[event.javaClass]!!)
	}
	
	@Suppress("UNCHECKED_CAST")
	@JvmStatic fun <T : Event?> subscribeToEvent(clazz: Class<T>, listener: Event.Listener<T?>): Event.Listener<T?> {
		LISTENERS.computeIfAbsent(clazz as Class<Event>) { ArrayList() }
		LISTENERS[clazz]!!.add(listener as Event.Listener<Event?>)
		return listener
	}
}