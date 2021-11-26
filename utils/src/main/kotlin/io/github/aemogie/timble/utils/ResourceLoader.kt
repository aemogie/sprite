package io.github.aemogie.timble.utils

import org.intellij.lang.annotations.Language
import java.io.*

//load a resource using class loader and map it using mapping function
//in a class cz then i can use `this::class.java`. but the method naming is meant for static imports.
object ResourceLoader {
	private fun file(path: String) = this::class.java.getResourceAsStream(path) ?: throw FileNotFoundException(path)
	
	@JvmStatic fun <T> getResourceBytes(
		@Language("file-reference") path: String,
		map: (ByteArray) -> T
	): T = file(path).use { map(it.readAllBytes()) }
	
	@JvmStatic fun <T> getResourceStream(
		@Language("file-reference") path: String,
		map: (BufferedInputStream) -> T
	): T = BufferedInputStream(file(path)).use(map)
	
	@JvmStatic fun <T> getResourceReader(
		@Language("file-reference") path: String,
		map: (BufferedReader) -> T
	): T = BufferedReader(InputStreamReader(file(path))).use(map)
	
	@JvmStatic fun <T> getResourceText(
		@Language("file-reference") path: String,
		map: (String) -> T
	): T = file(path).use { map(String(it.readAllBytes())) }
}