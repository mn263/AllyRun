package sample;

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
	private static final int FRAMERATE = 60;
	private static final int CANVAS_WIDTH = 800;  // width of the drawable
	private static final int CANVAS_HEIGHT = 600; // height of the drawable

	private static ArrayList<Model> models = new ArrayList<>();
	private static ArrayList<Model> carParts = new ArrayList<>();

	public static float gameTime = -2.0f;

	private Model ship;
	private RepeatingModel bridge;
	private Texture shipTexture;
	private Texture bridgeTexture;



	public Game() {
		try{
			Display.setDisplayMode(new DisplayMode(CANVAS_WIDTH,CANVAS_HEIGHT));
			Display.setTitle(TITLE);
			Display.create();
		} catch(LWJGLException e){
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
		bridgeTexture = loadTexture("JPG", "trench.jpg");

//		LOAD OJB's
		String objPath = "/home/matt/Programming/Java/CS455/AllyRun/Objects/";
		ship = Model.getModel(objPath + "Ship.obj",
				new Vector3f(3.0f, 3.0f, 0.0f), new Vector3f(4f, 4f, 4f), new Vector3f(0f, 0f, -4f));
		models.add(ship);
		carParts.add(ship);

		bridge = (RepeatingModel) RepeatingModel.getModel(objPath + "trench.obj",
				new Vector3f(0.0f, 4.0f, -2.0f), new Vector3f(0.01f, 0.01f, 0.01f), new Vector3f(0f, 0f, -20f));
		models.add(bridge);
		bridge.setIsStationary(90f);
		carParts.add(bridge);

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
		float aspect = (float)width / height;

		// Set the view port (display area) to cover the entire window
		glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		glMatrixMode(GL_PROJECTION);  // choose projection matrix
		glLoadIdentity();             // reset projection matrix
		org.lwjgl.util.glu.GLU.gluPerspective(45.0f, aspect, 0.1f, 400.0f); // fovy, aspect, zNear, zFar

		// Enable the model-view transform
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity(); // reset
	}

	private void display() {
		boolean hasDisplayed = false;
		while (!Display.isCloseRequested()) {
			Game.gameTime -= 0.1;
			GL11.glTranslatef(0, 0, 0.01f);
			if (hasDisplayed) getInput();
			else hasDisplayed = true;

			doAnyModelUpdating();

			Display.update();
			Display.sync(FRAMERATE);
		}

		Display.destroy();
	}

	private void doAnyModelUpdating() {
		//Render
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//		System.out.println(gameTime);
		if (gameTime >= -2.2) {
			shipTexture.bind();
			ship.render(true);

			bridgeTexture.bind();
			drawNeverEndingBridge(true);
		} else {
			shipTexture.bind();
			ship.render(false);

			bridgeTexture.bind();
			drawNeverEndingBridge(false);
		}
	}

	private void drawNeverEndingBridge(boolean recalculate) {
		Vector3f oldTran = bridge.getTranslate();
		float x = oldTran.getX();
		float y = oldTran.getY();
		float z = -2;
//		for (int i = 0; i < 10; i++) {
//			bridge.setTranslate(x, y, z - (5 * i));
//			System.out.println(z - (5 * i));
			bridge.render(recalculate);
//		}
	}


	public void getInput() {

		if (Mouse.isButtonDown(0)) {
			int x = Mouse.getX();
			int y = Mouse.getY();

			System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			System.out.println("SPACE KEY IS DOWN");
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
					System.out.println("A Key Pressed");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_F) {
					for (Model model : models) {
						model.updateTranslate(new Vector3f(-1f, 0f, 0f));
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_S) {
					for (Model model : models) {
						model.updateTranslate(new Vector3f(1f, 0f, 0f));
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_E) {
					for (Model model : models) {
						model.updateTranslate(new Vector3f(0.0f, -1f, 0f));
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D) {
					for (Model model : models) {
						model.updateTranslate(new Vector3f(0f, 1f, 0f));
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_Q) {
					for (Model model : models) {
						model.updateTranslate(new Vector3f(0f, 0f, 2f));
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
					for (Model model : models) {
						model.updateTranslate(new Vector3f(0.0f, 0f, -2f));
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_6) {
					for (Model model : models) {
						model.updateTireRotation(-5f);
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_4) {
					for (Model model : models) {
						model.updateTireRotation(5f);
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_T) {
					for (Model model : models) {
						model.updateHorizontalRotate(-5f);
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_G) {
					for (Model model : models) {
						model.updateHorizontalRotate(5f);
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_R) {
					for (Model model : models) {
						model.updateVerticalRotate(5f);
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_W) {
					for (Model model : models) {
						model.updateVerticalRotate(-5f);
					}
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_9 || Keyboard.getEventKey() == Keyboard.KEY_8 ||
						Keyboard.getEventKey() == Keyboard.KEY_7 || Keyboard.getEventKey() == Keyboard.KEY_3 ||
						Keyboard.getEventKey() == Keyboard.KEY_2 || Keyboard.getEventKey() == Keyboard.KEY_1) {

//					Vector3f cV = new Vector3f(0, 0, 4);
//					Vector3f tireV = null;
//					float degrees = 5;
//					if (Keyboard.getEventKey() == Keyboard.KEY_9) {
//						degrees = degrees*(-1);
//						tireV = car.driveForward(degrees, "right", cV);
//					} else if (Keyboard.getEventKey() == Keyboard.KEY_3) {
//						tireV = car.driveReverse(degrees, "right", cV);
//					} else if (Keyboard.getEventKey() == Keyboard.KEY_7) {
//						tireV = car.driveForward(degrees, "left", cV);
//					} else if (Keyboard.getEventKey() == Keyboard.KEY_1) {
//						degrees = degrees*(-1);
//						tireV = car.driveReverse(degrees, "left", cV);
//					} else if (Keyboard.getEventKey() == Keyboard.KEY_8) {
//						float angleDiff = tireOne.getObjectAngle() - car.getObjectAngle();
//						int cutoff = 9;
//						if (Math.abs(angleDiff) < cutoff) {
//							degrees = 0;
//							tireV = car.driveForward(degrees, "", null);
//						} else if (angleDiff > cutoff) {
//							tireV = car.driveForward(degrees, "left", cV);
//						} else if (angleDiff < cutoff) {
//							degrees = degrees*(-1);
//							tireV = car.driveForward(degrees, "right", cV);
//						}
//					} else if (Keyboard.getEventKey() == Keyboard.KEY_2) {
//						float angleDiff = tireOne.getObjectAngle() - car.getObjectAngle();
//						int cutoff = 10;
//						if (Math.abs(angleDiff) <= cutoff) {
//							degrees = 0;
//							tireV = car.driveReverse(degrees, "", null);
//						} else if (angleDiff > cutoff) {
//							degrees = degrees*(-1);
//							tireV = car.driveReverse(degrees, "left", cV);
//						} else if (angleDiff < cutoff) {
//							tireV = car.driveReverse(degrees, "right", cV);
//						}
//					}
//
//					float objectAngle = car.getObjectAngle();
//					tireOne.driveTire(degrees, objectAngle, tireV, new Vector3f(-1.6f, 0, 2.2f));
//					tireTwo.driveTire(degrees, objectAngle, tireV, new Vector3f(1.6f, 0, 2.2f));
//					tireThree.driveTire(degrees, objectAngle, tireV, new Vector3f(-1.6f, 0, -1.9f));
//					tireFour.driveTire(degrees, objectAngle, tireV, new Vector3f(1.6f, 0, -1.9f));
				}
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
					System.out.println("A Key Released");
				}
			}
		}
	}


	public static void main(String[] args) {
		new Game();
	}
}