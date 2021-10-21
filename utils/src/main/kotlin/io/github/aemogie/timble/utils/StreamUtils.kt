package io.github.aemogie.timble.utils

import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Predicate
import java.util.stream.Stream

object StreamUtils {
	@JvmStatic fun <T> runAllAndEval(stream: Stream<out T>, predicate: Predicate<T>): Boolean {
		val result = AtomicBoolean(true)
		stream.forEachOrdered { t: T ->
			val test = predicate.test(t)
			if (result.get() && !test) result.set(false)
		}
		return result.get()
	}
	
	@JvmStatic fun <T> runAllAndEval(iterable: Iterable<T>, predicate: Predicate<T>): Boolean {
		var result = true
		for (t in iterable) {
			val test = predicate.test(t)
			if (result && !test) result = false
		}
		return result
	}
}