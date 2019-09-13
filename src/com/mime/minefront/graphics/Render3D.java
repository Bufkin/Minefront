package com.mime.minefront.graphics;

import com.mime.minefront.Game;
import com.mime.minefront.input.Controller;

public class Render3D extends Render {

	public double[] zBuffer;
	private double renderDistance = 5000;
	private double forward, right, up, cosine, sine;

	public Render3D(int width, int height) {
		super(width, height);
		this.zBuffer = new double[width * height];
	}

	public void floor(Game game) {

		double floorPosition = 8;
		double ceilingPosition = 8;
		this.forward = game.controls.z;
		this.right = game.controls.x;
		this.up = game.controls.y;
		double walking = Math.sin((game.time / 6.0) * 0.5);
		if (Controller.crouchWalk) {
			walking = Math.sin((game.time / 6.0) * 0.25);
		}
		if (Controller.runWalk) {
			walking = Math.sin((game.time / 6.0) * 0.8);
		}

		double rotation = 0; // Math.sin(game.time % 1000.0 / 80); // game.controls.rotation;
		this.cosine = Math.cos(rotation);
		this.sine = Math.sin(rotation);

		for (int y = 0; y < this.height; y++) {
			double ceiling = (y + -this.height / 2.0) / this.height;

			double z = (floorPosition + this.up) / ceiling;
			if (Controller.walk) {
				z = (floorPosition + this.up + walking) / ceiling;
			}

			if (ceiling < 0) {
				z = (ceilingPosition - this.up) / -ceiling;
				if (Controller.walk) {
					z = (ceilingPosition - this.up - walking) / -ceiling;
				}
			}

			for (int x = 0; x < this.width; x++) {
				double depth = (x - this.width / 2.0) / this.height;
				depth *= z;
				double xx = depth * this.cosine + z * this.sine;
				double yy = z * this.cosine - depth * this.sine;
				int xPix = (int) (xx + this.right);
				int yPix = (int) (yy + this.forward);
				this.zBuffer[x + y * this.width] = z;
				this.pixels[x + y * this.width] = Texture.floor.pixels[(xPix & 31) + (yPix & 31) * 32];
			}
		}
	}

	public void renderWall(double xLeft, double xRight, double zDistance, double yHeight) {
		double xcLeft = ((xLeft) - this.right) * 2;
		double zcLeft = ((zDistance) - this.forward) * 2;

		double rotLeftSideX = xcLeft * this.cosine - zcLeft * this.sine;
		double yCornerTL = ((-yHeight) - this.up) * 2;
		double yCornerBL = ((+0.5 - yHeight) - this.up) * 2;
		double rotLeftSideZ = zcLeft * this.cosine - xcLeft * this.sine;

		double xcRight = ((xRight) - this.right) * 2;
		double zcRight = ((zDistance) - this.forward) * 2;

		double rotRightSideX = xcRight * this.cosine - zcRight * this.sine;
		double yCornerTR = ((-yHeight) - this.up) * 2;
		double yCornerBR = ((+0.5 - yHeight) - this.up) * 2;
		double rotRightSideZ = zcRight * this.cosine - xcRight * this.sine;

		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * this.height + this.width / 2);
		double xPixelRight = (rotRightSideX / rotRightSideZ * this.height + this.width / 2);

		if (xPixelLeft >= xPixelRight) {
			return;
		}

		int xPixelLeftInt = (int) xPixelLeft;
		int xPixelRightInt = (int) xPixelRight;

		if (xPixelLeftInt < 0) {
			xPixelLeftInt = 0;
		}

		if (xPixelRightInt > this.width) {
			xPixelRightInt = this.width;
		}

		double yPixelLeftTop = (int) yCornerTL / rotLeftSideZ * this.height + this.height / 2;
		double yPixelLeftBottom = (int) yCornerBL / rotLeftSideZ * this.height + this.height / 2;
		double yPixelRightTop = (int) yCornerTR / rotRightSideZ * this.height + this.height / 2;
		double yPixelRightBottom = (int) yCornerBR / rotRightSideZ * this.height + this.height / 2;

		for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);

			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

			int yPixelTopInt = (int) yPixelTop;
			int yPixelBottomInt = (int) yPixelBottom;

			if (yPixelTopInt < 0) {
				yPixelTopInt = 0;
			}

			if (yPixelTopInt > this.height) {
				yPixelTopInt = this.height;
			}

			for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
				this.pixels[x + y * this.width] = 0x1B91E0;
				this.zBuffer[x + y * this.width] = 0;
			}
		}
	}

	public void renderDistanceLimiter() {
		for (int i = 0; i < this.width * this.height; i++) {
			int color = this.pixels[i];
			int brightness = (int) (this.renderDistance / this.zBuffer[i]);

			if (brightness < 0) {
				brightness = 0;
			}

			if (brightness > 255) {
				brightness = 255;
			}

			int r = (color >> 16) & 0xff;
			int g = (color >> 8) & 0xff;
			int b = (color) & 0xff;

			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;

			this.pixels[i] = r << 16 | g << 8 | b;
		}
	}

}
