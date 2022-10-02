package main.java.com.mime.minefront.input;

import java.awt.event.*;
import java.util.Arrays;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener {

  public boolean[] key = new boolean[68836];
  public static int MouseX;
  public static int MouseY;
  public static int MouseDX; // d = drag
  public static int MouseDY;
  public static int MousePX; // p = mouse pressed
  public static int MousePY;
  public static boolean dragged = false;
  public static int MouseButton;

  public boolean forward, back, left, right, jump, crouch, run;

  public void tick() {
    this.forward = this.key[KeyEvent.VK_W];
    this.back = this.key[KeyEvent.VK_S];
    this.left = this.key[KeyEvent.VK_A];
    this.right = this.key[KeyEvent.VK_D];
    this.jump = this.key[KeyEvent.VK_SPACE];
    this.crouch = this.key[KeyEvent.VK_CONTROL];
    this.run = this.key[KeyEvent.VK_SHIFT];
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    MouseDX = e.getX();
    MouseDY = e.getY();
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    MouseX = e.getX();
    MouseY = e.getY();
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    MouseButton = e.getButton();
    MousePX = e.getX();
    MousePY = e.getY();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    dragged = false;
    MouseButton = 0;
  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  @Override
  public void focusGained(FocusEvent e) {

  }

  @Override
  public void focusLost(FocusEvent e) {
    Arrays.fill(this.key, false);
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if (keyCode > 0 && keyCode < this.key.length) {
      this.key[keyCode] = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if (keyCode > 0 && keyCode < this.key.length) {
      this.key[keyCode] = false;
    }
  }
}
