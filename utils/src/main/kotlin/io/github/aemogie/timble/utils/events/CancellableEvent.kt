package io.github.aemogie.timble.utils.events

class CancellableEvent : Event() {
	private var cancelled = false
	override fun fire(listeners: ArrayList<Listener<Event?>>): Boolean {
		var success = true
		for (listener in listeners) {
			if (cancelled) break
			success = success && (listener.fire(this) || !listener.important)
		}
		cancelled = false
		return success
	}
	
	fun cancel(): Boolean {
		cancelled = true
		return true
	}
}