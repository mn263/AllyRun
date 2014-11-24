package com.modelUtils;

import org.lwjgl.util.vector.*;

public class Face {

	public Vector4f norms, verts, vt;

	public Face(Vector4f n, Vector4f v, Vector4f vt) {
		this.norms = n;
		this.verts = v;
		this.vt = vt;
	}
}
