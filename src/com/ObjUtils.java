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

	private final String OBJ_PATH = "/home/matt/Programming/Java/CS455/AllyRun/Objects/";
	private StatusUtils status = StatusUtils.getInstance();

	public Model sky, you;
	public DigitModel zero, one, two, three, four, five, six, seven, eight, nine;
	public RepeatingModel bridge, ocean;

	public Texture blackTexture, redTexture, skyTexture, youTexture, bridgeTexture, oceanTexture;

	public void loadModels(ArrayList<Model> models, ArrayList<Model> carParts) {

//		LOAD TEXTURES FIRST
		blackTexture = loadTexture("JPG", "black.jpg");
		redTexture = loadTexture("JPG", "red.jpg");
		youTexture = loadTexture("JPG", "you.jpg");
		bridgeTexture = loadTexture("JPG", Game.LEVEL + "pier.jpg");
		oceanTexture = loadTexture("JPG", Game.LEVEL + "ocean.jpg");
		skyTexture = loadTexture("JPG", Game.LEVEL + "sky.jpg");

//		LOAD OJB's
//		enemy = Model.getModel(OBJ_PATH + "enemy.obj",
//				new Vector3f(3.0f, 0.0f, 0.0f), new Vector3f(4f, 4f, 4f), new Vector3f(0f, 0f, -4f));
//		models.add(enemy);
//		carParts.add(enemy);
//		enemy.setIsStationary(0f);

//		ship = Model.getModel(OBJ_PATH + "Ship.obj",
//				new Vector3f(3.0f, 0.0f, 0.0f), new Vector3f(4f, 4f, 4f), new Vector3f(0f, 0f, -4f));
//		models.add(ship);
//		carParts.add(ship);
//		ship.setIsStationary(0f);

		you = Model.getModel(OBJ_PATH + "you.obj",
				new Vector3f(0.0f, -0.3f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		models.add(you);
		carParts.add(you);
		you.setIsStationary(0f);

		bridge = (RepeatingModel) RepeatingModel.getModel(OBJ_PATH + "pier.obj",
				new Vector3f(0.0f, -1.0f, -2.0f), new Vector3f(0.01f, 0.01f, 0.01f), new Vector3f(0f, 0f, -20f));
		models.add(bridge);
		bridge.setIsStationary(90f);
		carParts.add(bridge);

		sky = Model.getModel(OBJ_PATH + "sky.obj",
				new Vector3f(-5.0f, -15.0f, -2.0f), new Vector3f(4f, 1f, 1f), new Vector3f(-10f, -20f, -20f));
		models.add(sky);
		carParts.add(sky);

		ocean = (RepeatingModel) RepeatingModel.getModel(OBJ_PATH + "ocean.obj",
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
		handleScore(true);
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
		handleScore(false);
	}


	public void displayLoadScreen() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		Model loading = Model.getModel(OBJ_PATH + "loading.obj",
				new Vector3f(-14.0f, 0.0f, -20.0f), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(0f, 0f, -15f));
		loading.setIsStationary(90f);

		loading.render(true);
		Display.update();
		Display.sync(Game.FRAMERATE);
	}

	//	public void handleScore(int score) {
	public void handleScore(boolean initialLoad) {
		if (initialLoad) loadDigits();

		String strScore = String.valueOf(status.score);
		while (strScore.length() < 4) {
			strScore = '0' + strScore;
		}

		for (int i = 0; i < strScore.length(); i++) {
			char digit = strScore.charAt(3 - i);
			switch (digit) {
				case '0':
					zero.render(i);
					break;
				case '1':
					one.render(i);
					break;
				case '2':
					two.render(i);
					break;
				case '3':
					three.render(i);
					break;
				case '4':
					four.render(i);
					break;
				case '5':
					five.render(i);
					break;
				case '6':
					six.render(i);
					break;
				case '7':
					seven.render(i);
					break;
				case '8':
					eight.render(i);
					break;
				case '9':
					nine.render(i);
					break;
				default:
					break;
			}
		}
	}

	private void loadDigits() {
		zero = DigitModel.getModel(OBJ_PATH + "zero.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
		one = DigitModel.getModel(OBJ_PATH + "one.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
		two = DigitModel.getModel(OBJ_PATH + "two.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
		three = DigitModel.getModel(OBJ_PATH + "three.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
		four = DigitModel.getModel(OBJ_PATH + "four.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
		five = DigitModel.getModel(OBJ_PATH + "five.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
		six = DigitModel.getModel(OBJ_PATH + "six.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
		seven = DigitModel.getModel(OBJ_PATH + "seven.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
		eight = DigitModel.getModel(OBJ_PATH + "eight.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
		nine = DigitModel.getModel(OBJ_PATH + "nine.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
	}

	public void moveScoreLocation(float change) {
		zero.updateTranslate(new Vector3f(0, 0, -change));
	}
}
