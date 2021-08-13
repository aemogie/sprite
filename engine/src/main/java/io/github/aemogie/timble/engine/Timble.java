package io.github.aemogie.timble.engine;

import io.github.aemogie.timble.engine.utils.Debug;
import io.github.aemogie.timble.gl.utils.Titles;
import io.github.aemogie.timble.gl.window.Window;
import io.github.aemogie.timble.utils.events.EventBus;

import static io.github.aemogie.timble.gl.utils.Titles.FPS_TITLE;
import static io.github.aemogie.timble.utils.logging.LogManager.getLogger;

public class Timble {
	public static void main(String[] args) {
		try {
			getLogger().debugln("starting up...!");
			Window window = Window.Builder.create().build();
			Titles.setTitle(FPS_TITLE);
			EventBus.subscribeToEvent(Window.FrameLoopEvent.class, Debug::triangle);
			if (window != null) window.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
