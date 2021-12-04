package io.github.aemogie.timble.gl.utils;

import io.github.aemogie.timble.graphics.Window.FrameLoopEvent;

import static org.lwjgl.opengl.GL11.*;

public final class Debug {
	private Debug() {}
	@SuppressWarnings("unused")
	public static Boolean legacyTriangle(FrameLoopEvent event) {
		glBegin(GL_TRIANGLES);
		glVertex2f(-0.5f, -0.5f);
		glVertex2f(0.0f, 0.5f);
		glVertex2f(0.5f, -0.5f);
		glEnd();
		return true;
	}
}
