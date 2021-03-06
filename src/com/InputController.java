package com;

import org.lwjgl.input.*;
import org.lwjgl.util.vector.*;

public class InputController {

	private static StatusUtils status = StatusUtils.getInstance();
	private static boolean changeView = false;

	private static boolean left = false;
	private static boolean right = false;
	private static boolean duck = false;
	private static boolean jump = false;

	public static final float STRAFE_DIFF = 0.01f;

	private static final float DUCK_DIFF = 0.004f;
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


	public static void checkForInput(ObjUtils m) {
//		ObjUtils m = ObjUtils.getInstance();
		StatusUtils status = StatusUtils.getInstance();

		handleCurrentStatus(m);

		while (Keyboard.next()) {
			if(!Game.isPlaying() &&
					Keyboard.getEventKey() != Keyboard.KEY_NUMPAD4 &&
					Keyboard.getEventKey() != Keyboard.KEY_NUMPAD6 &&
					Keyboard.getEventKey() != Keyboard.KEY_NUMPAD5 &&
					Keyboard.getEventKey() != Keyboard.KEY_NUMPAD8 &&
					Keyboard.getEventKey() != Keyboard.KEY_NUMPAD0 &&
					Keyboard.getEventKey() != Keyboard.KEY_NUMPAD7 &&
					Keyboard.getEventKey() != Keyboard.KEY_NUMPAD9) {
				Game.startPlay();
				return;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_F) status.moveCamera(new Vector3f(-0.4f, 0, 0));
			if (Keyboard.getEventKey() == Keyboard.KEY_S) status.moveCamera(new Vector3f(0.4f, 0, 0));
			if (Keyboard.getEventKey() == Keyboard.KEY_E) status.moveCamera(new Vector3f(0, -0.4f, 0));
			if (Keyboard.getEventKey() == Keyboard.KEY_D) status.moveCamera(new Vector3f(0, 0.4f, 0));
			if (Keyboard.getEventKey() == Keyboard.KEY_Q) status.moveCamera(new Vector3f(0, 0, 0.4f));
			if (Keyboard.getEventKey() == Keyboard.KEY_A) status.moveCamera(new Vector3f(0, 0, -0.4f));

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
						checkForFood(m);
					}
					InputController.downPressed();
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD0) {
					if (changeView) {
						status.moveCamera(new Vector3f(0f, -0.2f, -0.4f));
						m.moveScoreLocation(new Vector3f(0f, -0.2f, -0.4f));
					} else {
						status.moveCamera(new Vector3f(0f, 0.2f, 0.4f));
						m.moveScoreLocation(new Vector3f(0f, 0.2f, 0.4f));
					}
					InputController.changeView();
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

	private static void checkForFood(ObjUtils m) {
		if (m.food1.intersects(m.you.boundingBox)) {
			if(Game.LEVEL.equals("")) status.addToScore(5);
			else status.addToScore(1);
		} else if (m.food2.intersects(m.you.boundingBox)) {
			if(Game.LEVEL.equals("")) status.addToScore(10);
			else status.addToScore(2);
		} else if (m.food3.intersects(m.you.boundingBox)) {
			if(Game.LEVEL.equals("")) status.addToScore(20);
			else status.addToScore(4);
		} else {
			if(Game.LEVEL.equals("")) status.addToScore(-5);
			else status.addToScore(-1);
		}
	}

	private static void handleCurrentStatus(ObjUtils m) {
		if (jumpInProgress) {
			handleJump(m);
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
	private static void handleJump(ObjUtils m) {
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
