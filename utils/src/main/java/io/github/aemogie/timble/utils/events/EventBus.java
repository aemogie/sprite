package io.github.aemogie.timble.utils.events;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class EventBus {
	private static final List<Event> EVENTS = new ArrayList<>();
	
	public static <T extends Event> boolean registerEvent(Class<T> event) {
		return registerEvent(event, null);
	}
	
	public static <T extends Event> boolean registerEvent(Class<T> event, @Nullable Object initObj) {
		if (getSubsetOfEvents(event).findFirst().isEmpty()) {
			try {
				if (initObj == null) return EVENTS.add(event.getConstructor().newInstance());
				else return EVENTS.add(event.getDeclaredConstructor(initObj.getClass()).newInstance(initObj));
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	private static <T extends Event> boolean registerEventAndSubscribe(Class<T> event, Function<T, Boolean> listener, boolean important, @Nullable Object initObj) {
		return registerEvent(event, initObj) && subscribeToEvent(event, listener, important, initObj);
	}
	
	public static boolean fireEvent(Class<? extends Event> event) {
		return getSubsetOfEvents(event).allMatch(Event::fire);
	}
	
	public static <T extends Event> boolean subscribeToEvent(Class<T> event, Function<T, Boolean> listener) {
		return subscribeToEvent(event, listener, true);
	}
	
	public static <T extends Event> boolean subscribeToEvent(Class<T> event, Function<T, Boolean> listener, boolean important) {
		return subscribeToEvent(event, listener, important, null);
	}
	
	public static <T extends Event> boolean subscribeToEvent(Class<T> event, Function<T, Boolean> listener, @Nullable Object initObj) {
		return subscribeToEvent(event, listener, true, initObj);
	}
	
	public static <T extends Event> boolean subscribeToEvent(Class<T> event, Function<T, Boolean> listener, boolean important, @Nullable Object initObj) {
		if (getSubsetOfEvents(event).anyMatch(e -> e.subscribe(listener, important))) {
			return true;
		} else {
			return registerEventAndSubscribe(event, listener, important, initObj);
		}
	}
	
	private static <T extends Event> Stream<Event> getSubsetOfEvents(Class<T> event) {
		return EVENTS.stream().filter(e -> event.isAssignableFrom(e.getClass()));
	}
}