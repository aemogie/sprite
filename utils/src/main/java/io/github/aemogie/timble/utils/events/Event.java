package io.github.aemogie.timble.utils.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Event {
	protected final List<Function<Event, Boolean>> LISTENERS = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public<T extends Event> boolean subscribe(Function<T, Boolean> listener) {
		return LISTENERS.add((Function<Event, Boolean>) listener);
	}
	
	public boolean fire() {
		return LISTENERS.stream().allMatch(listener ->
				listener.apply(this)
		);
	}
}
