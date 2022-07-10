package io.github.aemogie.timble.utils

import org.intellij.lang.annotations.Language
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.ByteBuffer


@Suppress("unused")
object ResourceLoader {
	fun getFile(path: String) = ResourceLoader.javaClass.getResourceAsStream(path) ?: throw FileNotFoundException(path)

	inline fun <T> bytes(
		@Language("file-reference") path: String, map: (ByteArray) -> T
	): T = map(getFile(path).readAllBytes())

	inline fun <T> text(
		@Language("file-reference") path: String, map: (String) -> T
	): T = map(String(getFile(path).readAllBytes()))

	inline fun <T> stream(
		@Language("file-reference") path: String, map: (BufferedInputStream) -> T
	): T = map(BufferedInputStream(getFile(path)))

	inline fun <T> reader(
		@Language("file-reference") path: String, map: (BufferedReader) -> T
	): T = map(BufferedReader(InputStreamReader(getFile(path))))

	inline fun <T> byteBuffer(
		@Language("file-reference") path: String, map: (ByteBuffer) -> T
	): T = map(ByteBuffer.wrap(getFile(path).readAllBytes()))
}