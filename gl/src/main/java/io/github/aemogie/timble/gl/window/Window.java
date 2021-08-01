package io.github.aemogie.timble.gl.window;

import io.github.aemogie.timble.utils.events.Event;
import io.github.aemogie.timble.utils.events.EventBus;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFWErrorCallback;

import static io.github.aemogie.timble.utils.logging.LogManager.getLogger;
import static io.github.aemogie.timble.utils.logging.LogManager.nullifyLogger;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	private final long WINDOW;
	private int width, height;
	private String title;
	Frame currentFrame;
	
	private Window(int width, int height, String title) {
		getLogger().debugln("trying to create your amazing window...");
		this.title = title;
		this.width = width;
		this.height = height;
		if ((WINDOW = init()) == NULL) {
			throw new IllegalStateException("oops! we got an error while trying to initialise your window.");
		}
	}
	
	@NotNull
	public static Window create(int width, int height, String title) {
		if (!glfwInit()) throw new IllegalStateException("oops! we were unable to initialize GLFW.");
		return new Window(width, height, title);
	}
	
	public boolean run() {
		while (!glfwWindowShouldClose(WINDOW)) {
			if (!currentFrame.run()) return false;
		}
		return destroy();
	}
	
	private long init() {
		GLFWErrorCallback.createPrint(System.err).set();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		long window = glfwCreateWindow(width, height, title, NULL, NULL);
		glfwMakeContextCurrent(window);
//		glfwSwapInterval(1);
		glfwShowWindow(window);
		getLogger().debugln("window should be visible :)");
		currentFrame = new Frame();
		EventBus.registerEvent(currentFrame);
		return window;
	}
	
	private boolean destroy() {
		getLogger().debugln("you closed it? ugh, fine! destroying...");
		glfwFreeCallbacks(WINDOW);
		glfwDestroyWindow(WINDOW);
		glfwTerminate();
		GLFWErrorCallback callback;
		if ((callback = glfwSetErrorCallback(null)) != null) callback.free();
		nullifyLogger();
		return glfwGetCurrentContext() == NULL;
	}
	
	public class Frame extends Event {
		private static double elapsedTime = 0;
		private double deltaTime;
		
		public boolean run() {
			deltaTime = glfwGetTime() - elapsedTime;
			elapsedTime += deltaTime;
			glfwSwapBuffers(WINDOW);
			glfwPollEvents();
			return EventBus.fireEvent(getClass());
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
