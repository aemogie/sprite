package io.github.aemogie.timble.graphics.window

import io.github.aemogie.timble.utils.Event

class WindowInitEvent internal constructor(val window: Window): Event
class WindowLoopEvent internal constructor(val window: Window, val deltaTime: Double): Event
class WindowDestroyEvent internal constructor(val window: Window): Event