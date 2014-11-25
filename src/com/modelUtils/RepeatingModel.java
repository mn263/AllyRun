package com.modelUtils;

import com.renderUtils.*;
import org.lwjgl.util.vector.*;

import javax.media.opengl.*;
import java.io.*;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class RepeatingModel extends Model {

	ArrayList<ArrayList<RenderData>> bridgeInstances = null;

	public RepeatingModel(String path, Vector3f translate, Vector3f scale, Vector3f distFromOrigin) throws IOException {
		super(path, translate, scale, distFromOrigin);
	}

	public static Model getModel(String s, Vector3f translate, Vector3f scale, Vector3f distFromOrigin) {
		try {
			return new RepeatingModel(s, translate, scale, distFromOrigin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void render(int repeatNumber, float distance, boolean updateData) {
		if (this.renderDataList == null || updateData) {
			// load all of the data and add to list
			bridgeInstances = new ArrayList<>();
			Vector3f oldTran = getTranslate();
			float x = oldTran.getX();
			float y = oldTran.getY();
			float z = -2;
			for (int i = 0; i < repeatNumber; i++) {
				setTranslate(x, y, z - (distance * i));
				super.render(updateData);
				bridgeInstances.add(renderDataList);
			}
		} else { // simply display
			glBegin(GL.GL_TRIANGLES);
			for (ArrayList<RenderData> dataList : bridgeInstances) {
				for (RenderData renderData : dataList) {
					displayRenderData(0, renderData);
					displayRenderData(1, renderData);
					displayRenderData(2, renderData);
				}
			}
			glEnd();
		}
	}
}
