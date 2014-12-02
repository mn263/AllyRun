package com;

import org.lwjgl.input.*;
import org.lwjgl.util.vector.*;

public class InputController {

	private static ObjUtils m = ObjUtils.getInstance();
	private static StatusUtils status = StatusUtils.getInstance();
	private static boolean changeView = false;

	private static boolean left = false;
	private static boolean right = false;
	private static boolean duck = false;
	private static boolean jump = false;

	public static final float STRAFE_DIFF = 0.01f;

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
		StatusUtils status = StatusUtils.getInstance();

		handleCurrentStatus();

		while (Keyboard.next()) {
			if(!Game.isPlaying()) {
				Game.startPlay();
				return;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_F) status.moveCamera(new Vector3f(-0.1f, 0, 0));
			if (Keyboard.getEventKey() == Keyboard.KEY_S) status.moveCamera(new Vector3f(0.1f, 0, 0));
			if (Keyboard.getEventKey() == Keyboard.KEY_E) status.moveCamera(new Vector3f(0, -0.1f, 0));
			if (Keyboard.getEventKey() == Keyboard.KEY_D) status.moveCamera(new Vector3f(0, 0.1f, 0));
			if (Keyboard.getEventKey() == Keyboard.KEY_Q) status.moveCamera(new Vector3f(0, 0, 0.2f));
			if (Keyboard.getEventKey() == Keyboard.KEY_A) status.moveCamera(new Vector3f(0, 0, -0.2f));

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
					if (!duckInProgress) {
						checkForFood();
					}
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

	private static void checkForFood() {
		if (m.food1.intersects(m.you.boundingBox)) {
			status.addToScore(5);
		} else if (m.food2.intersects(m.you.boundingBox)) {
			status.addToScore(10);
		} else if (m.food3.intersects(m.you.boundingBox)) {
			status.addToScore(20);
		} else {
			status.addToScore(-5);
		}
	}

	private static void handleCurrentStatus() {
		if (jumpInProgress) {
			handleJump();
		}
		if (right) {
			status.updateScreenLocation(new Vector3f(-STRAFE_DIFF, 0, 0));
		} else if (left) {
			status.updateScreenLocation(new Vector3f(STRAFE_DIFF, 0, 0));
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


	private static final float JUMP_SPEED = 0.02f;
	private static int counter;
	private static void handleJump() {
		jumpStatus += JUMP_SPEED;
		if (jumpStatus > 0.2) { // Go back down
			if (jumpStatus < 0.4) {
				if (jumpStatus > 0.3) {
					counter -= 2;
					jumpStatus += JUMP_SPEED;
					m.you.updateTranslate(new Vector3f(0, -(JUMP_SPEED * 1), 0));
				} else {
					counter -= 1;
					m.you.updateTranslate(new Vector3f(0, -(JUMP_SPEED * 0.5f), 0));
				}
			} else {
				if(counter < 0) {
					m.you.updateTranslate(new Vector3f(0, -(counter * JUMP_SPEED * 0.5f), 0));
					counter = 0;
				}
				jumpStatus = 0;
				jumpInProgress = false;
				jump = false;
			}
		} else { // Go up
			if (jumpStatus <= 0.1) {
				counter += 2;
				jumpStatus += JUMP_SPEED;
				m.you.updateTranslate(new Vector3f(0, JUMP_SPEED * 1, 0));
			} else {
				counter += 1;
				m.you.updateTranslate(new Vector3f(0, JUMP_SPEED * 0.5f, 0));
			}
		}
	}
}
