package io.github.aemogie.timble.gl.utils;

import io.github.aemogie.timble.gl.window.Window;
import io.github.aemogie.timble.utils.events.Event.Listener;

public class Titles {
	
	public static Listener<Window.FrameLoopEvent> FPS_TITLE = new Listener<>(false) {
		private static final double REFRESH = 0.175;
		private static double refreshTemp = REFRESH;
		
		@Override
		public boolean onFire(Window.FrameLoopEvent event) {
			refreshTemp -= event.getDeltaTime();
			if (refreshTemp < 0) {
				refreshTemp = REFRESH;
				event.setTitle((int) (1 / event.getDeltaTime()));
			}
			return true;
		}
	};
}
