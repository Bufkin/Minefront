package com.mime.minefront.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mime.minefront.entity.Entity;
import com.mime.minefront.graphics.Sprite;

public class Level {

	public Block[] blocks;
	public final int width;
	public final int height;
	final Random random = new Random();

	private List<Entity> entities = new ArrayList<>();

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		this.blocks = new Block[width * height];
		this.generateLevel();
	}

	public void update() {
		for (int i = 0; i < this.entities.size(); i++) {
			this.entities.get(i).tick();
		}
	}

	public void addEntity(Entity e) {
		this.entities.add(e);
	}

	private void generateLevel() {
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				Block block = null;
				if (this.random.nextInt(18) == 0) {
					block = new SolidBlock();
				} else {
					block = new Block();
					if (this.random.nextInt(15) == 0) {
						block.addSprite(new Sprite(0, 0, 0));
					}
				}
				this.blocks[x + y * this.width] = block;
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
