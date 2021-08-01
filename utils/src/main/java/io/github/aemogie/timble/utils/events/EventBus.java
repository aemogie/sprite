package io.github.aemogie.timble.utils.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.github.aemogie.timble.utils.StreamUtils.filterStreamByClass;

public class EventBus {
	private static final List<Event> EVENTS = new ArrayList<>();
	
	public static boolean registerEvent(Event event) {
		if (filterStreamByClass(EVENTS.stream(), event.getClass()).findFirst().isEmpty()) return EVENTS.add(event);
		else return false;
	}
	
	public static boolean fireEvent(Class<? extends Event> eventClass) {
		return filterStreamByClass(EVENTS.stream(), eventClass).allMatch(Event::fire);
	}
	
	public static <T extends Event> boolean subscribeToEvent(Class<T> eventClass, Function<T, Boolean> listener) {
		return filterStreamByClass(EVENTS.stream(), eventClass).anyMatch(event ->
				event.subscribe(listener)
		);
	}
}