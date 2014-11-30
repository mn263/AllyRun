package com;

import com.modelUtils.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;
import org.newdawn.slick.opengl.*;

import java.io.*;

import static org.lwjgl.opengl.GL11.*;

public class ObjUtils {
	private static ObjUtils ourInstance = new ObjUtils();

	public static ObjUtils getInstance() {
		return ourInstance;
	}

	public final String OBJ_PATH = "/home/matt/Programming/Java/CS455/AllyRun/Objects/";
	private StatusUtils status = StatusUtils.getInstance();

	private Score score0, score1, score2, score3;

	public Model sky, you, mike;
	public RepeatingModel bridge, ocean;
	public FoodModel food1, food2, food3;

	public Texture mikeTexture, blackTexture, redTexture, skyTexture, youTexture, bridgeTexture, oceanTexture,
			food1Texture, food2Texture, food3Texture;

	public void loadModels() {
//		LOAD TEXTURES FIRST
		// character
		youTexture = loadTexture("JPG", "character/you.jpg");
		mikeTexture = loadTexture("JPG", "character/mike.jpg");
		// scene
		bridgeTexture = loadTexture("JPG", Game.LEVEL + "scene/pier.jpg");
		oceanTexture = loadTexture("JPG", Game.LEVEL + "scene/ocean.jpg");
		skyTexture = loadTexture("JPG", Game.LEVEL + "scene/sky.jpg");
		// food
		food1Texture = loadTexture("JPG", "food/food1.jpg");
		food2Texture = loadTexture("JPG", "food/food2.jpg");
		food3Texture = loadTexture("JPG", "food/food3.jpg");
		// misc
		blackTexture = loadTexture("JPG", "misc/black.jpg");
		redTexture = loadTexture("JPG", "misc/red.jpg");

//		CHARACTERS
		you = Model.getModel(OBJ_PATH + "character/you.obj",
				new Vector3f(0.0f, -0.3f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		mike = FoodModel.getModel(1, OBJ_PATH + "character/mike.obj",
				new Vector3f(-0.3f, -0.15f, 1.0f), new Vector3f(4f, 4f, 4f), new Vector3f(1f, 3f, -9f));
		mike.setNeededRotation(25f);

//		SCENE
		bridge = (RepeatingModel) RepeatingModel.getModel(OBJ_PATH + "scene/pier.obj", new Vector3f(0.0f, -1.0f, -2.0f), new Vector3f(0.01f, 0.01f, 0.01f), new Vector3f(0f, 0f, -20f));
		bridge.setNeededRotation(90f);
		sky = Model.getModel(OBJ_PATH + "scene/sky.obj", new Vector3f(-5.0f, -15.0f, -2.0f), new Vector3f(4f, 1f, 1f), new Vector3f(-10f, -20f, -20f));
		ocean = (RepeatingModel) RepeatingModel.getModel(OBJ_PATH + "scene/ocean.obj", new Vector3f(0.0f, -12.0f, -2.0f), new Vector3f(2f, 1f, 1f), new Vector3f(0f, 0f, -20f));
		ocean.setNeededRotation(90f);

//		FOOD
		food1 = FoodModel.getModel(10, OBJ_PATH + "food/food1.obj", new Vector3f(0.0f, -0.28f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		food2 = FoodModel.getModel(10, OBJ_PATH + "food/food2.obj", new Vector3f(0.0f, -0.28f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		food3 = FoodModel.getModel(10, OBJ_PATH + "food/food3.obj", new Vector3f(0.0f, -0.28f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));

//		SCORE
		score0 = new Score(0);
		score1 = new Score(1);
		score2 = new Score(2);
		score3 = new Score(3);

//		MISC
	}

	private Texture loadTexture(String imgType, String key) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture(imgType, new FileInputStream(new File("/home/matt/Programming/Java/CS455/AllyRun/Textures/" + key)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture;
	}


	public void updateModels(boolean initialLoad) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//		CHARACTERS
		youTexture.bind();
		you.render(true);
		mikeTexture.bind();
		mike.render(initialLoad);
//		SCENE
		bridgeTexture.bind();
		bridge.render(10, 5, initialLoad);
		oceanTexture.bind();
		ocean.render(2, 70, initialLoad);
		skyTexture.bind();
		sky.render(initialLoad);
//		FOOD
		food1Texture.bind();
		food1.render();
		food2Texture.bind();
		food2.render();
		food3Texture.bind();
		food3.render();
//		SCORE
		blackTexture.bind();
		handleScore();
//		MISC
	}


	public void displayLoadScreen() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		Model loading = Model.getModel(OBJ_PATH + "info/loading.obj",
				new Vector3f(-14.0f, 0.0f, -20.0f), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(0f, 0f, -15f));
		loading.setNeededRotation(90f);

		loading.render(true);
		Display.update();
		Display.sync(Game.FRAMERATE);
	}

	public void handleScore() {

		String strScore = String.valueOf(status.score);
		while (strScore.length() < 4) {
			strScore = '0' + strScore;
		}

		for (int i = 0; i < strScore.length(); i++) {
			String digit = String.valueOf(strScore.charAt(3 - i));
			switch (i) {
				case 0: score0.updateValue(Integer.valueOf(digit));
					break;
				case 1: score1.updateValue(Integer.valueOf(digit));
					break;
				case 2: score2.updateValue(Integer.valueOf(digit));
					break;
				case 3: score3.updateValue(Integer.valueOf(digit));
					break;
			}
		}
	}

	public void moveScoreLocation(float change) {
		score0.adjustWithScreen(change);
		score1.adjustWithScreen(change);
		score2.adjustWithScreen(change);
		score3.adjustWithScreen(change);
	}
}
