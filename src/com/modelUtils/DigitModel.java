package com.modelUtils;

import org.lwjgl.util.vector.*;

import java.io.*;

public class DigitModel extends Model {

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

	public void updateTranslate(float change) {
		float zChange = change - currZChange;
		currZChange = change;

		this.translate = new Vector3f(translate.getX(), translate.getY(),
				translate.getZ() - zChange);
	}
}