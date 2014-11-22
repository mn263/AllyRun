package sample;

import org.lwjgl.util.vector.*;

import javax.media.opengl.*;
import java.io.*;
import java.util.*;


public class Model {

	public Vector3f distFromOrigin = new Vector3f(0.0f, 0.0f, 0.0f);
	private Vector3f translate, scale;
	public List<Vector2f> vts;
	public List<Vector3f> verts;
	public List<Vector3f> norms;
	public List<Face> faces;
	private float vAngle, hAngle, objectAngle, startAngle;
	private boolean isFrontTire = false;
	private boolean isStationary = false;
//	private boolean isCar = false;


	public static Model getModel(String s, Vector3f translate, Vector3f scale, Vector3f distFromOrigin) {
		try {
			return new Model(s, translate, scale, distFromOrigin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setFrontTire(float objectAngle) {
		this.objectAngle = objectAngle;
		this.startAngle = objectAngle;
		isFrontTire = true;
	}
	public void setIsStationary(float objectAngle) {
		this.objectAngle = objectAngle;
		this.startAngle = objectAngle;
		this.isStationary = true;
	}

//	public void setIsCar(boolean isCar) {
//		this.isCar = isCar;
//	}

	public float getObjectAngle() {
		return this.objectAngle;
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

	public void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

		gl.glBegin(GL.GL_TRIANGLES);
		for (Face f : faces) {
			Vector3f v1 = verts.get((int) f.verts.x - 1);
			Vector3f n1 = norms.get((int) f.norms.x - 1);

			Vector3f v2 = verts.get((int) f.verts.y - 1);
			Vector3f n2 = norms.get((int) f.norms.y - 1);

			Vector3f v3 = verts.get((int) f.verts.z - 1);
			Vector3f n3 = norms.get((int) f.norms.z - 1);

			Vector3f v4 = null;
			Vector3f n4 = null;
			if (f.verts.w != 0) {
				v4 = verts.get((int) f.verts.w - 1);
				n4 = norms.get((int) f.norms.w - 1);
			}

			Vector2f vt1 = vts.get((int) f.vt.getX() - 1);
			Vector2f vt2 = vts.get((int) f.vt.getY() - 1);
			Vector2f vt3 = vts.get((int) f.vt.getZ() - 1);
			Vector2f vt4 = null;
			if (v4 != null && n4 != null) {
				vt4 = vts.get((int) f.vt.getW() - 1);
			}

			gl.glTexCoord2d(vt1.getX(), vt1.getY());
			n1 = transform(n1);
			gl.glNormal3d(n1.x, n1.y, n1.z);
			v1 = transform(v1);
			gl.glVertex3d(v1.x, v1.y, v1.z);

			gl.glTexCoord2d(vt2.getX(), vt2.getY());
			n2 = transform(n2);
			gl.glNormal3d(n2.x, n2.y, n2.z);
			v2 = transform(v2);
			gl.glVertex3d(v2.x, v2.y, v2.z);

			gl.glTexCoord2d(vt3.getX(), vt3.getY());
			n3 = transform(n3);
			gl.glNormal3d(n3.x, n3.y, n3.z);
			v3 = transform(v3);
			gl.glVertex3d(v3.x, v3.y, v3.z);

			if(v4 != null && n4 != null) {
				gl.glTexCoord2d(vt4.getX(), vt4.getY());
			n4 = transform(n4);
				gl.glNormal3d(n4.x, n4.y, n4.z);
			v4 = transform(v4);
				gl.glVertex3d(v4.x, v4.y, v4.z);
			}
		}
		gl.glEnd();
	}

	private Vector3f transform(Vector3f v) {
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

	public Vector3f driveForward(float degrees, String direction, Vector3f fakeDistFromOrigin) {
		objectAngle += degrees;
		Vector3f fakeDistFromOrigin2;
		if (direction.isEmpty()) {
			fakeDistFromOrigin = new Vector3f(0, 0, 0);
			fakeDistFromOrigin2 = vRotate(new Vector3f(-.5f, 0, 0), objectAngle - startAngle);
		} else if (direction.equals("right")) {
			fakeDistFromOrigin = vRotate(fakeDistFromOrigin, objectAngle - startAngle);
			fakeDistFromOrigin2 = vRotate(fakeDistFromOrigin, degrees);
		} else {
			fakeDistFromOrigin.z = fakeDistFromOrigin.z * (-1);
			fakeDistFromOrigin = vRotate(fakeDistFromOrigin, objectAngle - startAngle);
			fakeDistFromOrigin2 = vRotate(fakeDistFromOrigin, degrees);
		}
		distFromOrigin.setX(distFromOrigin.x + fakeDistFromOrigin2.x - fakeDistFromOrigin.x);
		distFromOrigin.setZ(distFromOrigin.z + fakeDistFromOrigin2.z - fakeDistFromOrigin.z);
		return distFromOrigin;
	}

	public void driveTire(float degrees, float direction, Vector3f fakeDistFromOrigin, Vector3f distFromCar) {
		Vector3f adjustedDistanceFromCar = vRotate(distFromCar, direction);
		distFromOrigin.x = fakeDistFromOrigin.x * 4 - adjustedDistanceFromCar.x;
		distFromOrigin.z = fakeDistFromOrigin.z * 4 - adjustedDistanceFromCar.z;

		objectAngle += degrees;
	}

	public Vector3f driveReverse(float degrees, String direction, Vector3f fakeDistFromOrigin) {
		objectAngle += degrees;
		Vector3f fakeDistFromOrigin2;

		if (direction.isEmpty()) {
			fakeDistFromOrigin = new Vector3f(0, 0, 0);
			fakeDistFromOrigin2 = vRotate(new Vector3f(0.5f, 0, 0), objectAngle - startAngle);
		} else if (direction.equals("right")) {
			fakeDistFromOrigin = vRotate(fakeDistFromOrigin, objectAngle - startAngle);
			fakeDistFromOrigin2 = vRotate(fakeDistFromOrigin, degrees);
		} else {
			fakeDistFromOrigin.z = fakeDistFromOrigin.z*(-1);
			fakeDistFromOrigin = vRotate(fakeDistFromOrigin, objectAngle - startAngle);
			fakeDistFromOrigin2 = vRotate(fakeDistFromOrigin, degrees);
		}
		distFromOrigin.setX(distFromOrigin.x + fakeDistFromOrigin2.x - fakeDistFromOrigin.x);
		distFromOrigin.setZ(distFromOrigin.z + fakeDistFromOrigin2.z - fakeDistFromOrigin.z);
		return distFromOrigin;
	}
}