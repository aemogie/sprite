package io.github.aemogie.timble.engine.utils;

import io.github.aemogie.timble.gl.window.Window;

import static org.lwjgl.opengl.GL11.*;

public class Debug {
	public static Boolean triangle(Window.FrameLoopEvent event) {
		glBegin(GL_TRIANGLES);
		glVertex2f(-5f, -5f);
		glVertex2f(0f, 5f);
		glVertex2f(5f, -5f);
		glEnd();
		return true;
	}
}
