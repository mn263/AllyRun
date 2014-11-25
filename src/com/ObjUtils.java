package com;

import com.modelUtils.*;
import org.lwjgl.util.vector.*;
import org.newdawn.slick.opengl.*;

import java.io.*;
import java.util.*;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class ObjUtils {
	private static ObjUtils ourInstance = new ObjUtils();

	public static ObjUtils getInstance() {
		return ourInstance;
	}

	public Model ship;
	public Model sky;
	public Model you;
	public RepeatingModel bridge;
	public RepeatingModel ocean;
	public Texture shipTexture;
	public Texture skyTexture;
	public Texture youTexture;
	public Texture bridgeTexture;
	public Texture oceanTexture;

	public void loadModels(ArrayList<Model> models, ArrayList<Model> carParts) {

//		LOAD TEXTURES FIRST
		shipTexture = loadTexture("JPG", Game.LEVEL + "sh3.jpg");
//		shipTexture = loadTexture("JPG", LEVEL + "enemy.jpg");
		youTexture = loadTexture("JPG", Game.LEVEL + "you.jpg");
		bridgeTexture = loadTexture("JPG", Game.LEVEL + "pier.jpg");
		oceanTexture = loadTexture("JPG", Game.LEVEL + "ocean.jpg");
		skyTexture = loadTexture("JPG", Game.LEVEL + "sky.jpg");

//		LOAD OJB's
		String objPath = "/home/matt/Programming/Java/CS455/AllyRun/Objects/";
//		enemy = Model.getModel(objPath + "enemy.obj",
//				new Vector3f(3.0f, 0.0f, 0.0f), new Vector3f(4f, 4f, 4f), new Vector3f(0f, 0f, -4f));
//		models.add(enemy);
//		carParts.add(enemy);
//		enemy.setIsStationary(0f);

//		ship = Model.getModel(objPath + "Ship.obj",
//				new Vector3f(3.0f, 0.0f, 0.0f), new Vector3f(4f, 4f, 4f), new Vector3f(0f, 0f, -4f));
//		models.add(ship);
//		carParts.add(ship);
//		ship.setIsStationary(0f);

		you = Model.getModel(objPath + "you.obj",
				new Vector3f(0.0f, -0.3f, 1.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -2f));
		models.add(you);
		carParts.add(you);
		you.setIsStationary(0f);

		bridge = (RepeatingModel) RepeatingModel.getModel(objPath + "pier.obj",
				new Vector3f(0.0f, -1.0f, -2.0f), new Vector3f(0.01f, 0.01f, 0.01f), new Vector3f(0f, 0f, -20f));
		models.add(bridge);
		bridge.setIsStationary(90f);
		carParts.add(bridge);

		sky = Model.getModel(objPath + "sky.obj",
				new Vector3f(-5.0f, -15.0f, -2.0f), new Vector3f(4f, 1f, 1f), new Vector3f(-10f, -20f, -20f));
		models.add(sky);
		carParts.add(sky);

		ocean = (RepeatingModel) RepeatingModel.getModel(objPath + "ocean.obj",
				new Vector3f(0.0f, -12.0f, -2.0f), new Vector3f(2f, 1f, 1f), new Vector3f(0f, 0f, -20f));
		models.add(ocean);
		ocean.setIsStationary(90f);
		carParts.add(ocean);
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



	public void initialModelLoad() {
//		shipTexture.bind();
//		ship.render(true);

		youTexture.bind();
		you.render(true);

		bridgeTexture.bind();
		bridge.render(10, 5, true);

		oceanTexture.bind();
		ocean.render(2, 70, true);

		skyTexture.bind();
		sky.render(true);
	}


	public void doAnyModelUpdating() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		if (InputTracker.viewChanged()) {
			// TODO: handle view changing
			InputTracker.changeView();
			System.out.println("view changing");
		}
		if (InputTracker.moveLeft()) {
			// TODO: handle moving left
			System.out.println("moving left");
		}
		if (InputTracker.moveRight()) {
			// TODO: handle moving right
			System.out.println("moving right");
		}
		if (InputTracker.duck()) {
			// TODO: handle ducking
			System.out.println("ducking");
		}
		if (InputTracker.jump()) {
//			TODO: handle jumping
			System.out.println("jumped");
			InputTracker.upReleased(); // They can't just jump off of the air
		}


		//Render
//		shipTexture.bind();
//		ship.render(false);

		youTexture.bind();
		you.render(true);
//
		bridgeTexture.bind();
		bridge.render(10, 5, false);

		oceanTexture.bind();
		ocean.render(2, 20, false);

		skyTexture.bind();
		sky.render(false);
	}

}
