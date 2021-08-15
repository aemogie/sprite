package io.github.aemogie.timble.utils.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * ALL SUBCLASSES SHOULD HAVE A NO ARGUMENT CONSTRUCTOR!
 */
public class Event {
	protected final List<Function<Event, Boolean>> LISTENERS = new ArrayList<>();
	
	@SuppressWarnings({"unchecked"})
	public <T extends Event> boolean subscribe(Function<T, Boolean> listener, boolean important) {
		if (important) {
			return LISTENERS.add((Function<Event, Boolean>) listener);
		} else {
			return LISTENERS.add(event -> {
				((Function<Event, Boolean>) listener).apply(event);
				return true;
			});
		}
	}
	
	public boolean fire() {
		return LISTENERS.stream().allMatch(listener -> listener.apply(this));
	}
}
