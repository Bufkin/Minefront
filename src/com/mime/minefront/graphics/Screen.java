package com.mime.minefront.graphics;

import java.util.Random;

import com.mime.minefront.Game;

public class Screen extends Render {

	private Render test;
	private Render3D render;

	public Screen(int width, int height) {
		super(width, height);
		Random random = new Random();
		this.render = new Render3D(width, height);
		this.test = new Render(256, 256);
		for (int i = 0; i < 256 * 256; i++) {
			this.test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
		}
	}

	public void render(Game game) {
		for (int i = 0; i < this.width * this.height; i++) {
			this.pixels[i] = 0;
		}

		this.render.floor(game);
//		this.render.renderWall(0, 0.5, 1.5, 1.5, 0);
//		this.render.renderWall(0, 0, 1, 1.5, 0);
//		this.render.renderWall(0, 0.5, 1, 1, 0);
//		this.render.renderWall(0.5, 0.5, 1, 1.5, 0);

		this.render.renderDistanceLimiter();

		this.draw(this.render, 0, 0);
	}
}
