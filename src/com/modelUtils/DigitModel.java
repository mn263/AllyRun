package com.modelUtils;

import com.renderUtils.*;
import org.lwjgl.util.vector.*;

import javax.media.opengl.*;
import java.io.*;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class DigitModel extends Model {

	private static final float DIGIT_DIST = 0.6f;
	ArrayList<ArrayList<RenderData>> locations = null;

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

	public void render(int index) {
		if (locations == null) { // load all of the data and add to list
			loadDataLocations();
		}
		glBegin(GL.GL_TRIANGLES);
		ArrayList<RenderData> dataList = locations.get(index);
		for (RenderData renderData : dataList) {
			displayRenderData(0, renderData);
			displayRenderData(1, renderData);
			displayRenderData(2, renderData);
		}
		glEnd();
	}

	private void loadDataLocations() {
		locations = new ArrayList<>();
		Vector3f oldTran = getTranslate();
		for (int i = 0; i < 4; i++) {
			setTranslate(oldTran.getX() - (i * DIGIT_DIST),
					oldTran.getY(), oldTran.getZ());
			updateRenderData();
			locations.add(renderDataList);
		}
	}

	public void updateTranslate(Vector3f newT) {
		this.translate = new Vector3f(translate.getX() + newT.getX() + (3 * DIGIT_DIST), translate.getY() + newT.getY(), translate.getZ() + newT.getZ());
		loadDataLocations();
	}
}