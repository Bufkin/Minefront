package main.java.com.mime.minefront.graphics;

import com.mime.minefront.Game;

import java.util.Random;

public class Screen extends Render {

  private final Render3D render;

  public Screen(int width, int height) {
    super(width, height);
    Random random = new Random();
    this.render = new Render3D(width, height);
    Render test = new Render(256, 256);
    for (int i = 0; i < 256 * 256; i++) {
      test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
    }
  }

  public void render(Game game) {
    for (int i = 0; i < this.width * this.height; i++) {
      this.pixels[i] = 0;
    }

    this.render.floor(game);
    this.render.renderDistanceLimiter();

    this.draw(this.render, 0, 0);
  }
}
