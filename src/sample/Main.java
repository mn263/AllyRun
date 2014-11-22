package sample;

import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.*;
import org.lwjgl.util.vector.*;

import javax.imageio.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.*;
import javax.media.opengl.glu.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL2.GL_LINEAR;
import static javax.media.opengl.GL2.GL_MODELVIEW;
import static javax.media.opengl.GL2.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.GL2.GL_PROJECTION;
import static javax.media.opengl.GL2.GL_REPEAT;
import static javax.media.opengl.GL2.GL_REPLACE;
import static javax.media.opengl.GL2.GL_SMOOTH;
import static javax.media.opengl.GL2.GL_TEXTURE_2D;
import static javax.media.opengl.GL2.GL_TEXTURE_ENV;
import static javax.media.opengl.GL2.GL_TEXTURE_ENV_MODE;
import static javax.media.opengl.GL2.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL2.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL2.GL_TEXTURE_WRAP_S;
import static javax.media.opengl.GL2.GL_TEXTURE_WRAP_T;
import static javax.media.opengl.GL2.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class Main extends GLCanvas implements GLEventListener {
	private static String TITLE = "Project 4";  // window's title
	private static final int CANVAS_WIDTH = 800;  // width of the drawable
	private static final int CANVAS_HEIGHT = 600; // height of the drawable
	private static final int FPS = 10; // animator's target frames per second

	private static ArrayList<Model> models = new ArrayList<>();
	private static ArrayList<Model> carParts = new ArrayList<>();

	private BufferedImage tireImage, carImage, parkingLotImage;

	private Model tireOne;
	private Model tireTwo;
	private Model tireThree;
	private Model tireFour;
	private Texture tireTexture;
	private Model car;
	private Texture carTexture;
	private Model parkingLot;
	private Texture parkingLotTexture;

	private GLU glu;

	public Main() {
		this.addGLEventListener(this);
		KeyboardFocusManager manager =
				KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyDispatcher());
	}

	class KeyDispatcher implements KeyEventDispatcher {
		public boolean dispatchKeyEvent(KeyEvent e) {
			if(e.getID() == KeyEvent.KEY_TYPED) System.out.println( "typed" + e.getExtendedKeyCode() );
			return false;
		}
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GLCanvas canvas = new Main();
				canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
				final FPSAnimator animator = new FPSAnimator(canvas, FPS, false);
				final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
				frame.getContentPane().add(canvas);
				frame.setTitle(TITLE);
				frame.pack();
				frame.setVisible(true);
				animator.start();
			}
		});
	}


	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
//		GLDrawable draw = drawable;

		glu = new GLU();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		gl.glShadeModel(GL_SMOOTH);

		File textureTire = new File("/home/matt/Programming/Java/CS455/tire.bmp");
		File textureCar = new File("/home/matt/Programming/Java/CS455/car.bmp");
		File textureParkingLot = new File("/home/matt/Programming/Java/CS455/ParkingLot.bmp");
		try {
			tireImage = ImageIO.read(textureTire);
			ImageUtil.flipImageVertically(tireImage);

			carImage = ImageIO.read(textureCar);
			ImageUtil.flipImageVertically(carImage);

			parkingLotImage = ImageIO.read(textureParkingLot);
			ImageUtil.flipImageVertically(parkingLotImage);


		} catch (IOException e) {
			e.printStackTrace();
		}

		loadModels();

		GLProfile profile = GLProfile.getDefault();

		try {
			GLContext glContext = GLContext.getCurrent();
			glContext.makeCurrent();
		} catch (GLException e) {
			e.printStackTrace();
		}

		tireTexture = AWTTextureIO.newTexture(profile, tireImage, true);
		carTexture = AWTTextureIO.newTexture(profile, carImage, true);
		parkingLotTexture = AWTTextureIO.newTexture(profile, parkingLotImage, true);

		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		gl.glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
		gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
	}

	private void loadModels() {

		tireOne = Model.getModel("/home/matt/Programming/Java/CS455/tire.obj",
				new Vector3f(0f, 0f, 0f), new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(-2.2f, 0.5f, -17.6f));
		models.add(tireOne);
		carParts.add(tireOne);
		tireOne.setFrontTire(90f);

		tireTwo = Model.getModel("/home/matt/Programming/Java/CS455/tire.obj",
				new Vector3f(0f, 0f, 0f), new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(-2.2f, 0.5f, -14.4f));
		models.add(tireTwo);
		carParts.add(tireTwo);
		tireTwo.setFrontTire(-90f);

		tireThree = Model.getModel("/home/matt/Programming/Java/CS455/tire.obj",
				new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f), new Vector3f(1.9f, 0.5f, -17.6f));
		models.add(tireThree);
		carParts.add(tireThree);
		tireThree.setIsStationary(90f);

		tireFour = Model.getModel("/home/matt/Programming/Java/CS455/tire.obj",
				new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f), new Vector3f(1.9f, 0.5f, -14.4f));
		models.add(tireFour);
		carParts.add(tireFour);
		tireFour.setIsStationary(-90f);

		car = Model.getModel("/home/matt/Programming/Java/CS455/car.obj",
				new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(4f, 4f, 4f), new Vector3f(0f, 0f, -4f));
		models.add(car);
		carParts.add(car);
		car.setIsStationary(90f);

		parkingLot = Model.getModel("/home/matt/Programming/Java/CS455/ParkingLot.obj",
				new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(7f, 7f, 7f), new Vector3f(6.7f, 0f, 2.4f));
		models.add(parkingLot);
		parkingLot.setIsStationary(35f);

		for (Model model : models) {
			model.updateTranslate(new Vector3f(0.0f, -5f, 0f));
		}
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

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

		if (height == 0) height = 1;   // prevent divide by zero
		float aspect = (float)width / height;

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
		gl.glLoadIdentity();             // reset projection matrix
		glu.gluPerspective(45.0, aspect, 0.1, 500.0); // fovy, aspect, zNear, zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset
	}

	private boolean hasDisplayed = false;
	@Override
	public void display(GLAutoDrawable drawable) {
		if (hasDisplayed) {
			getInput();
		} else {
			hasDisplayed = true;
		}
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

		tireTexture.enable(gl);
		tireTexture.bind(gl);
		tireOne.render(drawable);
		tireTwo.render(drawable);
		tireThree.render(drawable);
		tireFour.render(drawable);

		carTexture.enable(gl);
		carTexture.bind(gl);
		car.render(drawable);

		parkingLotTexture.enable(gl);
		parkingLotTexture.bind(gl);
		parkingLot.render(drawable);


		gl.glFlush();
		gl.glDisable(GL_TEXTURE_2D);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) { }
}