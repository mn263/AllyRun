package com;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.*;

public class Game {


	public static final String TITLE = "Pack Rat!";
	public static final int FRAMERATE = 100;
	public static final int CANVAS_WIDTH = 800;  // width of the drawable
	public static final int CANVAS_HEIGHT = 600; // height of the drawable
	private static boolean PLAY = false;
	public static enum GAME_STATUS { newgame, endgame, highscore, bonus }
	public static GAME_STATUS gameStatus = GAME_STATUS.newgame;
	public static float GAME_SPEED = 0.075f;
	public static String LEVEL = "";

	public static float gameTime = 0;
	private ObjUtils m;
	private StatusUtils status;


	public Game() {
		try {
			Display.setDisplayMode(new DisplayMode(CANVAS_WIDTH, CANVAS_HEIGHT));
			Display.setTitle(TITLE);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		//Initialization code OpenGL
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glShadeModel(GL_SMOOTH);
		reshape(CANVAS_WIDTH, CANVAS_HEIGHT);

		startGame();
	}

	private void startGame() {
		m = ObjUtils.getInstance();
		status = StatusUtils.getInstance();
		status.game = this;
		m.displayLoadScreen();
		m.loadModels();
		display();
	}


	public void reshape(int width, int height) {
		if (height == 0) height = 1;   // prevent divide by zero
		float aspect = (float) width / height;

		// Set the view port (display area) to cover the entire window
		glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		glMatrixMode(GL_PROJECTION);  // choose projection matrix
		glLoadIdentity();             // reset projection matrix
		org.lwjgl.util.glu.GLU.gluPerspective(45.0f, aspect, 0.1f, 400.0f); // fovy, aspect, zNear, zFar

		// Enable the modelUtils-view transform
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity(); // reset
	}

	private void display() {
		m.updateModels(true);
		while (!Display.isCloseRequested()) {
			InputController.checkForInput();
			if (isPlaying()) {
				Game.gameTime += GAME_SPEED;
				if (Game.gameTime > 47.5) {
					endGame();
				} else {
					status.updateStatus(m);
					status.updateScreenLocation(new Vector3f(0, 0, GAME_SPEED));
					m.updateModels(false);
				}
			} else {
				m.updateModels(true);
				m.displayInfoScreen();
			}
			Display.update();
			Display.sync(FRAMERATE);
		}
		Display.destroy();
	}

	public void endGame() {
		if(!isPlaying()) return;
		endPlay();

		if (LEVEL.equals("") && status.getScore() > 150) {
			gameStatus = GAME_STATUS.bonus;
			status.reset();
			LEVEL = "alien_";
			GAME_SPEED = GAME_SPEED * 2;
			m.loadSceneTextures();
			return;
		} else {
			LEVEL = "";
			GAME_SPEED = GAME_SPEED / 2;
			m.loadSceneTextures();

			if (status.getScore() > status.getHighScore()) {
				gameStatus = GAME_STATUS.highscore;
				status.setHighScore(status.getScore());
			} else {
				gameStatus = GAME_STATUS.endgame;
			}
		}
		status.setScore(0);
		GAME_SPEED = GAME_SPEED * 1.3f;
		status.reset();
	}

	public static void endPlay() {
		PLAY = false;
	}

	public static void startPlay() {
		PLAY = true;
		Game.gameTime = 0;
	}

	public static boolean isPlaying() {
		return PLAY;
	}

	public static void main(String[] args) {
		new Game();
	}
}