package com.mime.minefront.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.mime.minefront.Configuration;
import com.mime.minefront.RunGame;

public class Launcher extends JFrame {

	private static final long serialVersionUID = 6031394679183022710L;

	protected JPanel window = new JPanel();
	private JButton play, options, help, quit;
	private Rectangle rplay, roptions, rhelp, rquit;
	Configuration config = new Configuration();

	private int width = 240;
	private int height = 320;
	protected int button_width = 80;
	protected int button_height = 40;

	public Launcher(int id) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setTitle("Minefront Launcher");
		this.setSize(new Dimension(this.width, this.height));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().add(this.window);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		this.window.setLayout(null);

		if (id == 0) {
			this.drawButtons();
		}
		this.repaint();
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
				Launcher.this.dispose();
				new RunGame();
			}
		});

		this.options.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.this.dispose();
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
