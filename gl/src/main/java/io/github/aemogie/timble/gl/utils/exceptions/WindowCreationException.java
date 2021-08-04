package io.github.aemogie.timble.gl.utils.exceptions;

public class WindowCreationException extends Exception {
	public WindowCreationException() {
		this("Could not create the window.");
	}
	
	public WindowCreationException(String s) {
		super(s);
	}
}