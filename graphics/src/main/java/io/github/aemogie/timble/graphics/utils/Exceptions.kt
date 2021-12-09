package io.github.aemogie.timble.graphics.utils

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.APIUtil.apiClassTokens

class GLFWInitializationException : Exception()
class WindowCreationException : Exception()

class GLFWException(error: Int, description: Long) : RuntimeException(
	"${ERROR_CODES[error]} - ${GLFWErrorCallback.getDescription(description)}"
) {
	private companion object {
		val ERROR_CODES: MutableMap<Int, String> = apiClassTokens(
			//errors enums are in the 0x1XXXX range.
			{ _, value -> value in 0x10000..0x1FFFF }, null, GLFW::class.java
		)
	}
}