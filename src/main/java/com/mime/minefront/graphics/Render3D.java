package main.java.com.mime.minefront.graphics;

import com.mime.minefront.Game;
import com.mime.minefront.level.Block;
import com.mime.minefront.level.Level;
import main.java.com.mime.minefront.Display;
import main.java.com.mime.minefront.entity.mob.Player;

public class Render3D extends Render {

  public double[] zBuffer;
  public double[] zBufferWall;
  private final double renderDistance = 5000;
  private double forward, right, up, cosine, sine, walking;
  private final int spriteSheetWidth = 128;

  int c = 0;

  double h = 0.5;

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
    this.forward = game.player.z;
    this.right = game.player.x;
    this.up = game.player.y;
    this.walking = 0.0;
    double rotation = game.player.rotation; // Math.sin(game.time / 10.0) / 10;
    this.cosine = Math.cos(rotation);
    this.sine = Math.sin(rotation);

    for (int y = 0; y < this.height; y++) {
      double ceiling = (y + -this.height / 2.0) / this.height;
      double z = (floorPosition + this.up) / ceiling;
      this.c = 0;
      if (Player.walk) {
        this.walking = Math.sin((game.time / 6.0) * 0.5);
        z = (floorPosition + this.up + this.walking) / ceiling;
      }

      if (Player.crouchWalk && Player.walk) {
        this.walking = Math.sin((game.time / 6.0) * 0.25);
        z = (floorPosition + this.up + this.walking) / ceiling;
      }
      if (Player.runWalk && Player.walk) {
        this.walking = Math.sin((game.time / 6.0) * 0.8);
        z = (floorPosition + this.up + this.walking) / ceiling;
      }

      if (ceiling < 0) {
        z = (ceilingPosition - this.up) / -ceiling;
        this.c = 1;
        if (Player.walk) {
          z = (ceilingPosition - this.up - this.walking) / -ceiling;
        }
      }

      for (int x = 0; x < this.width; x++) {
        double depth = (x - this.width / 2.0) / this.height;
        depth *= z;
        double xx = depth * this.cosine + z * this.sine;
        double yy = z * this.cosine - depth * this.sine;
        int xPix = (int) ((xx + this.right) * 4);
        int yPix = (int) ((yy + this.forward) * 4);
        this.zBuffer[x + y * this.width] = z;
        if (this.c == 0) {
          this.pixels[x + y * this.width] = com.mime.minefront.graphics.Texture.block.pixels[(xPix & 30) + 32
              + (yPix & 30) * this.spriteSheetWidth];
        } else {
          this.pixels[x + y * this.width] = com.mime.minefront.graphics.Texture.block.pixels[(xPix & 30)
              + (yPix & 30) * this.spriteSheetWidth];
        }

        if (z > 500) {
          this.pixels[x + y * this.width] = 0;
        }
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

    for (int xBlock = -size; xBlock <= size; xBlock++) {
      for (int zBlock = -size; zBlock <= size; zBlock++) {
        Block block = level.create(xBlock, zBlock);
        for (int s = 0; s < block.sprites.size(); s++) {
          com.mime.minefront.graphics.Sprite sprite = block.sprites.get(s);

          this.renderSprite(xBlock + sprite.x, sprite.y, zBlock + sprite.z, 0.5);
        }
      }
    }
  }

  public void renderSprite(double x, double y, double z, double hoffset) {
    double upCorrect = -0.125;
    double rightCorrect = 0.0625;
    double forwardCorrect = 0.0625;
    double walkCorrect = 0.0625;

    double xc = ((x / 2) - (this.right * rightCorrect)) * 2;
    double yc = ((y / 2) - (this.up * upCorrect)) + (this.walking * walkCorrect) * 2 + hoffset;
    double zc = ((z / 2) - (this.forward * forwardCorrect)) * 2;

    double rotX = xc * this.cosine - zc * this.sine;
    double rotZ = zc * this.cosine + xc * this.sine;

    double xCentre = (double) Display.width / 2; // 400.0;
    double yCentre = (double) Display.height / 2; // 300.0;

    double xPixel = rotX / rotZ * this.height + xCentre;
    double yPixel = yc / rotZ * this.height + yCentre;

    double xPixelL = xPixel - (double) this.height / 2 / rotZ;
    double xPixelR = xPixel + (double) this.height / 2 / rotZ;

    double yPixelL = yPixel - (double) this.height / 2 / rotZ;
    double yPixelR = yPixel + (double) this.height / 2 / rotZ;

    int xpl = (int) xPixelL;
    int xpr = (int) xPixelR;
    int ypl = (int) yPixelL;
    int ypr = (int) yPixelR;

    if (xpl < 0) {
      xpl = 0;
    }

    if (xpr > this.width) {
      xpr = this.width;
    }

    if (ypl < 0) {
      ypl = 0;
    }

    if (ypr > this.height) {
      ypr = this.height;
    }

    rotZ *= 8;

    for (int yp = ypl; yp < ypr; yp++) {

      double pixelRotationY = (yp - yPixelR) / (yPixelL - yPixelR);
      int yTexture = (int) (pixelRotationY * 8 * 4);

      for (int xp = xpl; xp < xpr; xp++) {
        double pixelRotationX = (xp - xPixelR) / (xPixelL - xPixelR);
        int xTexture = (int) (pixelRotationX * 8 * 4);

        if (this.zBuffer[xp + yp * this.width] > rotZ) {
          int col = com.mime.minefront.graphics.Texture.block.pixels[((xTexture & 30) + 96) + (yTexture & 30) * this.spriteSheetWidth];
          if (col != 0xFFFF00FF) {
            this.pixels[xp + yp * this.width] = col;
            this.zBuffer[xp + yp * this.width] = rotZ;
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
    double yCornerBL = ((0.5 - yHeight) - (-this.up * upCorrect + (this.walking * walkCorrect))) * 2.0;
    double rotLeftSideZ = zcLeft * this.cosine + xcLeft * this.sine;

    // Right side
    double xcRight = ((xRight / 2) - (this.right * rightCorrect)) * 2.0;
    double zcRight = ((zDistanceRight / 2) - (this.forward * forwardCorrect)) * 2.0;

    double rotRightSideX = xcRight * this.cosine - zcRight * this.sine;
    double yCornerTR = ((-yHeight) - (-this.up * upCorrect + (this.walking * walkCorrect))) * 2.0;
    double yCornerBR = ((0.5 - yHeight) - (-this.up * upCorrect + (this.walking * walkCorrect))) * 2.0;
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

      int xTexture = (int) ((tex3 + tex4 * pixelRotation) / zWall * 4);

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

        int yTexture = (int) (8 * pixelRotationY * 4);

        this.pixels[x + y * this.width] = Texture.block.pixels[((xTexture & 31) + 64)
            + (yTexture & 31) * this.spriteSheetWidth];
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
