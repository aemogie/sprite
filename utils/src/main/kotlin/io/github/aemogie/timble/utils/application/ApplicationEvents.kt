package io.github.aemogie.timble.utils.application

import io.github.aemogie.timble.utils.Event
import io.github.aemogie.timble.utils.EventNode

object ApplicationExitEvent : Event()

@Suppress("unused")
object EventBus : EventNode() {
	public override fun <T : Event> fire(event: T) = super.fire(event)
}