package com.mime.minefront.level;

import java.util.Random;

import com.mime.minefront.graphics.Sprite;

public class Level {

	public Block[] blocks;
	public final int width;
	public final int height;

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		this.blocks = new Block[width * height];
		Random random = new Random();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Block block = null;
				if (random.nextInt(18) == 0) {
					block = new SolidBlock();
				} else {
					block = new Block();
					if (random.nextInt(15) == 0) {
						block.addSprite(new Sprite(0, 0, 0));
					}
				}
				this.blocks[x + y * width] = block;
			}
		}
	}

	public Block create(int x, int y) {
		if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
			return Block.solidWall;
		}
		return this.blocks[x + y * this.width];
	}
}
