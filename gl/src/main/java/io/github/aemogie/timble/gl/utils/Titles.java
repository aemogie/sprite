package io.github.aemogie.timble.gl.utils;

import io.github.aemogie.timble.gl.window.Window;
import io.github.aemogie.timble.utils.events.EventBus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;

public class Titles {
	public static boolean setTitle(Template title) {
		return EventBus.subscribeToEvent(Window.FrameLoopEvent.class, title);
	}
	
	public abstract static class Template implements Function<Window.FrameLoopEvent, Boolean> {
		@Override
		public Boolean apply(Window.FrameLoopEvent frameLoopEvent) {
			String title = getTitle(frameLoopEvent);
			if (!(title == null || title.isEmpty())) glfwSetWindowTitle(frameLoopEvent.getWindowPointer(), title);
			return true;
		}
		
		@Nullable
		protected abstract String getTitle(Window.FrameLoopEvent frameLoopEvent);
	}
	
	public static class FpsTitle extends Template {
		private static final double REFRESH = 0.175;
		private static double refreshTemp = REFRESH;
		
		@Override
		protected String getTitle(Window.FrameLoopEvent frameLoopEvent) {
			refreshTemp -= frameLoopEvent.getDeltaTime();
			if (refreshTemp < 0) {
				refreshTemp = REFRESH;
				return String.valueOf((int) (1 / frameLoopEvent.getDeltaTime()));
			}
			return null;
		}
	}
}
