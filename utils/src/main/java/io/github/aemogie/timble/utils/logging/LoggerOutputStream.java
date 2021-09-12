package io.github.aemogie.timble.utils.logging;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.util.function.Consumer;

public class LoggerOutputStream extends OutputStream {
	private final Consumer<String> logStream;
	
	public LoggerOutputStream(Consumer<String> logStream) {
		this.logStream = logStream;
	}
	
	@Override
	public void write(byte @NotNull [] b) {
		logStream.accept(new String(b));
	}
	
	@Override
	public void write(byte @NotNull [] b, int off, int len) {
		logStream.accept(new String(b, off, len));
	}
	
	@Override
	public void write(int b) {
		logStream.accept(new String(new byte[]{(byte) b}));
	}
}
