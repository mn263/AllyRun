package com;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import static java.lang.Math.max;

public class StatusUtils {
	private static StatusUtils ourInstance = new StatusUtils();

	public static StatusUtils getInstance() {
		return ourInstance;
	}

	private ObjUtils m = null;

	private int score = 0;
	public int health = 4;
	private int currentLevel = 0;

	public int getScore() {
		return score;
	}
	public void addToScore(int increase) {
		score = max(0, score + increase);

	}

	public void updateStatus(ObjUtils m) {
		if (this.m == null) this.m = m;
		updateScore();
		checkCollision();
	}

	public void updateScreenLocation(Vector3f change) {
		GL11.glTranslatef(change.getX(), change.getY(), change.getZ());
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
		if (m.enemy1.intersects(m.you.boundingBox)) {
			System.out.print("You died--endgame screen should appear");
		} else if (m.enemy2.intersects(m.you.boundingBox)) {
			System.out.print("You died--endgame screen should appear");
		}
	}

	public void changeLevel(int i) {
//		i = 1 means level passed, 0 mean start over

		m.you.updateTranslate(new Vector3f(0, 0, Game.gameTime));
		GL11.glTranslatef(0, 0, -Game.gameTime);
		Game.gameTime += 0;
		distanceScore = 0;

//		TODO: Display "Level Passed" display for a second or so

		if (i == 1) {
			currentLevel += 1;
		} else {
			currentLevel = 0;
		}

		switch (currentLevel) {
			case 0:
				Game.LEVEL = "";
				break;
			case 1:
				Game.LEVEL = "alien_";
				break;
//			TODO: implement more levels
//			case 2: Game.LEVEL = "hell_";
//				break;
			default:
				Game.endGame();
		}
	}


}
