package com.mime.minefront.level;

import java.util.ArrayList;
import java.util.List;

import com.mime.minefront.graphics.Sprite;

public class Block {

	public boolean solid = false;

	public static Block solidWall = new SolidBlock();

	public List<Sprite> sprites = new ArrayList<>();

	public void addSprite(Sprite sprite) {
		this.sprites.add(sprite);
	}
}
