package io.github.aemogie.timble.graphics.utils.exceptions;

public class GLFWInitializationException extends Exception {
	public GLFWInitializationException() {
		this("Could not initialise GLFW.");
	}
	
	public GLFWInitializationException(String s) {
		super(s);
	}
}