package io.github.aemogie.timble.utils

import org.intellij.lang.annotations.Language
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.ByteBuffer

val clazz = object : Any() {}::class.java.enclosingClass!!

inline fun <reified T> getResourceBytes(
	@Language("file-reference") path: String,
	map: (ByteArray) -> T
): T = map((clazz.getResourceAsStream(path) ?: throw FileNotFoundException(path)).readAllBytes())

inline fun <reified T> getResourceText(
	@Language("file-reference") path: String,
	map: (String) -> T
): T = map(String((clazz.getResourceAsStream(path) ?: throw FileNotFoundException(path)).readAllBytes()))

inline fun <reified T> getResourceStream(
	@Language("file-reference") path: String,
	map: (BufferedInputStream) -> T
): T = map(BufferedInputStream(clazz.getResourceAsStream(path) ?: throw FileNotFoundException(path)))

inline fun <reified T> getResourceReader(
	@Language("file-reference") path: String,
	map: (BufferedReader) -> T
): T = map(BufferedReader(InputStreamReader(clazz.getResourceAsStream(path) ?: throw FileNotFoundException(path))))

inline fun <reified T> getResourceByteBuffer(
	@Language("file-reference") path: String,
	map: (ByteBuffer) -> T
): T = map(ByteBuffer.wrap((clazz.getResourceAsStream(path) ?: throw FileNotFoundException(path)).readAllBytes()))