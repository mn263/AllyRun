package com;

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
}
