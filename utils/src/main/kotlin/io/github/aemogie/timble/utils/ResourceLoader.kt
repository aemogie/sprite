package io.github.aemogie.timble.utils

import org.intellij.lang.annotations.Language
import java.io.*

private val clazz = object : Any() {}::class.java.enclosingClass

private fun <T> file(
	path: String,
	map: (InputStream) -> T
) = map(clazz.getResourceAsStream(path) ?: throw FileNotFoundException(path))

fun <T> getResourceBytes(
	@Language("file-reference") path: String,
	map: (ByteArray) -> T
): T = file(path) { map(it.readAllBytes()) }

fun <T> getResourceText(
	@Language("file-reference") path: String,
	map: (String) -> T
): T = file(path) { map(String(it.readAllBytes())) }

fun <T> getResourceStream(
	@Language("file-reference") path: String,
	map: (BufferedInputStream) -> T
): T = file(path) { map(BufferedInputStream(it)) }

fun <T> getResourceReader(
	@Language("file-reference") path: String,
	map: (BufferedReader) -> T
): T = file(path) { map(BufferedReader(InputStreamReader(it))) }
