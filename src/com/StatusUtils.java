package com;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import static java.lang.Math.*;

public class StatusUtils {
	private static StatusUtils ourInstance = new StatusUtils();

	public static StatusUtils getInstance() {
		return ourInstance;
	}

	private ObjUtils m = null;
	public Game game;

	private int highScore = 0;
	private int score = 0;

	public int getScore() {
		return score;
	}
	public void addToScore(int increase) {
		score = max(0, score + increase);
	}

	public void setScore(int score) {
		this.score = score;
	}
	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}
	public int getHighScore() {
		return this.highScore;
	}

	public void updateStatus(ObjUtils m) {
		if (this.m == null) this.m = m;
		updateScore();
		checkCollision();
	}

	public void updateScreenLocation(Vector3f change) {
		moveCamera(new Vector3f(change.getX(), change.getY(), change.getZ()));
		m.you.updateTranslate(new Vector3f(-change.getX(), -change.getY(), -change.getZ()));
		m.moveScoreLocation(change);
	}


	private int distanceScore = 0;
	private void updateScore() {
		if(Game.gameTime - distanceScore > 5) {
			score += 5;
			distanceScore += 5;
		}
	}

	private void checkCollision() {
		if (m.enemy1.intersects(m.you.boundingBox) || m.enemy2.intersects(m.you.boundingBox)) game.endGame();
	}



	public Vector3f totalCameraMovement = new Vector3f(0, 0, 0);
	public void moveCamera(Vector3f translate) {
		GL11.glTranslatef(translate.getX(), translate.getY(), translate.getZ());
		totalCameraMovement.setX(totalCameraMovement.getX() + translate.getX());
		totalCameraMovement.setY(totalCameraMovement.getY() + translate.getY());
		totalCameraMovement.setZ(totalCameraMovement.getZ() + translate.getZ());
	}

	public void reset() {
		GL11.glTranslatef(-totalCameraMovement.getX(), -totalCameraMovement.getY(), -totalCameraMovement.getZ());
		m.displayInfoScreen();


		ObjUtils.getInstance().loadCharacters();

		totalCameraMovement = new Vector3f(0, 0, 0);
	}
}
