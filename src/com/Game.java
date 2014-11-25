package com;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;
import org.newdawn.slick.opengl.*;

import java.io.*;
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
	private static String TITLE = "Pier Run!";
	private static float GAME_SPEED = 0.05f;
	private static final int FRAMERATE = 60;
	private static final int CANVAS_WIDTH = 800;  // width of the drawable
	private static final int CANVAS_HEIGHT = 600; // height of the drawable

	private static ArrayList<Model> models = new ArrayList<>();
	private static ArrayList<Model> carParts = new ArrayList<>();

	public static int gameTime = 0;

	private Model ship;
	private RepeatingModel bridge;
	private RepeatingModel ocean;
	private Texture shipTexture;
	private Texture bridgeTexture;
	private Texture oceanTexture;


	public Game() {
		try {
			Display.setDisplayMode(new DisplayMode(CANVAS_WIDTH, CANVAS_HEIGHT));
			Display.setTitle(TITLE);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		loadModels();


		//Initialization code OpenGL
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glShadeModel(GL_SMOOTH);

		reshape(CANVAS_WIDTH, CANVAS_HEIGHT);

//		gluLookAt();
		display();
	}


	private void loadModels() {

//		LOAD TEXTURES FIRST
		shipTexture = loadTexture("JPG", "sh3.jpg");
		bridgeTexture = loadTexture("JPG", "pier.jpg");
		oceanTexture = loadTexture("JPG", "ocean.jpg");

//		LOAD OJB's
		String objPath = "/home/matt/Programming/Java/CS455/AllyRun/Objects/";
		ship = Model.getModel(objPath + "Ship.obj",
				new Vector3f(3.0f, 3.0f, 0.0f), new Vector3f(4f, 4f, 4f), new Vector3f(0f, 0f, -4f));
		models.add(ship);
		carParts.add(ship);

		bridge = (RepeatingModel) RepeatingModel.getModel(objPath + "pier.obj",
				new Vector3f(0.0f, 4.0f, -2.0f), new Vector3f(0.01f, 0.01f, 0.01f), new Vector3f(0f, 0f, -20f));
		models.add(bridge);
		bridge.setIsStationary(90f);
		carParts.add(bridge);

		ocean = (RepeatingModel) RepeatingModel.getModel(objPath + "ocean.obj",
				new Vector3f(0.0f, -7.0f, -2.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -20f));
		models.add(ocean);
		ocean.setIsStationary(90f);
		carParts.add(ocean);


		for (Model model : models) {
			model.updateTranslate(new Vector3f(0.0f, -5f, 0f));
		}
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
//		TODO: display loading message when appropriate
//		TODO: when done loading display "press spacebar" message
//		while (!Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//			System.out.println("Press Space Bar");
//		}

		initialModelLoad();

		while (!Display.isCloseRequested()) {
			Game.gameTime += GAME_SPEED;
			GL11.glTranslatef(0, 0, GAME_SPEED);
			checkForInput();

			doAnyModelUpdating();

			Display.update();
			Display.sync(FRAMERATE);
		}

		Display.destroy();
	}

	private void initialModelLoad() {
//		TODO: find a way to make the background above use an image of the sky that never changes.
		shipTexture.bind();
		ship.render(true);

		bridgeTexture.bind();
		bridge.render(10, 5, true);

		oceanTexture.bind();
		ocean.render(2, 100, true);
	}

	private void doAnyModelUpdating() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		if (InputTracker.viewChanged()) {
			// TODO: handle view changing
			InputTracker.changeView();
			System.out.println("view changing");
		}
		if (InputTracker.moveLeft()) {
			// TODO: handle moving left
			System.out.println("moving left");
		}
		if (InputTracker.moveRight()) {
			// TODO: handle moving right
			System.out.println("moving right");
		}
		if (InputTracker.duck()) {
			// TODO: handle ducking
			System.out.println("ducking");
		}
		if (InputTracker.jump()) {
//			TODO: handle jumping
			System.out.println("jumped");
			InputTracker.upReleased(); // They can't just jump off of the air
		}


		//Render
		shipTexture.bind();
		ship.render(false);
//
		bridgeTexture.bind();
		bridge.render(10, 5, false);

		oceanTexture.bind();
		ocean.render(2, 80, false);
	}

	public void checkForInput() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD8) InputTracker.upPressed();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD4) InputTracker.leftPressed();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD6) InputTracker.rightPressed();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD5 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD2) InputTracker.downPressed();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD0) InputTracker.changeView();
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD8) InputTracker.upReleased();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD4) InputTracker.leftReleased();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD6) InputTracker.rightReleased();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD5 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD2) InputTracker.downReleased();
			}
		}
	}


	public static void main(String[] args) {
		new Game();
	}
}