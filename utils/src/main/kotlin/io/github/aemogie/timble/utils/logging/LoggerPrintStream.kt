package io.github.aemogie.timble.utils.logging

import java.io.PrintStream
import java.lang.System.lineSeparator

open class LoggerPrintStream(
	private val level: Level
) : PrintStream(nullOutputStream()) {
	protected open fun write(content: String) = log(
		content = content.removeSuffix(lineSeparator()),
		level = level,
		caller = Thread.currentThread().stackTrace[4]
	)

	//region PrintStream#print(ln)
	override fun println() = write("")
	override fun println(x: Boolean) = write(x.toString())
	override fun println(x: Char) = write(x.toString())
	override fun println(x: Int) = write(x.toString())
	override fun println(x: Long) = write(x.toString())
	override fun println(x: Float) = write(x.toString())
	override fun println(x: Double) = write(x.toString())
	override fun println(x: CharArray) = write(x.toString())
	override fun println(x: String?) = write(x.toString())
	override fun println(x: Any?) = write(x.toString())
	override fun print(x: Boolean) = write(x.toString())
	override fun print(x: Char) = write(x.toString())
	override fun print(x: Int) = write(x.toString())
	override fun print(x: Long) = write(x.toString())
	override fun print(x: Float) = write(x.toString())
	override fun print(x: Double) = write(x.toString())
	override fun print(x: CharArray) = write(x.toString())
	override fun print(x: String?) = write(x.toString())
	override fun print(x: Any?) = write(x.toString())
	//endregion

	//region Delete
	override fun write(b: Int) = Unit
	override fun write(buf: ByteArray, off: Int, len: Int) = Unit
	override fun write(buf: ByteArray) = Unit
	override fun close() = Unit
	override fun flush() = Unit
	//endregion
}