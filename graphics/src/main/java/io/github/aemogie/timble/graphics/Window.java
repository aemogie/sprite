package io.github.aemogie.timble.graphics;

import io.github.aemogie.timble.graphics.utils.exceptions.GLFWInitializationException;
import io.github.aemogie.timble.graphics.utils.exceptions.WindowCreationException;
import io.github.aemogie.timble.utils.events.Event;
import io.github.aemogie.timble.utils.events.EventNode;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

@SuppressWarnings("FieldMayBeFinal") //TODO: Implement setters and callbacks.
public final class Window extends EventNode {
	private long windowPointer; //don't modify. removing "final" so it's possible to modify in init()
	private final GraphicsAPI api;
	private int width;
	private int height;
	private CharSequence title;
	private boolean vsync;
	
	private double elapsedTime = 0;
	
	private Window(GraphicsAPI api, int width, int height, CharSequence title, boolean vsync) {
		this.api = api;
		this.width = width;
		this.height = height;
		this.title = title;
		this.vsync = vsync;
	}
	
	public boolean run() throws WindowCreationException {
		if (!init()) return false;
		while (!glfwWindowShouldClose(windowPointer)) {
			double dt = glfwGetTime() - elapsedTime;
			elapsedTime += dt;
			glfwSwapBuffers(windowPointer);
			glfwPollEvents();
			FrameLoopEvent frame = new FrameLoopEvent(dt);
			if (!fire(frame)) glfwSetWindowShouldClose(windowPointer, true);
		}
		return destroy();
	}
	
	private boolean init() throws WindowCreationException {
		GLFWErrorCallback.createPrint(System.err).set();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		windowPointer = glfwCreateWindow(width, height, title, NULL, NULL);
		if (windowPointer == NULL) throw new WindowCreationException("oops! we were unable to initialise your window.");
		api.init(this);
		glfwSwapInterval(vsync ? 1 : 0);
		glfwShowWindow(windowPointer);
		return fire(new InitEvent());
	}
	
	private boolean destroy() {
		boolean eventSuccess = fire(new DestroyEvent());
		glfwFreeCallbacks(windowPointer);
		glfwDestroyWindow(windowPointer);
		glfwTerminate();
		GLFWErrorCallback callback;
		if ((callback = glfwSetErrorCallback(null)) != null) callback.free();
		return glfwGetCurrentContext() == NULL && eventSuccess;
	}
	
	public long getGLFWWindowPointer() {
		return windowPointer;
	}
	
	public boolean setTitle(Object title) {
		String newTitle;
		if (title == null) return true;
		else newTitle = String.valueOf(title);
		CharSequence windowTitle = Window.this.title;
		if (newTitle.isBlank() || windowTitle.equals(title)) return true;
		Window.this.title = newTitle;
		glfwSetWindowTitle(windowPointer, Window.this.title);
		return true;
	}
	
	public static final class Builder {
		private @NotNull GraphicsAPI api;
		private int width = 480;
		private int height = 480;
		private boolean vsync = true;
		private CharSequence title = "timble. engine. (by aemogie.)";
		
		private Builder(@NotNull GraphicsAPI api) throws GLFWInitializationException {
			if (!glfwInit()) throw new GLFWInitializationException("oops! we were unable to initialize GLFW.");
			this.api = api;
		}
		
		public static synchronized Builder create(@NotNull GraphicsAPI api) throws GLFWInitializationException {
			return new Builder(api);
		}
		
		public Builder setApi(@NotNull GraphicsAPI api) {
			this.api = api;
			return this;
		}
		
		public Builder setWidth(int width) {
			this.width = width;
			return this;
		}
		
		public Builder setHeight(int height) {
			this.height = height;
			return this;
		}
		
		public Builder setVsync(boolean vsync) {
			this.vsync = vsync;
			return this;
		}
		
		public Builder setTitle(CharSequence title) {
			this.title = title;
			return this;
		}
		
		public Window build() {
			return new Window(api, width, height, title, vsync);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(api, width, height, vsync, title);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (!(obj instanceof Builder other)) return false;
			return width == other.width && height == other.height && vsync == other.vsync && Objects.equals(title, other.title);
		}
	}
	
	private class WindowEvent extends Event {
		public Window getWindow() { return Window.this; }
	}
	
	public class InitEvent extends WindowEvent {}
	
	public class FrameLoopEvent extends WindowEvent {
		public final double deltaTime;
		
		public FrameLoopEvent(double deltaTime) {
			this.deltaTime = deltaTime;
		}
	}
	
	public class DestroyEvent extends WindowEvent {}
}