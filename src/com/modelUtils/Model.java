package com.modelUtils;

import com.renderUtils.*;
import org.lwjgl.util.vector.*;
import org.lwjgl.util.vector.Vector2f;

import javax.media.opengl.*;
import java.io.*;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;


public class Model {

	protected ArrayList<RenderData> renderDataList = null;

	public Vector3f distFromOrigin = new Vector3f(0.0f, 0.0f, 0.0f);
	protected Vector3f translate;
	private Vector3f scale;
	public List<Vector2f> vts;
	public List<Vector3f> verts;
	public List<Vector3f> norms;
	public List<Face> faces;
	private float vAngle, hAngle, objectAngle;
	private boolean isFrontTire = false;
	private boolean isStationary = false;


	public static Model getModel(String s, Vector3f translate, Vector3f scale, Vector3f distFromOrigin) {
		try {
			return new Model(s, translate, scale, distFromOrigin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setIsStationary(float objectAngle) {
		this.objectAngle = objectAngle;
		this.isStationary = true;
	}

	public Model(String path, Vector3f translate, Vector3f scale, Vector3f distFromOrigin) throws IOException {
		this.distFromOrigin = distFromOrigin;
		this.translate = translate;
		this.scale = scale;
		this.vAngle = 0;
		this.hAngle = 0;
		this.objectAngle = 0;

		vts = new ArrayList<>();
		verts = new ArrayList<>();
		norms = new ArrayList<>();
		faces = new ArrayList<>();

		new ModelLoader(this, path);
	}

	public void updateTranslate(Vector3f newT) {
		this.translate = new Vector3f(translate.getX() + newT.getX(), translate.getY() + newT.getY(), translate.getZ() + newT.getZ());
	}

	public Vector3f getTranslate() {
		return translate;
	}

	public void setTranslate(float x, float y, float z) {
		this.translate = new Vector3f(x, y, z);
	}


	public void updateVerticalRotate(float angleChange) {
		this.vAngle += angleChange;
	}
	public void updateHorizontalRotate(float angleChange) {
		this.hAngle += angleChange;
	}
	public void updateTireRotation(float angleChange) {
		if (isFrontTire) {
			this.objectAngle += angleChange;
		}
	}

	private Vector3f translate(Vector3f v, Vector3f t) {
		return new Vector3f(v.getX() + t.getX(), v.getY() + t.getY(), v.getZ() + t.getZ());
	}
	private Vector3f scale(Vector3f v, Vector3f s) {
		return new Vector3f(v.getX() * s.getX(), v.getY() * s.getY(), v.getZ() * s.getZ());
	}
	private Vector3f vRotate(Vector3f v, float angle) {
		Matrix4D rotation = MatrixUtils.createRotationMatrix(angle);
		v = MatrixUtils.multiplyRowWithMatrix(rotation, v);
		return v;
	}
	private Vector3f hRotate(Vector3f v, float angle) {
		Matrix4D rotation = MatrixUtils.createVRotationMatrix(angle);
		v = MatrixUtils.multiplyRowWithMatrix(rotation, v);
		return v;
	}

	public void render(boolean updateData) {
		glBegin(GL.GL_TRIANGLES);
		if (this.renderDataList == null || updateData) updateRenderData();
		for (RenderData renderData : renderDataList) {
			displayRenderData(0, renderData);
			displayRenderData(1, renderData);
			displayRenderData(2, renderData);
		}
		glEnd();
	}

	protected void displayRenderData(int i, RenderData renderData) {
		Vector2f t = renderData.texCoords.get(i);
		glTexCoord2d(t.getX(), t.getY());

		Vector3f n = renderData.normalCoords.get(i);
		glNormal3d(n.getX(), n.getY(), n.getZ());

		Vector3f v = renderData.vertexCoords.get(i);
		glVertex3d(v.getX(), v.getY(), v.getZ());
	}

	protected void updateRenderData() {
		renderDataList = new ArrayList<>();
		for (Face f : faces) {
			RenderData renderData = new RenderData();
			Vector3f v1 = verts.get((int) f.verts.x - 1);
			Vector3f n1 = norms.get((int) f.norms.x - 1);

			Vector3f v2 = verts.get((int) f.verts.y - 1);
			Vector3f n2 = norms.get((int) f.norms.y - 1);

			Vector3f v3 = verts.get((int) f.verts.z - 1);
			Vector3f n3 = norms.get((int) f.norms.z - 1);

			Vector2f vt1 = vts.get((int) f.vt.getX() - 1);
			Vector2f vt2 = vts.get((int) f.vt.getY() - 1);
			Vector2f vt3 = vts.get((int) f.vt.getZ() - 1);

			renderData.texCoords.add(vt1);

			n1 = transform(n1);
			renderData.normalCoords.add(n1);

			v1 = transform(v1);
			renderData.vertexCoords.add(v1);


			renderData.texCoords.add(vt2);

			n2 = transform(n2);
			renderData.normalCoords.add(n2);


			v2 = transform(v2);
			renderData.vertexCoords.add(v2);

			renderData.texCoords.add(vt3);

			n3 = transform(n3);
			renderData.normalCoords.add(n3);


			v3 = transform(v3);
			renderData.vertexCoords.add(v3);

			renderDataList.add(renderData);
		}
	}

	protected Vector3f transform(Vector3f v) {
		if (this.isFrontTire) {
			v = vRotate(v, objectAngle);
		}
		if (this.isStationary) {
			v = vRotate(v, objectAngle);
		}
		v = translate(v, this.distFromOrigin);
		v = scale(v, this.scale);
		v = vRotate(v, vAngle);
		v = hRotate(v, hAngle);
		v = translate(v, this.translate);
		return v;
	}
}