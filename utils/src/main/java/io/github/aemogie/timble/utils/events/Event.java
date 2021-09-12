package io.github.aemogie.timble.utils.events;

import java.util.ArrayList;

public class Event {
	
	protected boolean initFire() {
		return true;
	}
	
	protected boolean fire(ArrayList<Listener<Event>> listeners) {
		return listeners.stream().allMatch(listener -> listener.fire(this) || !listener.important);
	}
	
	public abstract static class Listener<T extends Event> {
		public final boolean important;
		
		protected Listener(boolean important) {this.important = important;}
		
		protected abstract boolean fire(T event);
	}
}
