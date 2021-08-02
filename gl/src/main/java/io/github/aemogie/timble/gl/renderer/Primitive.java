package io.github.aemogie.timble.gl.renderer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class Primitive implements Iterable<Vertex> {
	private List<Vertex> vertices = new ArrayList<>();
	
	public Primitive(Vertex... vertices) {
		this.vertices.addAll(Arrays.stream(vertices).toList());
	}
	
	@NotNull
	@Override
	public Iterator<Vertex> iterator() {
		return vertices.iterator();
	}
}