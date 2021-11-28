//EventBus = singleton of EventNode
@file:JvmName("EventBus")
package io.github.aemogie.timble.utils.events

import kotlin.reflect.KClass
private val EVENT_BUS = EventNode()
fun <T : Event> fireEvent(event: T) = EVENT_BUS.fire(event)
fun <T : Event> subscribeToEvent(clazz: Class<T>, listener: (T) -> Boolean) = EVENT_BUS.subscribe(clazz, listener)
fun <T : Event> subscribeToEvent(clazz: KClass<T>, listener: (T) -> Boolean) = EVENT_BUS.subscribe(clazz, listener)