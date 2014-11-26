package com;

import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

public class InputController {

	private static ObjUtils m = ObjUtils.getInstance();
	private static boolean changeView = false;

	private static boolean left = false;
	private static boolean right = false;
	private static boolean duck = false;
	private static boolean jump = false;

	private static final float DUCK_DIFF = 0.002f;
	private static boolean duckInProgress = false;

	private static boolean jumpInProgress = false;
	private static double jumpStatus = 0.0;




	private static void changeView() { changeView = !changeView; }
	private static void leftPressed() { left = true; }
	private static void rightPressed() { right = true; }
	private static void downPressed() { duck = true; }
	private static void upPressed() { jump = true; }

	private static void leftReleased() { left = false; }
	private static void rightReleased() { right = false; }
	private static void downReleased() { duck = false; }


	public static void checkForInput() {
		ObjUtils m = ObjUtils.getInstance();

		handleCurrentStatus();

		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_F) GL11.glTranslatef(-1, 0, 0);
			if (Keyboard.getEventKey() == Keyboard.KEY_S) GL11.glTranslatef(1, 0, 0);
			if (Keyboard.getEventKey() == Keyboard.KEY_E) GL11.glTranslatef(0, -1, 0);
			if (Keyboard.getEventKey() == Keyboard.KEY_D) GL11.glTranslatef(0, 1, 0);
			if (Keyboard.getEventKey() == Keyboard.KEY_Q) GL11.glTranslatef(0, 0, 2);
			if (Keyboard.getEventKey() == Keyboard.KEY_A) GL11.glTranslatef(0, 0, -2);

			if (Keyboard.getEventKey() == Keyboard.KEY_I) GL11.glRotatef(10, -1, 0, 0);
			if (Keyboard.getEventKey() == Keyboard.KEY_K) GL11.glRotatef(10, 1, 0, 0);
			if (Keyboard.getEventKey() == Keyboard.KEY_L) GL11.glRotatef(10, 0, 1, 0);
			if (Keyboard.getEventKey() == Keyboard.KEY_J) GL11.glRotatef(10, 0, -1, 0);
			if (Keyboard.getEventKey() == Keyboard.KEY_U) GL11.glRotatef(10, 0, 0, 1);
			if (Keyboard.getEventKey() == Keyboard.KEY_O) GL11.glRotatef(10, 0, 0, -1);
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD8) {
					if (!jumpInProgress) {
						jumpInProgress = true;
						InputController.upPressed();
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD4) {
					InputController.leftPressed();
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD6) {
					InputController.rightPressed();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD5 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD2) {
					InputController.downPressed();
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD0) {
					InputController.changeView();
//					TODO: changeView
				}
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD4) InputController.leftReleased();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD6) InputController.rightReleased();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD5 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD2) {
					InputController.downReleased();
					if (duckInProgress) {
						duckInProgress = false;
						m.you.updateTranslate(new Vector3f(0, DUCK_DIFF, 0));
					}
				}
			}
		}
	}

	private static void handleCurrentStatus() {
		if (jumpInProgress) {
			handleJump();
		}
		if (right) {
			GL11.glTranslatef(-0.03f, 0, 0);
			m.you.updateTranslate(new Vector3f(0.03f, 0, 0));
		}
		if (left) {
			GL11.glTranslatef(0.03f, 0, 0);
			m.you.updateTranslate(new Vector3f(-0.03f, 0, 0));
		}
		if (duck) {
			if (!duckInProgress) {
				duckInProgress = true;
				m.you.updateTranslate(new Vector3f(0, -DUCK_DIFF, 0));
			}
		} else {
			duckInProgress = false;
		}
	}


	private static final float JUMP_SPEED = 0.01f;
	private static int counter;
	private static void handleJump() {
		jumpStatus += JUMP_SPEED;
		if (jumpStatus > 0.2) { // Go back down
			if (jumpStatus < 0.4) {
				if (jumpStatus > 0.3) {
					counter -= 2;
					jumpStatus += JUMP_SPEED;
					m.you.updateTranslate(new Vector3f(0, -(JUMP_SPEED * 2), 0));
				} else {
					counter -= 1;
					m.you.updateTranslate(new Vector3f(0, -JUMP_SPEED, 0));
				}
			} else {
				jumpStatus = 0;
				jumpInProgress = false;
				jump = false;
			}
		} else { // Go up
			if (jumpStatus <= 0.1) {
				counter += 2;
				jumpStatus += JUMP_SPEED;
				m.you.updateTranslate(new Vector3f(0, JUMP_SPEED * 2, 0));
			} else {
				counter += 1;
				m.you.updateTranslate(new Vector3f(0, JUMP_SPEED, 0));
			}
		}
	}
}
