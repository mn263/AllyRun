package com;

import com.modelUtils.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import java.util.*;

import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.*;

public class Game {
	public static final String TITLE = "Pier Run!";
	public static final int FRAMERATE = 60;
	public static final int CANVAS_WIDTH = 800;  // width of the drawable
	public static final int CANVAS_HEIGHT = 600; // height of the drawable
//	private static float GAME_SPEED = 0.0f;
	public static float GAME_SPEED = 0.05f;
//	public static String LEVEL = "alien_";
	public static String LEVEL = "";

	private static ArrayList<Model> models = new ArrayList<>();
	private static ArrayList<Model> carParts = new ArrayList<>();

	public static int gameTime = 0;
	private ObjUtils m;


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
		m.displayLoadScreen();
		m.loadModels(models, carParts);
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
		m.initialModelLoad();

		while (!Display.isCloseRequested()) {
//			TODO: add logic for loading
//			TODO: add logic for collision
			Game.gameTime += GAME_SPEED;
			GL11.glTranslatef(0, 0, GAME_SPEED);
			m.you.updateTranslate(new Vector3f(0, 0, -GAME_SPEED));
			InputController.checkForInput();

			m.doAnyModelUpdating();

			Display.update();
			Display.sync(FRAMERATE);
		}

		Display.destroy();
	}


	public static void main(String[] args) {
		new Game();
	}
}