package io.github.aemogie.timble.utils.events;

import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CancellableEvent extends Event {
	private boolean cancelled = false;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean fire() {
		boolean success = true;
		for (Entry<Class<Event>, Listener<? extends Event>> listener : EventBus.getApplicableListeners(getClass()).collect(Collectors.toList())) {
			if (cancelled) break;
			success = success && listener.getValue().fire(this);
		}
		cancelled = false;
		return success;
	}
	
	public boolean cancel() {
		return cancelled = true;
	}
}
