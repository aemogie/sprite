package io.github.aemogie.timble.gl;

import io.github.aemogie.timble.graphics.window.Window;
import io.github.aemogie.timble.utils.events.Event.Listener;
import io.github.aemogie.timble.utils.events.EventBus;

import org.lwjgl.opengl.*;

public class OpenGL {
	public static boolean init() {
		EventBus.subscribeToEvent(Window.InitEvent.class, new Listener<>(true) {
			protected boolean fire(Window.InitEvent event) {
				GL.createCapabilities();
				return true;
			}
		});
		return true;
	}
}
