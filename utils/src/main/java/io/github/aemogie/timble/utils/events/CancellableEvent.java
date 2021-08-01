package io.github.aemogie.timble.utils.events;

import java.util.function.Function;

public class CancellableEvent extends Event {
	private boolean cancelled = false;
	@Override
	public boolean fire() {
		boolean success = true;
		for (Function<Event, Boolean> listener : LISTENERS) {
			if (cancelled) break;
			success &= listener.apply(this);
		}
		cancelled = false;
		return success;
	}
	
	public boolean cancel() {
		return cancelled = true;
	}
}
