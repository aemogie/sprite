package io.github.aemogie.timble.gl.utils

import org.lwjgl.opengl.GL11.*

fun legacyTriangle() = true.also {
	glBegin(GL_TRIANGLES)
	glVertex2f(-0.5f, -0.5f)
	glVertex2f(0.0f, 0.5f)
	glVertex2f(0.5f, -0.5f)
	glEnd()
}