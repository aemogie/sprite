package io.github.aemogie.timble.utils.logging

import java.io.OutputStream
import java.io.PrintStream
import java.lang.System.lineSeparator
import java.lang.Thread.currentThread
import java.time.Instant.now

@Suppress("MemberVisibilityCanBePrivate", "unused") //java interop nullability fix.
class LoggerPrintStream( //could make internal but you never know
	val level: Level
) : PrintStream(NullOutputStream()) {
	private fun write(content: String) = synchronized(records) {
		//[0] = Thread#getStackTrace
		//[1] = LoggerPrintStream#write
		//[2] = LoggerPrintStream#print(ln)
		//[3] = Caller#method
		records += LogRecord(
			level, currentThread(), currentThread().stackTrace[3], now(), content.removeSuffix(lineSeparator())
		)
	}

	//region PrintStream#print(ln)
	override fun println() = write("")
	override fun println(x: Boolean) = write(x.toString())
	fun println(x: Boolean?) = write(x.toString())
	override fun println(x: Char) = write(x.toString())
	fun println(x: Char?) = write(x.toString())
	override fun println(x: Int) = write(x.toString())
	fun println(x: Int?) = write(x.toString())
	override fun println(x: Long) = write(x.toString())
	fun println(x: Long?) = write(x.toString())
	override fun println(x: Float) = write(x.toString())
	fun println(x: Float?) = write(x.toString())
	override fun println(x: Double) = write(x.toString())
	fun println(x: Double?) = write(x.toString())
	override fun println(x: CharArray?) = write(x.toString())
	override fun println(x: String?) = write(x.toString())
	override fun println(x: Any?) = write(x.toString())
	override fun print(x: Boolean) = write(x.toString())
	fun print(x: Boolean?) = write(x.toString())
	override fun print(x: Char) = write(x.toString())
	fun print(x: Char?) = write(x.toString())
	override fun print(x: Int) = write(x.toString())
	fun print(x: Int?) = write(x.toString())
	override fun print(x: Long) = write(x.toString())
	fun print(x: Long?) = write(x.toString())
	override fun print(x: Float) = write(x.toString())
	fun print(x: Float?) = write(x.toString())
	override fun print(x: Double) = write(x.toString())
	fun print(x: Double?) = write(x.toString())
	override fun print(x: CharArray?) = write(x.toString())
	override fun print(x: String?) = write(x.toString())
	override fun print(x: Any?) = write(x.toString())
	//endregion

	//region Delete
	override fun write(b: Int) = Unit
	override fun write(buf: ByteArray, off: Int, len: Int) = Unit
	override fun write(buf: ByteArray) = Unit
	override fun writeBytes(buf: ByteArray?) = Unit
	override fun close() = Unit
	override fun flush() = Unit
	//endregion

	class NullOutputStream : OutputStream() {
		override fun write(b: Int) = Unit
		override fun write(b: ByteArray) = Unit
		override fun write(b: ByteArray, off: Int, len: Int) = Unit
		override fun close() = Unit
		override fun flush() = Unit
	}
}