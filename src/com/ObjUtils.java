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

	private Score score0, score1, score2, score3, hScore0, hScore1, hScore2, hScore3;

	public Model infoBox, sky, you, mike;
	public RepeatingModel bridge, ocean;
	public FoodModel food1, food2, food3;
	public FoodModel enemy1, enemy2;

	public Texture newGameTexture, highScoreTexture, bonusRoundTexture, gameOverTexture, mikeTexture, blackTexture, redTexture, skyTexture, youTexture, bridgeTexture, oceanTexture,
			food1Texture, food2Texture, food3Texture, enemy1Texture, enemy2Texture;

	public void loadModels() {
//		LOAD TEXTURES FIRST
		// character
		youTexture = loadTexture("JPG", "character/you.jpg");
		mikeTexture = loadTexture("JPG", "character/mike.jpg");
		// scene
		loadSceneTextures();
		// food
		food1Texture = loadTexture("JPG", "food/food1.jpg");
		food2Texture = loadTexture("JPG", "food/food2.jpg");
		food3Texture = loadTexture("JPG", "food/food3.jpg");
		// enemy
		enemy1Texture = loadTexture("TGA", "enemy/enemy1.tga");
		enemy2Texture = loadTexture("JPG", "enemy/enemy2.jpg");
		// misc
		blackTexture = loadTexture("JPG", "misc/black.jpg");
		redTexture = loadTexture("JPG", "misc/red.jpg");
		gameOverTexture = loadTexture("JPG", "info/gameover.jpg");
		highScoreTexture = loadTexture("JPG", "info/highscore.jpg");
		bonusRoundTexture = loadTexture("JPG", "info/bonusround.jpg");
		newGameTexture = loadTexture("JPG", "info/newgame.jpg");

//		SCENE
		bridge = (RepeatingModel) RepeatingModel.getModel(OBJ_PATH + "scene/pier.obj", new Vector3f(0.0f, -1.0f, -2.0f), new Vector3f(0.01f, 0.01f, 0.01f), new Vector3f(0f, 0f, -20f));
		bridge.setNeededRotation(90f);
		sky = Model.getModel(OBJ_PATH + "scene/sky.obj", new Vector3f(-5.0f, -15.0f, -2.0f), new Vector3f(4f, 1f, 1f), new Vector3f(-10f, -20f, -20f));
		ocean = (RepeatingModel) RepeatingModel.getModel(OBJ_PATH + "scene/ocean.obj", new Vector3f(0.0f, -12.0f, -2.0f), new Vector3f(2f, 1f, 1f), new Vector3f(0f, 0f, -20f));
		ocean.setNeededRotation(90f);

		loadCharacters();

//		MISC
		infoBox = Model.getModel(OBJ_PATH + "info/continue.obj",
				new Vector3f(0.8f, 0.2f, -0.5f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		infoBox.setNeededRotation(90f);
		infoBox.render(true);

	}

	public void loadSceneTextures() {
		bridgeTexture = loadTexture("JPG", "scene/" + Game.LEVEL + "pier.jpg");
		oceanTexture = loadTexture("JPG", "scene/" + Game.LEVEL + "ocean.jpg");
		skyTexture = loadTexture("JPG", "scene/" + Game.LEVEL + "sky.jpg");
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
//		ENEMY
		enemy1Texture.bind();
		enemy1.render();
		enemy2Texture.bind();
		enemy2.render();
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

		String strScore = String.valueOf(status.getScore());
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



		strScore = String.valueOf(status.getHighScore());
		while (strScore.length() < 4) {
			strScore = '0' + strScore;
		}

		for (int i = 0; i < strScore.length(); i++) {
			String digit = String.valueOf(strScore.charAt(3 - i));
			switch (i) {
				case 0: hScore0.updateValue(Integer.valueOf(digit));
					break;
				case 1: hScore1.updateValue(Integer.valueOf(digit));
					break;
				case 2: hScore2.updateValue(Integer.valueOf(digit));
					break;
				case 3: hScore3.updateValue(Integer.valueOf(digit));
					break;
			}
		}
	}

	public void moveScoreLocation(Vector3f change) {
		score0.adjustWithScreen(change);
		score1.adjustWithScreen(change);
		score2.adjustWithScreen(change);
		score3.adjustWithScreen(change);
		hScore0.adjustWithScreen(change);
		hScore1.adjustWithScreen(change);
		hScore2.adjustWithScreen(change);
		hScore3.adjustWithScreen(change);
	}

	public void displayInfoScreen() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		if (Game.gameStatus == Game.GAME_STATUS.newgame) {
			newGameTexture.bind();
		} else if (Game.gameStatus == Game.GAME_STATUS.endgame) {
			gameOverTexture.bind();
		} else if (Game.gameStatus == Game.GAME_STATUS.highscore) {
			highScoreTexture.bind();
		} else if (Game.gameStatus == Game.GAME_STATUS.bonus) {
			bonusRoundTexture.bind();
		}

		infoBox.render(true);
	}

	public void loadCharacters() {

//		CHARACTERS
		you = Model.getModel(OBJ_PATH + "character/you.obj",
				new Vector3f(0.0f, -0.28f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		mike = FoodModel.getModel(1, OBJ_PATH + "character/mike.obj",
				new Vector3f(-0.3f, -0.15f, 1.0f), new Vector3f(4f, 4f, 4f), new Vector3f(1f, 3f, -9f));
		mike.setNeededRotation(25f);

//		FOOD
		food1 = FoodModel.getModel(20, OBJ_PATH + "food/food1.obj", new Vector3f(0.0f, -0.28f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		food2 = FoodModel.getModel(15, OBJ_PATH + "food/food2.obj", new Vector3f(0.0f, -0.28f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		food3 = FoodModel.getModel(5, OBJ_PATH + "food/food3.obj", new Vector3f(0.0f, -0.28f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));

//		ENEMY
		enemy1 = FoodModel.getModel(20, OBJ_PATH + "enemy/enemy1.obj", new Vector3f(0.0f, -0.28f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		enemy2 = FoodModel.getModel(15, OBJ_PATH + "enemy/enemy2.obj", new Vector3f(0.0f, -0.28f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		enemy2.setNeededRotation(20f);

//		SCORE
		score0 = new Score(false, 0);
		score1 = new Score(false, 1);
		score2 = new Score(false, 2);
		score3 = new Score(false, 3);
		hScore0 = new Score(true, 0);
		hScore1 = new Score(true, 1);
		hScore2 = new Score(true, 2);
		hScore3 = new Score(true, 3);

	}
}
