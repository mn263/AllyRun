package com;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.*;

public class Game {


//	TODO LIST
//	TODO: move score so that it doesn't disappear
//	TODO: keep track of highest score so far
//	TODO: if the ---- score was passed allow for a bonus round
//	TODO: create info page while loading
//	TODO: dont start until space bar hit
//	TODO: allow for pausing by hitting the space bar
//	TODO: end game if an enemy was run into
//	TODO: Create game over screen, with play again option
//	TODO: create bonus round loading screen
//	TODO: if you are in a jump, don't allow side movement

//	TODO: implement "first-person" view

//	TODO: create a light source and somehow add shading????
//	TODO: add option for different difficulty levels (score should go up faster with higher difficulty--game_speed, # of enemies)


	public static final String TITLE = "Pier Run!";
	public static final int FRAMERATE = 60;
	public static final int CANVAS_WIDTH = 800;  // width of the drawable
	public static final int CANVAS_HEIGHT = 600; // height of the drawable
	public static float GAME_SPEED = 0.05f;
//	public static String LEVEL = "alien_";
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
			status.updateStatus(m);
			Game.gameTime += GAME_SPEED;
			if (Game.gameTime > 47.5) {
				status.changeLevel(1);
			} else {
				status.updateScreenLocation(GAME_SPEED);
				InputController.checkForInput();
				m.updateModels(false);
			}
			Display.update();
			Display.sync(FRAMERATE);
		}
		Display.destroy();
	}

	public static void main(String[] args) {
		new Game();
	}

	public static void endGame() {
//		TODO: implement this
		System.out.println("GAME ENDED, DID YOU WIN?");
	}
}