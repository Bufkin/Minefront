package main.java.com.mime.minefront;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RunGame {

  public RunGame() {
    BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
    Display game = new Display();
    JFrame frame = new JFrame();
    frame.add(game);
    frame.pack();
    frame.setSize(Display.getGameWidth(), Display.getGameHeight());
    frame.getContentPane().setCursor(blank);
    frame.setTitle(Display.TITLE);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);

    game.start();
    this.stopMenuThread();
  }

  private void stopMenuThread() {
    Display.getLauncherInstance().stopMenu();
  }
}
