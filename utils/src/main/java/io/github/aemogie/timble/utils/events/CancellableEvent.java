package io.github.aemogie.timble.utils.events;

import java.util.ArrayList;

public class CancellableEvent extends Event {
	private boolean cancelled = false;
	
	@Override
	protected boolean fire(ArrayList<Listener<Event>> listeners) {
		boolean success = true;
		for (Listener<Event> listener : listeners) {
			if (cancelled) break;
			success = success && (listener.fire(this) || !listener.important);
		}
		cancelled = false;
		return success;
	}
	
	public boolean cancel() {
		cancelled = true;
		return true;
	}
}
