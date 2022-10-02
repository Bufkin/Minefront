package main.java.com.mime.minefront.entity;

public class Entity {

  public double x, z;
  protected boolean removed = false;

  protected Entity() {

  }

  public void remove() {
    this.removed = true;
  }

  public void tick() {

  }
}
