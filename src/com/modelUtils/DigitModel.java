package com.modelUtils;

import org.lwjgl.util.vector.*;

import java.io.*;

public class DigitModel extends Model {

	private float currXChange = 0;
	private float currYChange = 0;
	private float currZChange = 0;

	public DigitModel(String path, Vector3f translate, Vector3f scale, Vector3f distFromOrigin) throws IOException {
		super(path, translate, scale, distFromOrigin);
	}

	public static DigitModel getModel(String s, Vector3f translate, Vector3f scale, Vector3f distFromOrigin) {
		try {
			return new DigitModel(s, translate, scale, distFromOrigin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateTranslate(Vector3f change) {
		float xChange = change.getX() - currXChange;
		currXChange = change.getX();
		float yChange = change.getY() - currYChange;
		currYChange = change.getY();
		float zChange = change.getZ() - currZChange;
		currZChange = change.getZ();

		this.translate = new Vector3f(translate.getX() - xChange, translate.getY() - yChange, translate.getZ() - zChange);
	}
}