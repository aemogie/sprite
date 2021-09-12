package io.github.aemogie.timble.utils.events;

import io.github.aemogie.timble.utils.events.Event.Listener;

import java.util.ArrayList;
import java.util.HashMap;

public class EventBus {
	private EventBus() {}
	
	static final HashMap<Class<Event>, ArrayList<Listener<Event>>> LISTENERS = new HashMap<>();
	
	public static <T extends Event> boolean fireEvent(T event) {
		return event.initFire() && event.fire(LISTENERS.get(event.getClass()));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Event> Listener<T> subscribeToEvent(Class<T> clazz, Listener<T> listener) {
		LISTENERS.computeIfAbsent((Class<Event>) clazz, eventClass -> new ArrayList<>());
		LISTENERS.get(clazz).add((Listener<Event>) listener);
		return listener;
	}
}