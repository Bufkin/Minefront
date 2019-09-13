package com.mime.minefront.graphics;

import com.mime.minefront.Game;
import com.mime.minefront.input.Controller;
import com.mime.minefront.level.Block;
import com.mime.minefront.level.Level;

public class Render3D extends Render {

	public double[] zBuffer;
	public double[] zBufferWall;
	private double renderDistance = 5000;
	private double forward, right, up, cosine, sine, walking;

	public Render3D(int width, int height) {
		super(width, height);
		this.zBuffer = new double[width * height];
		this.zBufferWall = new double[width];
	}

	public void floor(Game game) {

		for (int x = 0; x < this.width; x++) {
			this.zBufferWall[x] = 0;
		}

		double floorPosition = 8;
		double ceilingPosition = 8;
		this.forward = game.controls.z;
		this.right = game.controls.x;
		this.up = game.controls.y;
		this.walking = 0.0;
		double rotation = game.controls.rotation; // Math.sin(game.time / 10.0) / 10;
		this.cosine = Math.cos(rotation);
		this.sine = Math.sin(rotation);

		for (int y = 0; y < this.height; y++) {
			double ceiling = (y + -this.height / 2.0) / this.height;

			double z = (floorPosition + this.up) / ceiling;
			if (Controller.walk) {
				this.walking = Math.sin((game.time / 6.0) * 0.5);
				z = (floorPosition + this.up + this.walking) / ceiling;
			}

			if (Controller.crouchWalk && Controller.walk) {
				this.walking = Math.sin((game.time / 6.0) * 0.25);
				z = (floorPosition + this.up + this.walking) / ceiling;
			}
			if (Controller.runWalk && Controller.walk) {
				this.walking = Math.sin((game.time / 6.0) * 0.8);
				z = (floorPosition + this.up + this.walking) / ceiling;
			}

			if (ceiling < 0) {
				z = (ceilingPosition - this.up) / -ceiling;
				if (Controller.walk) {
					z = (ceilingPosition - this.up - this.walking) / -ceiling;
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
				this.pixels[x + y * this.width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 16];
			}
		}
		Level level = game.level;
		int size = 20;
		for (int xBlock = -size; xBlock <= size; xBlock++) {
			for (int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock + 1, zBlock);
				Block south = level.create(xBlock, zBlock + 1);

				if (block.solid) {
					if (!east.solid) {
						this.renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0);
					}
					if (!south.solid) {
						this.renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0);
					}
				} else {
					if (east.solid) {
						this.renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0);
					}
					if (south.solid) {
						this.renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0);
					}
				}
			}
		}
		for (int xBlock = -size; xBlock <= size; xBlock++) {
			for (int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock + 1, zBlock);
				Block south = level.create(xBlock, zBlock + 1);

				if (block.solid) {
					if (!east.solid) {
						this.renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0.5);
					}
					if (!south.solid) {
						this.renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0.5);
					}
				} else {
					if (east.solid) {
						this.renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0.5);
					}
					if (south.solid) {
						this.renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0.5);
					}
				}
			}
		}
	}

	public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {
		double upCorrect = 0.0625;
		double rightCorrect = 0.0625;
		double forwardCorrect = 0.0625;
		double walkCorrect = -0.0625;

		// Left side
		double xcLeft = ((xLeft / 2) - (this.right * rightCorrect)) * 2.0;
		double zcLeft = ((zDistanceLeft / 2) - (this.forward * forwardCorrect)) * 2.0;

		double rotLeftSideX = xcLeft * this.cosine - zcLeft * this.sine;
		double yCornerTL = ((-yHeight) - (-this.up * upCorrect + (this.walking * walkCorrect))) * 2.0;
		double yCornerBL = ((+0.5 - yHeight) - (-this.up * upCorrect + (this.walking * walkCorrect))) * 2.0;
		double rotLeftSideZ = zcLeft * this.cosine + xcLeft * this.sine;

		// Right side
		double xcRight = ((xRight / 2) - (this.right * rightCorrect)) * 2.0;
		double zcRight = ((zDistanceRight / 2) - (this.forward * forwardCorrect)) * 2.0;

		double rotRightSideX = xcRight * this.cosine - zcRight * this.sine;
		double yCornerTR = ((-yHeight) - (-this.up * upCorrect + (this.walking * walkCorrect))) * 2.0;
		double yCornerBR = ((+0.5 - yHeight) - (-this.up * upCorrect + (this.walking * walkCorrect))) * 2.0;
		double rotRightSideZ = zcRight * this.cosine + xcRight * this.sine;

		double tex30 = 0;
		double tex40 = 8;
		double clip = 0.5;

		if (rotLeftSideZ < clip && rotRightSideZ < clip) {
			return;
		}

		if (rotLeftSideZ < clip) {
			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotLeftSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex30 = tex30 + (tex40 - tex30) * clip0;
		}

		if (rotRightSideZ < clip) {
			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotRightSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex40 = tex30 + (tex40 - tex30) * clip0;
		}

		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * this.height + this.width / 2.0);
		double xPixelRight = (rotRightSideX / rotRightSideZ * this.height + this.width / 2.0);

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

		double yPixelLeftTop = yCornerTL / rotLeftSideZ * this.height + this.height / 2.0;
		double yPixelLeftBottom = yCornerBL / rotLeftSideZ * this.height + this.height / 2.0;
		double yPixelRightTop = yCornerTR / rotRightSideZ * this.height + this.height / 2.0;
		double yPixelRightBottom = yCornerBR / rotRightSideZ * this.height + this.height / 2.0;

		double tex1 = 1 / rotLeftSideZ;
		double tex2 = 1 / rotRightSideZ;
		double tex3 = tex30 / rotLeftSideZ;
		double tex4 = tex40 / rotRightSideZ - tex3;

		for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
			double zWall = (tex1 + (tex2 - tex1) * pixelRotation);

			if (this.zBufferWall[x] > zWall) {
				continue;
			}
			this.zBufferWall[x] = zWall;

			int xTexture = (int) ((tex3 + tex4 * pixelRotation) / zWall);

			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

			int yPixelTopInt = (int) yPixelTop;
			int yPixelBottomInt = (int) yPixelBottom;

			if (yPixelTopInt < 0) {
				yPixelTopInt = 0;
			}

			if (yPixelBottomInt > this.height) {
				yPixelBottomInt = this.height;
			}

			for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
				double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);

				int yTexture = (int) (8 * pixelRotationY);

				this.pixels[x + y * this.width] = Texture.floor.pixels[((xTexture & 7) + 8) + (yTexture & 7) * 16];
				this.zBuffer[x + y * this.width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 8;
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
