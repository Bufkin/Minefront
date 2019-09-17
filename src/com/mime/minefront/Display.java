package com.mime.minefront;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.mime.minefront.entity.mob.Player;
import com.mime.minefront.graphics.Screen;
import com.mime.minefront.gui.Launcher;
import com.mime.minefront.input.InputHandler;

public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = -346504722713325956L;

	public static int width = 800;
	public static int height = 600;
	public static final String TITLE = "Minefront Pre-Alpha 0.02";

	private Thread thread;
	private Screen screen;
	private Game game;
	private BufferedImage img;
	private boolean running = false;
	private int[] pixels;
	private InputHandler input;
	private int newX = 0;
	private int oldX = 0;
	private int fps;
	public static int selection = 0;
	public static int MouseSpeed;

	static Launcher launcher;

	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);

		this.screen = new Screen(Display.getGameWidth(), Display.getGameHeight());
		this.input = new InputHandler();
		this.game = new Game(this.input);
		this.img = new BufferedImage(Display.getGameWidth(), Display.getGameHeight(), BufferedImage.TYPE_INT_RGB);
		this.pixels = ((DataBufferInt) this.img.getRaster().getDataBuffer()).getData();

		this.addKeyListener(this.input);
		this.addFocusListener(this.input);
		this.addMouseListener(this.input);
		this.addMouseMotionListener(this.input);
	}

	public static Launcher getLauncherInstance() {
		if (launcher == null) {
			launcher = new Launcher(0);
		}
		return launcher;
	}

	public static int getGameWidth() {
		return width;
	}

	public static int getGameHeight() {
		return height;
	}

	public synchronized void start() {
		if (this.running) {
			return;
		}
		this.running = true;
		this.thread = new Thread(this, "game");
		this.thread.start();
	}

	private synchronized void stop() {
		if (!this.running) {
			return;
		}
		this.running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		long previousTime = System.nanoTime();
		double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		this.requestFocus();

		while (this.running) {
			long currentTime = System.nanoTime();
			delta += (currentTime - previousTime) / ns;
			previousTime = currentTime;

			if (delta >= 1) {
				this.tick();
				delta--;
			}

			this.render();
			frames++;

			while (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				this.fps = frames;
				frames = 0;
			}
		}
	}

	private void tick() {
		this.input.tick();
		this.game.tick();

		this.newX = InputHandler.MouseX;
		if (this.newX > this.oldX) {
			Player.turnRight = true;
		}
		if (this.newX < this.oldX) {
			Player.turnLeft = true;
		}
		if (this.newX == this.oldX) {
			Player.turnLeft = false;
			Player.turnRight = false;
		}
		MouseSpeed = Math.abs(this.newX - this.oldX);
		this.oldX = this.newX;
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		this.screen.render(this.game);

		for (int i = 0; i < Display.getGameWidth() * Display.getGameHeight(); i++) {
			this.pixels[i] = this.screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(this.img, 0, 0, Display.getGameWidth(), Display.getGameHeight(), null);
		g.setFont(new Font("Verdana", 2, 50));
		g.setColor(Color.YELLOW);
		g.drawString(this.fps + " FPS", 20, 50);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		getLauncherInstance();
	}
}
