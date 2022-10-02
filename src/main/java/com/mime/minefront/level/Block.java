package main.java.com.mime.minefront.level;

import main.java.com.mime.minefront.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Block {

  public boolean solid = false;

  public static Block solidWall = new SolidBlock();

  public List<Sprite> sprites = new ArrayList<>();

  public void addSprite(Sprite sprite) {
    this.sprites.add(sprite);
  }

}
