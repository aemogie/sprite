package io.github.aemogie.timble.gl.renderer;

import java.nio.BufferOverflowException;
import java.nio.IntBuffer;
import java.util.HashMap;

public final class Vertex {
	HashMap<String, Vertex.Attribute> attributes = new HashMap<>();
	
	public boolean addData(String name, Vertex.Attribute data) {
		if (name == null || name.isBlank()) return false;
		attributes.put(name, data);
		return attributes.get(name) != null;
	}
	
	public Attribute getData(String name) {
		return attributes.get(name);
	}
	
	public interface Attribute {
		IntBuffer buffer(IntBuffer buffer) throws BufferOverflowException;
	}
}