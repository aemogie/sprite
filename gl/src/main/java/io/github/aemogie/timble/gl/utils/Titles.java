package io.github.aemogie.timble.gl.utils;

import io.github.aemogie.timble.gl.window.Window;
import io.github.aemogie.timble.utils.events.EventBus;

import java.util.function.Function;

public class Titles {
	
	private static Function<Window.FrameLoopEvent, Boolean> title;
	
	public static boolean setTitle(Function<Window.FrameLoopEvent, Object> title, Window window) {
		Titles.title = e -> e.setTitle(title.apply(e));
		return EventBus.subscribeToEvent(Window.FrameLoopEvent.class, Titles.title, false, window);
	}
	
	public static Function<Window.FrameLoopEvent, Object> FPS_TITLE = new Function<>() {
		private static final double REFRESH = 0.175;
		private static double refreshTemp = REFRESH;
		
		@Override
		public String apply(Window.FrameLoopEvent frameLoopEvent) {
			refreshTemp -= frameLoopEvent.getDeltaTime();
			if (refreshTemp < 0) {
				refreshTemp = REFRESH;
				return String.valueOf((int) (1 / frameLoopEvent.getDeltaTime()));
			}
			return null;
		}
	};
}
