package io.github.aemogie.timble.graphics.window;

import io.github.aemogie.timble.graphics.utils.exceptions.GLFWInitializationException;
import io.github.aemogie.timble.graphics.utils.exceptions.WindowCreationException;
import io.github.aemogie.timble.utils.events.Event;
import io.github.aemogie.timble.utils.events.EventBus;
import io.github.aemogie.timble.utils.logging.Logger;

import static io.github.aemogie.timble.utils.logging.LogManager.getLogger;
import static io.github.aemogie.timble.utils.logging.LogManager.nullifyLogger;

import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
	private long window; //don't modify. removing "final" so it's possible to modify in init()
	private int width, height;
	private CharSequence title;
	private boolean vsync;
	
	private Window(int width, int height, CharSequence title, boolean vsync) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.vsync = vsync;
	}
	
	public boolean run() throws WindowCreationException {
		if (!init()) return false;
		while (!glfwWindowShouldClose(window))
			if (!EventBus.fireEvent(new FrameLoopEvent())) glfwWindowShouldClose(window);
		return destroy();
	}
	
	private boolean init() throws WindowCreationException {
		GLFWErrorCallback.createPrint(System.err).set();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL) throw new WindowCreationException("oops! we were unable to initialise your window.");
		glfwMakeContextCurrent(window);
		glfwSwapInterval(vsync ? 1 : 0);
		glfwShowWindow(window);
		return EventBus.fireEvent(new InitEvent());
	}
	
	private boolean destroy() {
		boolean eventSuccess = EventBus.fireEvent(new DestroyEvent());
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		GLFWErrorCallback callback;
		if ((callback = glfwSetErrorCallback(null)) != null) callback.free();
		nullifyLogger();
		return glfwGetCurrentContext() == NULL && eventSuccess;
	}
	
	public static class Builder {
		
		private Logger logger = getLogger();
		private int width = 480, height = 480;
		private boolean vsync = true;
		private CharSequence title = "timble. engine. (by aemogie.)";
		
		private Builder() throws GLFWInitializationException {
			if (!glfwInit()) throw new GLFWInitializationException("oops! we were unable to initialize GLFW.");
		}
		
		public static synchronized Builder create() throws GLFWInitializationException {
			return new Builder();
		}
		
		public Builder setLogger(Logger logger) {
			this.logger = logger;
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
			return new Window(width, height, title, vsync);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (!(obj instanceof Builder other)) return false;
			return logger == other.logger && width == other.width && height == other.height && vsync == other.vsync && title == other.title;
		}
	}
	
	public class InitEvent extends Event {
		public Window getWindow() {
			return Window.this;
		}
	}
	
	public class FrameLoopEvent extends Event {
		private static double elapsedTime = 0;
		private double deltaTime;
		
		@Override
		public boolean fire() {
			deltaTime = glfwGetTime() - elapsedTime;
			elapsedTime += deltaTime;
			glfwSwapBuffers(Window.this.window);
			glfwPollEvents();
			return super.fire();
		}
		
		public double getDeltaTime() {
			return deltaTime;
		}
		
		public double getElapsedTime() {
			return elapsedTime;
		}
		
		public long getWindowPointer() {
			return Window.this.window;
		}
		
		public boolean setTitle(Object title) {
			String newTitle;
			if (title == null) return true;
			else newTitle = String.valueOf(title);
			CharSequence windowTitle = Window.this.title;
			if (newTitle.isBlank() || windowTitle.equals(title)) return true;
			Window.this.title = newTitle;
			glfwSetWindowTitle(window, Window.this.title);
			return true;
		}
	}
	
	public class DestroyEvent extends Event {
		public Window getWindow() {
			return Window.this;
		}
	}
}