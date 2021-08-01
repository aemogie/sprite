package io.github.aemogie.timble.utils;

import io.github.aemogie.timble.utils.events.Event;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class StreamUtils {
	
	@NotNull
	public static <T> Stream<T> filterStreamByClass(Stream<T> stream, Class<? extends Event> eventClass) {
		return stream.filter(event -> eventClass.isAssignableFrom(event.getClass()));
	}
}
