package main.java.com.mime.minefront.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.mime.minefront.Configuration;
import com.mime.minefront.RunGame;
import com.mime.minefront.input.InputHandler;

public class Launcher extends Canvas implements Runnable {

	@Serial
	private static final long serialVersionUID = 6031394679183022710L;

	protected JPanel window = new JPanel();
	Configuration config = new Configuration();

	private final int width = 800;
	protected int button_width = 80;
	protected int button_height = 40;
	boolean running = false;
	Thread thread;
	JFrame frame = new JFrame();

	public Launcher(int id) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.frame.setUndecorated(true);
		this.frame.setTitle("Minefront Launcher");
		int height = 400;
		this.frame.setSize(new Dimension(this.width, height));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().add(this.window);
		this.frame.add(this);
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
		this.window.setLayout(null);
		if (id == 0) {
			this.drawButtons();
		}
		InputHandler input = new InputHandler();
		this.addKeyListener(input);
		this.addFocusListener(input);
		this.addMouseListener(input);
		this.addMouseMotionListener(input);
		this.startMenu();
		this.frame.repaint();
	}

	public void updateFrame() {
		if (InputHandler.dragged) {
			Point p = this.frame.getLocation();
			this.setLocation(p.x + InputHandler.MouseDX - InputHandler.MousePX,
					p.y + InputHandler.MouseDY - InputHandler.MousePY);
		}
	}

	public void startMenu() {
		this.running = true;
		this.thread = new Thread(this, "menu");
		this.thread.start();
	}

	public void stopMenu() {
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (this.running) {
			try {
				this.renderMenu();
			} catch (IllegalStateException e) {
				System.out.println("Handled, baby!");
			}
			this.updateFrame();
		}
	}

	private void renderMenu() throws IllegalStateException {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 400);
		try {
			g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu_image.jpg"))), 0, 0, 800, 400, null);
			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 && InputHandler.MouseY > 130
					&& InputHandler.MouseY < 130 + 30) {
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/play_on.png"))), 690, 130, 80, 30, null);
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/arrow.png"))), 690 + 80, 134, 22, 20, null);
				if (InputHandler.MouseButton == 1) {
					this.config.loadConfiguration("res/settings/config.xml");
					this.frame.dispose();
					new RunGame();
				}
			} else {
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/play_off.png"))), 690, 130, 80, 30, null);
			}

			if (InputHandler.MouseX > 641 && InputHandler.MouseX < 641 + 130 && InputHandler.MouseY > 170
					&& InputHandler.MouseY < 170 + 30) {
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/options_on.png"))), 641, 170, 130, 30, null);
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/arrow.png"))), 690 + 80, 174, 22, 20, null);
				if (InputHandler.MouseButton == 1) {
					new com.mime.minefront.gui.Options();
				}
			} else {
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/options_off.png"))), 641, 170, 130, 30, null);
			}

			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 && InputHandler.MouseY > 210
					&& InputHandler.MouseY < 210 + 30) {
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/help_on.png"))), 690, 210, 80, 30, null);
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/arrow.png"))), 690 + 80, 214, 22, 20, null);
			} else {
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/help_off.png"))), 690, 210, 80, 30, null);
			}

			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 && InputHandler.MouseY > 250
					&& InputHandler.MouseY < 250 + 30) {
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/exit_on.png"))), 690, 250, 80, 30, null);
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/arrow.png"))), 690 + 80, 254, 22, 20, null);
				if (InputHandler.MouseButton == 1) {
					System.exit(0);
				}
			} else {
				g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/exit_off.png"))), 690, 250, 80, 30, null);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		g.dispose();
		bs.show();
	}

	private void drawButtons() {
		JButton play = new JButton("Play!");
		Rectangle rplay = new Rectangle((this.width / 2) - (this.button_width / 2), 80, this.button_width,
				this.button_height);
		play.setBounds(rplay);
		this.window.add(play);

		JButton options = new JButton("Options!");
		Rectangle roptions = new Rectangle((this.width / 2) - (this.button_width / 2), 130, this.button_width,
				this.button_height);
		options.setBounds(roptions);
		this.window.add(options);

		JButton help = new JButton("Help!");
		Rectangle rhelp = new Rectangle((this.width / 2) - (this.button_width / 2), 180, this.button_width,
				this.button_height);
		help.setBounds(rhelp);
		this.window.add(help);

		JButton quit = new JButton("Quit!");
		Rectangle rquit = new Rectangle((this.width / 2) - (this.button_width / 2), 230, this.button_width,
				this.button_height);
		quit.setBounds(rquit);
		this.window.add(quit);

		play.addActionListener(e -> {
			Launcher.this.config.loadConfiguration("res/settings/config.xml");
			Launcher.this.frame.dispose();
			new RunGame();
		});

		options.addActionListener(e -> {
			Launcher.this.frame.dispose();
			new Options();
		});

		help.addActionListener(e -> {

		});

		quit.addActionListener(e -> System.exit(0));
	}
}
