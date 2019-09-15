package com.mime.minefront.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.mime.minefront.Configuration;
import com.mime.minefront.RunGame;
import com.mime.minefront.input.InputHandler;

public class Launcher extends Canvas implements Runnable {

	private static final long serialVersionUID = 6031394679183022710L;

	protected JPanel window = new JPanel();
	private JButton play, options, help, quit;
	private Rectangle rplay, roptions, rhelp, rquit;
	Configuration config = new Configuration();

	private int width = 800;
	private int height = 400;
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
		this.frame.setSize(new Dimension(this.width, this.height));
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
			g.drawImage(ImageIO.read(Launcher.class.getResource("/menu_image.jpg")), 0, 0, 800, 400, null);
			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 && InputHandler.MouseY > 130
					&& InputHandler.MouseY < 130 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/play_on.png")), 690, 130, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 690 + 80, 134, 22, 20, null);
				if (InputHandler.MouseButton == 1) {
					this.config.loadConfiguration("res/settings/config.xml");
					this.frame.dispose();
					new RunGame();
				}
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/play_off.png")), 690, 130, 80, 30, null);
			}

			if (InputHandler.MouseX > 641 && InputHandler.MouseX < 641 + 130 && InputHandler.MouseY > 170
					&& InputHandler.MouseY < 170 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/options_on.png")), 641, 170, 130, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 690 + 80, 174, 22, 20, null);
				if (InputHandler.MouseButton == 1) {
					new Options();
				}
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/options_off.png")), 641, 170, 130, 30, null);
			}

			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 && InputHandler.MouseY > 210
					&& InputHandler.MouseY < 210 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/help_on.png")), 690, 210, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 690 + 80, 214, 22, 20, null);
				if (InputHandler.MouseButton == 1) {

				}
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/help_off.png")), 690, 210, 80, 30, null);
			}

			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 && InputHandler.MouseY > 250
					&& InputHandler.MouseY < 250 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/exit_on.png")), 690, 250, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 690 + 80, 254, 22, 20, null);
				if (InputHandler.MouseButton == 1) {
					System.exit(0);
				}
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/exit_off.png")), 690, 250, 80, 30, null);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		g.dispose();
		bs.show();
	}

	private void drawButtons() {
		this.play = new JButton("Play!");
		this.rplay = new Rectangle((this.width / 2) - (this.button_width / 2), 80, this.button_width,
				this.button_height);
		this.play.setBounds(this.rplay);
		this.window.add(this.play);

		this.options = new JButton("Options!");
		this.roptions = new Rectangle((this.width / 2) - (this.button_width / 2), 130, this.button_width,
				this.button_height);
		this.options.setBounds(this.roptions);
		this.window.add(this.options);

		this.help = new JButton("Help!");
		this.rhelp = new Rectangle((this.width / 2) - (this.button_width / 2), 180, this.button_width,
				this.button_height);
		this.help.setBounds(this.rhelp);
		this.window.add(this.help);

		this.quit = new JButton("Quit!");
		this.rquit = new Rectangle((this.width / 2) - (this.button_width / 2), 230, this.button_width,
				this.button_height);
		this.quit.setBounds(this.rquit);
		this.window.add(this.quit);

		this.play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.this.config.loadConfiguration("res/settings/config.xml");
				Launcher.this.frame.dispose();
				new RunGame();
			}
		});

		this.options.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.this.frame.dispose();
				new Options();
			}
		});

		this.help.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		this.quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

}
