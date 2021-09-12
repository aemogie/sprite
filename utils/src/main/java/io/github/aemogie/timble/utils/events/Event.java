package io.github.aemogie.timble.utils.events;

import java.util.Map.Entry;
import java.util.stream.Stream;

public class Event {
	
	protected boolean fire() {
		return getApplicableListeners().allMatch(listener -> listener.getValue().fire(this) || !listener.getValue().important);
	}
	
	protected Stream<Entry<Class<Event>, Listener<Event>>> getApplicableListeners() {
		return EventBus.getApplicableListeners(getClass());
	}
	
	public abstract static class Listener<T extends Event> {
		public final boolean important;
		
		protected Listener(boolean important) {this.important = important;}
		
		protected abstract boolean onFire(T event);
		
		@SuppressWarnings({"unchecked"})
		protected boolean fire(Event event) {
			try {
				return onFire((T) event);
			} catch (ClassCastException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}
