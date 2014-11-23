package sample;

import org.lwjgl.*;
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
	private static String TITLE = "Ally Run!";
	private static final int FRAMERATE = 60;
	private static final int CANVAS_WIDTH = 800;  // width of the drawable
	private static final int CANVAS_HEIGHT = 600; // height of the drawable

	private static ArrayList<Model> models = new ArrayList<>();
	private static ArrayList<Model> carParts = new ArrayList<>();

	private Model tireOne;
	private Model tireTwo;
	private Model tireThree;
	private Model tireFour;
	private Model car;
	private Model ship;
	private Model parkingLot;
	private Texture tireTexture;
	private Texture carTexture;
	private Texture shipTexture;
	private Texture parkingLotTexture;



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
//		glMatrixMode(GL_PROJECTION);
//		glLoadIdentity();
//		glOrtho(0, 640, 480, 0, 1, -1);
//		glMatrixMode(GL_MODELVIEW);
//		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glShadeModel(GL_SMOOTH);

		reshape(CANVAS_WIDTH, CANVAS_HEIGHT);

		display();
	}


	private void loadModels() {

//		LOAD TEXTURES FIRST
		tireTexture = loadTexture("JPG", "tire.jpg");
		carTexture = loadTexture("JPG", "car.jpg");
		shipTexture = loadTexture("JPG", "sh3.jpg");
		parkingLotTexture = loadTexture("JPG", "ParkingLot.jpg");


//		LOAD OJB's
		String objPath = "/home/matt/Programming/Java/CS455/AllyRun/Objects/";
		tireOne = Model.getModel(objPath + "tire.obj", new Vector3f(0f, 0f, 0f), new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(-2.2f, 0.5f, -17.6f));
		models.add(tireOne);
		carParts.add(tireOne);
		tireOne.setFrontTire(90f);

		tireTwo = Model.getModel(objPath + "tire.obj", new Vector3f(0f, 0f, 0f), new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(-2.2f, 0.5f, -14.4f));
		models.add(tireTwo);
		carParts.add(tireTwo);
		tireTwo.setFrontTire(-90f);

		tireThree = Model.getModel(objPath + "tire.obj", new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f), new Vector3f(1.9f, 0.5f, -17.6f));
		models.add(tireThree);
		carParts.add(tireThree);
		tireThree.setIsStationary(90f);

		tireFour = Model.getModel(objPath + "tire.obj", new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f), new Vector3f(1.9f, 0.5f, -14.4f));
		models.add(tireFour);
		carParts.add(tireFour);
		tireFour.setIsStationary(-90f);

		car = Model.getModel(objPath + "car.obj", new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(4f, 4f, 4f), new Vector3f(0f, 0f, -4f));
		models.add(car);
		carParts.add(car);
		car.setIsStationary(90f);

		ship = Model.getModel(objPath + "Sample_Ships.obj",
				new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(4f, 4f, 4f), new Vector3f(0f, 0f, -4f));
		models.add(ship);
		carParts.add(ship);

		parkingLot = Model.getModel("/home/matt/Programming/Java/CS455/ParkingLot.obj",
				new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(7f, 7f, 7f), new Vector3f(6.7f, 0f, 2.4f));
		models.add(parkingLot);
		parkingLot.setIsStationary(35f);

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
			if (hasDisplayed) getInput();
			else hasDisplayed = true;

			//Render
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			tireTexture.bind();
			tireOne.render();
			tireTwo.render();
			tireThree.render();
			tireFour.render();

			carTexture.bind();
			car.render();

			shipTexture.bind();
			ship.render();

			parkingLotTexture.bind();
			parkingLot.render();

			Display.update();
			Display.sync(FRAMERATE);
		}

		Display.destroy();
	}


	public void getInput() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String s = br.readLine();
			if (s.contains("f")) {
				for (Model model : models) {
					model.updateTranslate(new Vector3f(-1f, 0f, 0f));
				}
			}
			if (s.contains("s")) {
				for (Model model : models) {
					model.updateTranslate(new Vector3f(1f, 0f, 0f));
				}
			}
			if (s.contains("e")) {
				for (Model model : models) {
					model.updateTranslate(new Vector3f(0.0f, -1f, 0f));
				}
			}
			if (s.contains("d")) {
				for (Model model : models) {
					model.updateTranslate(new Vector3f(0f, 1f, 0f));
				}
			}
			if (s.contains("q")) {
				for (Model model : models) {
					model.updateTranslate(new Vector3f(0f, 0f, 2f));
				}
			}
			if (s.contains("a")) {
				for (Model model : models) {
					model.updateTranslate(new Vector3f(0.0f, 0f, -2f));
				}
			}
			if (s.contains("6")) {
				for (Model model : models) {
					model.updateTireRotation(-5f);
				}
			}
			if (s.contains("4")) {
				for (Model model : models) {
					model.updateTireRotation(5f);
				}
			}
			if (s.contains("t")) {
				for (Model model : models) {
					model.updateHorizontalRotate(-5f);
				}
			}
			if (s.contains("g")) {
				for (Model model : models) {
					model.updateHorizontalRotate(5f);
				}
			}
			if (s.contains("r")) {
				for (Model model : models) {
					model.updateVerticalRotate(5f);
				}
			}
			if (s.contains("w")) {
				for (Model model : models) {
					model.updateVerticalRotate(-5f);
				}
			}

			if (s.contains("9") || s.contains("8") || s.contains("7") ||
					s.contains("3") || s.contains("2") || s.contains("1")) {

				Vector3f cV = new Vector3f(0, 0, 4);
				Vector3f tireV = null;
				float degrees = 5;
				if (s.contains("9")) {
					degrees = degrees*(-1);
					tireV = car.driveForward(degrees, "right", cV);
				} else if (s.contains("3")) {
					tireV = car.driveReverse(degrees, "right", cV);
				} else if (s.contains("7")) {
					tireV = car.driveForward(degrees, "left", cV);
				} else if (s.contains("1")) {
					degrees = degrees*(-1);
					tireV = car.driveReverse(degrees, "left", cV);
				} else if (s.contains("8")) {
					float angleDiff = tireOne.getObjectAngle() - car.getObjectAngle();
					int cutoff = 9;
					if (Math.abs(angleDiff) < cutoff) {
						degrees = 0;
						tireV = car.driveForward(degrees, "", null);
					} else if (angleDiff > cutoff) {
						tireV = car.driveForward(degrees, "left", cV);
					} else if (angleDiff < cutoff) {
						degrees = degrees*(-1);
						tireV = car.driveForward(degrees, "right", cV);
					}
				} else if (s.contains("2")) {
					float angleDiff = tireOne.getObjectAngle() - car.getObjectAngle();
					int cutoff = 10;
					if (Math.abs(angleDiff) <= cutoff) {
						degrees = 0;
						tireV = car.driveReverse(degrees, "", null);
					} else if (angleDiff > cutoff) {
						degrees = degrees*(-1);
						tireV = car.driveReverse(degrees, "left", cV);
					} else if (angleDiff < cutoff) {
						tireV = car.driveReverse(degrees, "right", cV);
					}
				}

				float objectAngle = car.getObjectAngle();
				tireOne.driveTire(degrees, objectAngle, tireV, new Vector3f(-1.6f, 0, 2.2f));
				tireTwo.driveTire(degrees, objectAngle, tireV, new Vector3f(1.6f, 0, 2.2f));
				tireThree.driveTire(degrees, objectAngle, tireV, new Vector3f(-1.6f, 0, -1.9f));
				tireFour.driveTire(degrees, objectAngle, tireV, new Vector3f(1.6f, 0, -1.9f));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		new Game();
	}
}