package com;

import com.modelUtils.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;
import org.newdawn.slick.opengl.*;

import java.io.*;
import java.util.*;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class ObjUtils {
	private static ObjUtils ourInstance = new ObjUtils();

	public static ObjUtils getInstance() {
		return ourInstance;
	}

	public final String OBJ_PATH = "/home/matt/Programming/Java/CS455/AllyRun/Objects/";
	private StatusUtils status = StatusUtils.getInstance();

	private Score score0, score1, score2, score3;

	public Model sky, you;
	public RepeatingModel bridge, ocean;

	public Texture blackTexture, redTexture, skyTexture, youTexture, bridgeTexture, oceanTexture;

	public void loadModels(ArrayList<Model> models, ArrayList<Model> carParts) {
//		LOAD SCORE DIGITS
		score0 = new Score(0);
		score1 = new Score(1);
		score2 = new Score(2);
		score3 = new Score(3);

//		LOAD TEXTURES FIRST
		blackTexture = loadTexture("JPG", "misc/black.jpg");
		redTexture = loadTexture("JPG", "misc/red.jpg");
//		youTexture = loadTexture("JPG", "food/food1.jpg");
		youTexture = loadTexture("JPG", "character/you.jpg");
		bridgeTexture = loadTexture("JPG", Game.LEVEL + "scene/pier.jpg");
		oceanTexture = loadTexture("JPG", Game.LEVEL + "scene/ocean.jpg");
		skyTexture = loadTexture("JPG", Game.LEVEL + "scene/sky.jpg");

//		LOAD OJB's
//		enemy = Model.getModel(OBJ_PATH + "enemy.obj",
//				new Vector3f(3.0f, 0.0f, 0.0f), new Vector3f(4f, 4f, 4f), new Vector3f(0f, 0f, -4f));
//		models.add(enemy);
//		carParts.add(enemy);
//		enemy.setIsStationary(0f);


//		you = Model.getModel(OBJ_PATH + "food/food1.obj",
//				new Vector3f(0.0f, -0.3f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
//		models.add(you);
//		carParts.add(you);
//		you.setIsStationary(0f);

		you = Model.getModel(OBJ_PATH + "character/you.obj",
				new Vector3f(0.0f, -0.3f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		models.add(you);
		carParts.add(you);
		you.setIsStationary(0f);

		bridge = (RepeatingModel) RepeatingModel.getModel(OBJ_PATH + "scene/pier.obj",
				new Vector3f(0.0f, -1.0f, -2.0f), new Vector3f(0.01f, 0.01f, 0.01f), new Vector3f(0f, 0f, -20f));
		models.add(bridge);
		bridge.setIsStationary(90f);
		carParts.add(bridge);

		sky = Model.getModel(OBJ_PATH + "scene/sky.obj",
				new Vector3f(-5.0f, -15.0f, -2.0f), new Vector3f(4f, 1f, 1f), new Vector3f(-10f, -20f, -20f));
		models.add(sky);
		carParts.add(sky);

		ocean = (RepeatingModel) RepeatingModel.getModel(OBJ_PATH + "scene/ocean.obj",
				new Vector3f(0.0f, -12.0f, -2.0f), new Vector3f(2f, 1f, 1f), new Vector3f(0f, 0f, -20f));
		models.add(ocean);
		ocean.setIsStationary(90f);
		carParts.add(ocean);
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



	public void initialModelLoad() {

		youTexture.bind();
		you.render(true);

		bridgeTexture.bind();
		bridge.render(10, 5, true);

		oceanTexture.bind();
		ocean.render(2, 70, true);

		skyTexture.bind();
		sky.render(true);

		blackTexture.bind();
		handleScore();
	}


	public void doAnyModelUpdating() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		youTexture.bind();
		you.render(true);

		bridgeTexture.bind();
		bridge.render(10, 5, false);

		oceanTexture.bind();
		ocean.render(2, 20, false);

		skyTexture.bind();
		sky.render(false);

		blackTexture.bind();
		handleScore();
	}


	public void displayLoadScreen() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		Model loading = Model.getModel(OBJ_PATH + "info/loading.obj",
				new Vector3f(-14.0f, 0.0f, -20.0f), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(0f, 0f, -15f));
		loading.setIsStationary(90f);

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
