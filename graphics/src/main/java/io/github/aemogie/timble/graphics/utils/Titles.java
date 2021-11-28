package io.github.aemogie.timble.graphics.utils;

import io.github.aemogie.timble.graphics.Window.FrameLoopEvent;

public final class Titles {
	private Titles() {}
	
	private static final double FPS_TITLE_REFRESH_RATE = 0.175;
	private static double fpsTitleRefreshProgress = 0;
	
	public static Boolean fpsTitle(FrameLoopEvent event) {
		fpsTitleRefreshProgress += event.deltaTime;
		if (fpsTitleRefreshProgress > FPS_TITLE_REFRESH_RATE) {
			fpsTitleRefreshProgress = 0;
			event.getWindow().setTitle((int) (1 / event.deltaTime));
		}
		return true;
	}
}
