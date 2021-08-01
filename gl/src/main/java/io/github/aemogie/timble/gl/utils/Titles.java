package io.github.aemogie.timble.gl.utils;

import io.github.aemogie.timble.gl.window.Window;
import io.github.aemogie.timble.utils.events.EventBus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;

public class Titles {
	public static boolean setTitle(Template title) {
		return EventBus.subscribeToEvent(Window.Frame.class, title);
	}
	
	public abstract static class Template implements Function<Window.Frame, Boolean> {
		@Override
		public Boolean apply(Window.Frame frame) {
			String title = getTitle(frame);
			if (!(title == null || title.isEmpty())) glfwSetWindowTitle(frame.getWindowPointer(), title);
			return true;
		}
		
		@Nullable
		protected abstract String getTitle(Window.Frame frame);
	}
	
	public static class FpsTitle extends Template {
		private static final double REFRESH = 0.175;
		private static double refreshTemp = REFRESH;
		
		@Override
		protected String getTitle(Window.Frame frame) {
			refreshTemp -= frame.getDeltaTime();
			if (refreshTemp < 0) {
				refreshTemp = REFRESH;
				return String.valueOf((int) (1 / frame.getDeltaTime()));
			}
			return null;
		}
	}
}
