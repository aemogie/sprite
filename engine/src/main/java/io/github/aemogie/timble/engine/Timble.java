package io.github.aemogie.timble.engine;

import io.github.aemogie.timble.gl.OpenGL;
import io.github.aemogie.timble.gl.utils.Debug;
import io.github.aemogie.timble.graphics.window.Window;
import io.github.aemogie.timble.utils.events.Event.Listener;
import io.github.aemogie.timble.utils.events.EventBus;

import java.util.Arrays;

import static io.github.aemogie.timble.graphics.utils.Titles.FPS_TITLE;
import static io.github.aemogie.timble.utils.logging.LogManager.getLogger;

public class Timble {
	public static void main(String[] args) {
		try {
			Window window = Window.Builder.create().build();
			OpenGL.init();
			if (Arrays.asList(args).contains("--scream")) scream();
			EventBus.subscribeToEvent(Window.FrameLoopEvent.class, FPS_TITLE);
			EventBus.subscribeToEvent(Window.FrameLoopEvent.class, Debug.TRIANGLE);
			if (!window.run()) getLogger().errorln("oh no! we got an error while running the window :(");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean scream() {
		EventBus.subscribeToEvent(Window.InitEvent.class, new Listener<>(false) {
			@Override
			protected boolean fire(Window.InitEvent event) {
				return getLogger().debugln("initializing your window!");
			}
		});
		EventBus.subscribeToEvent(Window.DestroyEvent.class, new Listener<>(false) {
			@Override
			protected boolean fire(Window.DestroyEvent event) {
				return getLogger().debugln("sorry! we have to destroy your window...");
			}
		});
		return true;
	}
}