package io.github.aemogie.timble.utils.logging

import java.io.OutputStream
import java.util.function.Consumer

class LoggerOutputStream(private val logStream: Consumer<String>) : OutputStream() {
	override fun write(b: ByteArray) {
		logStream.accept(String(b))
	}
	
	override fun write(b: ByteArray, off: Int, len: Int) {
		logStream.accept(String(b, off, len))
	}
	
	override fun write(b: Int) {
		logStream.accept(String(byteArrayOf(b.toByte())))
	}
}