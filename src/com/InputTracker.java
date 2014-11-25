package com;

import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

public class InputTracker {

	private static boolean changeView = false;

	private static boolean left = false;
	private static boolean right = false;
	private static boolean duck = false;
	private static boolean jump = false;


	public static void changeView() { changeView = !changeView; }
	public static void leftPressed() { left = true; }
	public static void rightPressed() { right = true; }
	public static void downPressed() { duck = true; }
	public static void upPressed() { jump = true; }

	public static void leftReleased() { left = false; }
	public static void rightReleased() { right = false; }
	public static void downReleased() { duck = false; }
	public static void upReleased() { jump = false; }

	public static boolean viewChanged() { return changeView; }
	public static boolean moveLeft() { return left; }
	public static boolean moveRight() { return right; }
	public static boolean jump() { return jump; }
	public static boolean duck() { return duck; }


	public static void checkForInput() {
		ObjUtils m = ObjUtils.getInstance();

		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_F) {
				GL11.glTranslatef(-0.03f, 0, 0);
				m.you.updateTranslate(new Vector3f(0.03f, 0, 0));
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_S) {
				GL11.glTranslatef(0.03f, 0, 0);
				m.you.updateTranslate(new Vector3f(-0.03f, 0, 0));
			}
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
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD8) InputTracker.upPressed();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD4) InputTracker.leftPressed();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD6) InputTracker.rightPressed();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD5 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD2)
					InputTracker.downPressed();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD0) InputTracker.changeView();
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD8) InputTracker.upReleased();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD4) InputTracker.leftReleased();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD6) InputTracker.rightReleased();
				if (Keyboard.getEventKey() == Keyboard.KEY_NUMPAD5 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD2)
					InputTracker.downReleased();
			}
		}
	}

}
