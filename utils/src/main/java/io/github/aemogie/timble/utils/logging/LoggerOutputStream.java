package io.github.aemogie.timble.utils.logging;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.util.function.Consumer;

public class LoggerOutputStream extends OutputStream {
	private final Consumer<String> LOG_STREAM;
	
	public LoggerOutputStream(Consumer<String> logStream) {
		this.LOG_STREAM = logStream;
	}
	
	@Override
	public void write(byte @NotNull [] b) {
		LOG_STREAM.accept(new String(b));
	}
	
	@Override
	public void write(byte @NotNull [] b, int off, int len) {
		LOG_STREAM.accept(new String(b, off, len));
	}
	
	@Override
	public void write(int b) {
		LOG_STREAM.accept(new String(new byte[]{(byte) b}));
	}
}
