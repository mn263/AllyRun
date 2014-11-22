package sample;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class SimpleScene implements GLEventListener {

	private GLU glu;  // for the GL Utility
//	private Vector3f location, rotation;
//	private Point3D cameraPosition = new Point3D(0, 0, 20);
//	private float angle;

	@Override
	public void init(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();
		glu = new GLU();

		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45f, (float) (640 / 480), 0.1f, 400f);
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glEnable(GL_DEPTH_TEST);

		try {
			GLContext glContext = GLContext.getCurrent();
			glContext.makeCurrent();
		} catch (GLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Call-back handler for window re-size event. Also called when the drawable is
	 * first set to visible.
	 */
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
		glu.gluPerspective(45.0, aspect, 0.1, 400.0); // fovy, aspect, zNear, zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset
	}

	@Override
	public void dispose(GLAutoDrawable glAutoDrawable) {
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear color and depth buffers
		glMatrixMode(GL_MODELVIEW);     // To operate on model-view matrix

		// Render a color-cube consisting of 6 quads with different colors
		glLoadIdentity();                 // Reset the model-view matrix
		glTranslatef(1.5f, 0.0f, -7.0f);  // Move right and into the screen

		glBegin(GL_QUADS);                // Begin drawing the color cube with 6 quads
		// Top face (y = 1.0f)
		// Define vertices in counter-clockwise (CCW) order with normal pointing out
		glColor3f(0.0f, 1.0f, 0.0f);     // Green
		glVertex3f( 1.0f, 1.0f, -1.0f);
		glVertex3f(-1.0f, 1.0f, -1.0f);
		glVertex3f(-1.0f, 1.0f,  1.0f);
		glVertex3f( 1.0f, 1.0f,  1.0f);

		// Bottom face (y = -1.0f)
		glColor3f(1.0f, 0.5f, 0.0f);     // Orange
		glVertex3f( 1.0f, -1.0f,  1.0f);
		glVertex3f(-1.0f, -1.0f,  1.0f);
		glVertex3f(-1.0f, -1.0f, -1.0f);
		glVertex3f( 1.0f, -1.0f, -1.0f);

		// Front face  (z = 1.0f)
		glColor3f(1.0f, 0.0f, 0.0f);     // Red
		glVertex3f( 1.0f,  1.0f, 1.0f);
		glVertex3f(-1.0f,  1.0f, 1.0f);
		glVertex3f(-1.0f, -1.0f, 1.0f);
		glVertex3f( 1.0f, -1.0f, 1.0f);

		// Back face (z = -1.0f)
		glColor3f(1.0f, 1.0f, 0.0f);     // Yellow
		glVertex3f( 1.0f, -1.0f, -1.0f);
		glVertex3f(-1.0f, -1.0f, -1.0f);
		glVertex3f(-1.0f,  1.0f, -1.0f);
		glVertex3f( 1.0f,  1.0f, -1.0f);

		// Left face (x = -1.0f)
		glColor3f(0.0f, 0.0f, 1.0f);     // Blue
		glVertex3f(-1.0f,  1.0f,  1.0f);
		glVertex3f(-1.0f,  1.0f, -1.0f);
		glVertex3f(-1.0f, -1.0f, -1.0f);
		glVertex3f(-1.0f, -1.0f,  1.0f);

		// Right face (x = 1.0f)
		glColor3f(1.0f, 0.0f, 1.0f);     // Magenta
		glVertex3f(1.0f,  1.0f, -1.0f);
		glVertex3f(1.0f,  1.0f,  1.0f);
		glVertex3f(1.0f, -1.0f,  1.0f);
		glVertex3f(1.0f, -1.0f, -1.0f);
		glEnd();  // End of drawing color-cube

		// Render a pyramid consists of 4 triangles
		glLoadIdentity();                  // Reset the model-view matrix
		glTranslatef(-1.5f, 0.0f, -6.0f);  // Move left and into the screen

		glBegin(GL_TRIANGLES);           // Begin drawing the pyramid with 4 triangles
		// Front
		glColor3f(1.0f, 0.0f, 0.0f);     // Red
		glVertex3f( 0.0f, 1.0f, 0.0f);
		glColor3f(0.0f, 1.0f, 0.0f);     // Green
		glVertex3f(-1.0f, -1.0f, 1.0f);
		glColor3f(0.0f, 0.0f, 1.0f);     // Blue
		glVertex3f(1.0f, -1.0f, 1.0f);

		// Right
		glColor3f(1.0f, 0.0f, 0.0f);     // Red
		glVertex3f(0.0f, 1.0f, 0.0f);
		glColor3f(0.0f, 0.0f, 1.0f);     // Blue
		glVertex3f(1.0f, -1.0f, 1.0f);
		glColor3f(0.0f, 1.0f, 0.0f);     // Green
		glVertex3f(1.0f, -1.0f, -1.0f);

		// Back
		glColor3f(1.0f, 0.0f, 0.0f);     // Red
		glVertex3f(0.0f, 1.0f, 0.0f);
		glColor3f(0.0f, 1.0f, 0.0f);     // Green
		glVertex3f(1.0f, -1.0f, -1.0f);
		glColor3f(0.0f, 0.0f, 1.0f);     // Blue
		glVertex3f(-1.0f, -1.0f, -1.0f);

		// Left
		glColor3f(1.0f,0.0f,0.0f);       // Red
		glVertex3f( 0.0f, 1.0f, 0.0f);
		glColor3f(0.0f,0.0f,1.0f);       // Blue
		glVertex3f(-1.0f,-1.0f,-1.0f);
		glColor3f(0.0f,1.0f,0.0f);       // Green
		glVertex3f(-1.0f,-1.0f, 1.0f);
		glEnd();   // Done drawing the pyramid

	}

	private void update() {
	}


	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

		gl.glEnable(GL_TEXTURE_2D);
		gl.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

//		ArrayList<Model> models = Main.getModels();
//		for (Model currModel : models) {
//			currModel.render(drawable);
//		}
	}

}


//package com.company;
//
//import org.lwjgl.util.vector.*;
//
//import javax.media.opengl.*;
//import javax.media.opengl.glu.*;
//import java.awt.event.*;
//import java.util.*;
//
//import static javax.media.opengl.GL.GL_DEPTH_TEST;
//import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
//
//
//
//public class SimpleScene implements GLEventListener, KeyListener {
//
//	private GLU glu;  // for the GL Utility
//////	Texture texture = null;
//
////	private GLU glu;  // for the GL Utility
////	private float angleRotation = 0;    // rotational angle in degree for pyramid
////	private float rotationSpeed = 2.0f; // rotational speed for pyramid
////	private float speedCube = -1.5f;   // rotational speed for cube
//
//	@Override
//	public void init(GLAutoDrawable drawable) {
//
//		location = new Vector3f(0f, 0f, 0f);
//		rotation = new Vector3f(0f, 0f, 0f);
//
//
//		GL2 gl = drawable.getGL().getGL2();
//		glu = new GLU();
//		gl.glMatrixMode(GL_PROJECTION);
//		gl.glLoadIdentity();
////		gl.glOrtho(0, 1.0, 0.0, 1.0, -1.0, 1.0);
//		glu.gluPerspective(45, (float) (640 / 480), 0.1, 400);
////		gl.glMatrixMode(GL_MODELVIEW);
//		gl.glEnable(GL_DEPTH_TEST);
//
//		try {
//			GLContext glContext = GLContext.getCurrent();
//			glContext.makeCurrent();
//		} catch (GLException e) {
//			e.printStackTrace();
//		}
////		gl.glLoadIdentity();
//
//	}
//
//	/**
//	 * Call-back handler for window re-size event. Also called when the drawable is
//	 * first set to visible.
//	 */
//	@Override
//	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
//	}
//
//	@Override
//	public void dispose(GLAutoDrawable glAutoDrawable) {
//	}
//
//	@Override
//	public void display(GLAutoDrawable drawable) {
//		update();
//		render(drawable);
//	}
//
//
//	private void update() {
//	}
//
//	private void render(GLAutoDrawable drawable) {
//		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
//
////		gl.glEnable(GL_TEXTURE_2D);
//		gl.glColor3f(.5f, .5f, .5f);
//		gl.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
//		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
//
//		ArrayList<Model> models = Main.getModels();
//		for (Model currModel : models) {
//			currModel.render(drawable);
//		}
//	}
//
//	@Override
//	public void keyTyped(KeyEvent e) {
//
//	}
//
//	@Override
//	public void keyPressed(KeyEvent e) {
//		input(e.getKeyCode());
//	}
//
//	@Override
//	public void keyReleased(KeyEvent e) {
//
//	}
//}