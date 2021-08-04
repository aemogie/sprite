package io.github.aemogie.timble.gl.window;

import io.github.aemogie.timble.gl.utils.exceptions.GLFWInitializationException;
import io.github.aemogie.timble.gl.utils.exceptions.WindowCreationException;
import io.github.aemogie.timble.utils.events.Event;
import io.github.aemogie.timble.utils.events.EventBus;
import io.github.aemogie.timble.utils.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFWErrorCallback;

import static io.github.aemogie.timble.utils.logging.LogManager.nullifyLogger;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	private final Logger LOGGER;
	private final long WINDOW;
	private int width, height;
	private String title;
	private FrameLoopEvent frameLoopEvent;
	private boolean vsync;
	
	private Window(@Nullable Logger logger, int width, int height, String title, boolean vsync) throws WindowCreationException {
		this.LOGGER = logger;
		if (LOGGER != null) LOGGER.debugln("trying to create your amazing window...");
		this.width = width;
		this.height = height;
		this.title = title;
		this.vsync = vsync;
		this.WINDOW = init();
	}
	
	@NotNull
	public static Window create(Logger logger, int width, int height, String title, boolean vsync) throws GLFWInitializationException, WindowCreationException {
		if (!glfwInit()) throw new GLFWInitializationException("oops! we were unable to initialize GLFW.");
		return new Window(logger, width, height, title, vsync);
	}
	
	public boolean run() {
		while (!glfwWindowShouldClose(WINDOW)) {
			if (!EventBus.fireEvent(FrameLoopEvent.class)) return false;
		}
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
		glfwShowWindow(window);
		if (LOGGER != null) LOGGER.debugln("window should be visible :)");
		frameLoopEvent = new FrameLoopEvent();
		EventBus.registerEvent(frameLoopEvent);
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
	}
}
