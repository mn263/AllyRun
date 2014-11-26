package com.modelUtils;

import com.*;
import com.renderUtils.*;
import javafx.geometry.*;
import org.lwjgl.util.vector.*;

import javax.media.opengl.*;
import java.io.*;
import java.util.*;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;

public class FoodModel extends Model {

	private ArrayList<ArrayList<RenderData>> foodInstances = null;
	private ArrayList<BoundingBox> boundingBoxes = null;
	//	TODO: make repeat num based off of the current level
	private final int repeatNumber = 10;
	private float xRange = 7 * InputController.STRAFE_DIFF;


	public FoodModel(String path, Vector3f translate, Vector3f scale, Vector3f distFromOrigin) throws IOException {
		super(path, translate, scale, distFromOrigin);
	}

	public static FoodModel getModel(String s, Vector3f translate, Vector3f scale, Vector3f distFromOrigin) {
		try {
			return new FoodModel(s, translate, scale, distFromOrigin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void render() {
		if (this.renderDataList == null) {
			// load all of the data and add to list
			foodInstances = new ArrayList<>();
			boundingBoxes = new ArrayList<>();

			Vector3f oldTran = getTranslate();
			float x = oldTran.getX();
			float y = oldTran.getY();
			float z = -2;
			for (int i = 0; i < repeatNumber; i++) {

				Random rn = new Random();
				float xMovement = rn.nextFloat() * xRange;
				if(rn.nextBoolean()) xMovement = xMovement * (-1);
				float zMovement = rn.nextFloat() * 40;

				setTranslate(x + xMovement, y, z - zMovement);
				super.render(true); // Food location doesn't move with screen
				foodInstances.add(renderDataList);
				boundingBoxes.add(boundingBox);
			}
		} else { // simply display
			glBegin(GL.GL_TRIANGLES);
			for (ArrayList<RenderData> dataList : foodInstances) {
				for (RenderData renderData : dataList) {
					displayRenderData(0, renderData);
					displayRenderData(1, renderData);
					displayRenderData(2, renderData);
				}
			}
			glEnd();
		}
	}

	public boolean intersects(BoundingBox intersectingBox) {
		for (BoundingBox box : boundingBoxes) {
			if (box.intersects(intersectingBox)) return true;
		}
		return false;
	}
}
