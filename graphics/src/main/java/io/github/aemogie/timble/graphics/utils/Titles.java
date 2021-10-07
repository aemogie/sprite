package io.github.aemogie.timble.graphics.utils;

import io.github.aemogie.timble.graphics.Window;
import io.github.aemogie.timble.utils.events.Event.Listener;

public final class Titles {
	private Titles() {}
	
	public static final Listener<Window.FrameLoopEvent> FPS_TITLE = new Listener<>(false) {
		private static final double REFRESH = 0.175;
		private static double refreshTemp = REFRESH;
		
		@Override
		public boolean fire(Window.FrameLoopEvent event) {
			refreshTemp -= event.getDeltaTime();
			if (refreshTemp < 0) {
				refreshTemp = REFRESH;
				event.setTitle((int) (1 / event.getDeltaTime()));
			}
			return true;
		}
	};
}
