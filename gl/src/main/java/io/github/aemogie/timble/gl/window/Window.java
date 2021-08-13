package io.github.aemogie.timble.gl.window;

import io.github.aemogie.timble.gl.utils.exceptions.GLFWInitializationException;
import io.github.aemogie.timble.gl.utils.exceptions.WindowCreationException;
import io.github.aemogie.timble.utils.events.Event;
import io.github.aemogie.timble.utils.events.EventBus;
import io.github.aemogie.timble.utils.logging.Logger;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static io.github.aemogie.timble.utils.logging.LogManager.getLogger;
import static io.github.aemogie.timble.utils.logging.LogManager.nullifyLogger;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	private final Logger LOGGER;
	private final long WINDOW;
	private int width, height;
	private CharSequence title;
	private boolean vsync;
	
	private Window(@Nullable Logger logger, int width, int height, CharSequence title, boolean vsync) throws WindowCreationException {
		LOGGER = logger;
		if (LOGGER != null) LOGGER.debugln("trying to create your amazing window...");
		this.width = width;
		this.height = height;
		this.title = title;
		this.vsync = vsync;
		WINDOW = init();
	}
	
	public boolean run() {
		while (!glfwWindowShouldClose(WINDOW)) if (!EventBus.fireEvent(FrameLoopEvent.class)) return false;
		return destroy();
	}
	
	private long init() throws WindowCreationException {
		GLFWErrorCallback.createPrint(System.err).set();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		long window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL) throw new WindowCreationException("oops! we were unable to initialise your window.");
		glfwMakeContextCurrent(window);
		glfwSwapInterval(vsync ? 1 : 0);
		GL.createCapabilities();
		glfwShowWindow(window);
		if (LOGGER != null) LOGGER.debugln("window should be visible :)");
		EventBus.registerEvent(new FrameLoopEvent());
		return window;
	}
	
	private boolean destroy() {
		if (LOGGER != null) LOGGER.debugln("you closed it? ugh, fine! destroying...");
		glfwFreeCallbacks(WINDOW);
		glfwDestroyWindow(WINDOW);
		glfwTerminate();
		GLFWErrorCallback callback;
		if ((callback = glfwSetErrorCallback(null)) != null) callback.free();
		nullifyLogger();
		return glfwGetCurrentContext() == NULL;
	}
	
	public class FrameLoopEvent extends Event {
		private static double elapsedTime = 0;
		private double deltaTime;
		
		@Override
		public boolean fire() {
			deltaTime = glfwGetTime() - elapsedTime;
			elapsedTime += deltaTime;
			glfwSwapBuffers(WINDOW);
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
			return WINDOW;
		}
		
		public boolean setTitle(CharSequence title) {
			if (title == null || title.isEmpty() || Window.this.title.equals(title)) return true;
			Window.this.title = title;
			glfwSetWindowTitle(WINDOW, title);
			return true;
		}
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
		
		public Window build() throws WindowCreationException {
			return new Window(logger, width, height, title, vsync);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (!(obj instanceof Builder other)) return false;
			return logger == other.logger && width == other.width && height == other.height && vsync == other.vsync && title == other.title;
		}
	}
}
