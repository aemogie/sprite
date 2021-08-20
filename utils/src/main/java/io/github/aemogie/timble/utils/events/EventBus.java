package io.github.aemogie.timble.utils.events;

import com.google.common.collect.ArrayListMultimap;
import io.github.aemogie.timble.utils.events.Event.Listener;

import java.util.Map.Entry;
import java.util.stream.Stream;

public class EventBus {
	static final ArrayListMultimap<Class<Event>, Listener<? extends Event>> LISTENERS = ArrayListMultimap.create();
	
	public static <T extends Event> boolean fireEvent(T event) {
		return event.fire();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Event> Listener<T> subscribeToEvent(Class<T> clazz, Listener<T> listener) {
		LISTENERS.put((Class<Event>) clazz, listener);
		return listener;
	}
	
	public static Stream<Entry<Class<Event>, Listener<? extends Event>>> getApplicableListeners(Class<? extends Event> clazz) {
		return LISTENERS.entries().stream().filter(entry -> clazz.isAssignableFrom(entry.getKey()));
	}
}