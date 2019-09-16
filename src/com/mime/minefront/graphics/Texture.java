package com.mime.minefront.graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {

	public static Render block = loadBitMap("/textures/blocks.png");

	public static Render loadBitMap(String fileName) {

		try {
			BufferedImage image = ImageIO.read(Texture.class.getResource(fileName));
			int width = image.getWidth();
			int height = image.getHeight();
			Render result = new Render(width, height);
			image.getRGB(0, 0, width, height, result.pixels, 0, width);

			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}