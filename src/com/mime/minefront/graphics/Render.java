package com.mime.minefront.graphics;

public class Render {

	public final int width;
	public final int height;
	public final int[] pixels;

	public Render(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

	public void draw(Render render, int xOffset, int yOffset) {
		for (int y = 0; y < render.height; y++) {
			int yPix = y + yOffset;

			if (yPix < 0 || yPix >= this.height) {
				continue;
			}

			for (int x = 0; x < render.width; x++) {
				int xPix = x + xOffset;

				if (xPix < 0 || xPix >= this.width) {
					continue;
				}

				int alpha = render.pixels[x + y * render.width];

				if (alpha > 0) {
					this.pixels[xPix + yPix * this.width] = alpha;
				}
			}
		}
	}
}
