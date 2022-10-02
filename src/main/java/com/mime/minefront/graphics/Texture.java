package main.java.com.mime.minefront.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Texture {

  public static Render block = loadBitMap("/textures/blocks.png");

  public static Render loadBitMap(String fileName) {

    try {
      BufferedImage image = ImageIO.read(Objects.requireNonNull(Texture.class.getResource(fileName)));
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