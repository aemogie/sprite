package io.github.aemogie.timble.gl;

import io.github.aemogie.timble.graphics.GraphicsAPI;
import io.github.aemogie.timble.graphics.Window;

import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.GLFW.*;

public class OpenGL extends GraphicsAPI {
	
	@Override
	public boolean init(Window window) {
		glfwMakeContextCurrent(window.getGLFWWindowPointer());
		GL.createCapabilities();
		return true;
	}
}