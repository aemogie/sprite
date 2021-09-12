package io.github.aemogie.timble.utils.events;

public class CancellableEvent extends Event {
	private boolean cancelled = false;
	
	@Override
	public boolean fire() {
		boolean success = true;
		for (var listener : EventBus.getApplicableListeners(getClass()).toList()) {
			if (cancelled) break;
			success = success && listener.getValue().fire(this);
		}
		cancelled = false;
		return success;
	}
	
	public boolean cancel() {
		cancelled = true;
		return true;
	}
}
