package io.github.aemogie.timble.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class StreamUtils {
	private StreamUtils() {}
	
	public static <T> boolean runAllAndEval(Stream<? extends T> stream, Predicate<T> predicate) {
		AtomicBoolean result = new AtomicBoolean(true);
		stream.forEachOrdered(t -> {
			boolean test = predicate.test(t);
			if (result.get() && !test) result.set(false);
		});
		return result.get();
	}
	
	public static <T> boolean runAllAndEval(Iterable<T> iterable, Predicate<T> predicate) {
		boolean result = true;
		for (T t : iterable) {
			boolean test = predicate.test(t);
			if (result && !test) result = false;
		}
		return result;
	}
}
