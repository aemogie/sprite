package io.github.aemogie.timble.engine;

import io.github.aemogie.timble.engine.utils.Debug;
import io.github.aemogie.timble.gl.utils.Titles;
import io.github.aemogie.timble.gl.window.Window;
import io.github.aemogie.timble.utils.events.EventBus;

import java.util.Arrays;

import static io.github.aemogie.timble.gl.utils.Titles.FPS_TITLE;
import static io.github.aemogie.timble.utils.logging.LogManager.getLogger;

public class Timble {
	private static Window window;
	public static void main(String[] args) {
		try {
			window = Window.Builder.create().build();
			if (Arrays.asList(args).contains("--scream")) scream();
			Titles.setTitle(FPS_TITLE, window);
			EventBus.subscribeToEvent(Window.FrameLoopEvent.class, Debug::triangle);
			if (!window.run()) getLogger().errorln("oh no! we got an error while running the window :(");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void scream() {
		EventBus.subscribeToEvent(Window.InitEvent.class, e -> getLogger().debugln("initializing your window!"), false, window);
		EventBus.subscribeToEvent(Window.DestroyEvent.class, e -> getLogger().debugln("sorry! we have to destroy your window..."), false, window);
	}
}