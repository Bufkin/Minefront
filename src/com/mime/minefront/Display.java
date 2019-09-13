package com.mime.minefront;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.mime.minefront.graphics.Screen;
import com.mime.minefront.input.Controller;
import com.mime.minefront.input.InputHandler;

public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = -346504722713325956L;

	public final static int WIDTH = 800;
	public final static int HEIGHT = 600;
	public static final String TITLE = "Minefront Pre-Alpha 0.02";

	private Thread thread;
	private Screen screen;
	private Game game;
	private BufferedImage img;
	private boolean running = false;
	private int[] pixels;
	private InputHandler input;
	private int newX = 0;
	private int newY = 0;
	private int oldX = 0;
	private int oldY = 0;
	private int fps;

	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);

		this.screen = new Screen(Display.WIDTH, Display.HEIGHT);
		this.game = new Game();
		this.img = new BufferedImage(Display.WIDTH, Display.HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.pixels = ((DataBufferInt) this.img.getRaster().getDataBuffer()).getData();

		this.input = new InputHandler();
		this.addKeyListener(this.input);
		this.addFocusListener(this.input);
		this.addMouseListener(this.input);
		this.addMouseMotionListener(this.input);
	}

	private void start() {
		if (this.running) {
			return;
		}
		this.running = true;
		this.thread = new Thread(this);
		this.thread.start();
	}

	private void stop() {
		if (!this.running) {
			return;
		}
		this.running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;

		while (this.running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			this.requestFocus();

			while (unprocessedSeconds > secondsPerTick) {
				this.tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					this.fps = frames;
					previousTime += 1000;
					frames = 0;
				}
			}
			if (ticked) {
				this.render();
				frames++;
			}
			this.render();
			frames++;

			this.newX = InputHandler.MouseX;
			this.newY = InputHandler.MouseY;

			if (this.newX > this.oldX) {
				Controller.turnRight = true;
			}
			if (this.newX < this.oldX) {
				Controller.turnLeft = true;
			}
			if (this.newX == this.oldX) {
				Controller.turnLeft = false;
				Controller.turnRight = false;
			}
			this.oldX = this.newX;
		}
	}

	private void tick() {
		this.game.tick(this.input.key);
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		this.screen.render(this.game);

		for (int i = 0; i < Display.WIDTH * Display.HEIGHT; i++) {
			this.pixels[i] = this.screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(this.img, 0, 0, Display.WIDTH, Display.HEIGHT, null);
		g.setFont(new Font("Verdana", 2, 50));
		g.setColor(Color.YELLOW);
		g.drawString(this.fps + " FPS", 20, 50);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.pack();
		frame.getContentPane().setCursor(blank);
		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		game.start();
	}
}