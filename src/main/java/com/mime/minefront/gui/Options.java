package com.mime.minefront.gui;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mime.minefront.Configuration;

public class Options extends JFrame {

	private static final long serialVersionUID = -7743258581038005972L;

	private final int width = 550;
	private final int height = 450;
	private JButton OK;
	private JTextField twidth, theight;
	private JLabel lwidth, lheight;
	private Rectangle rOK, rresolution;
	private Choice resolution = new Choice();
	Configuration config = new Configuration();

	int w = 0;
	int h = 0;
	private final int button_width = 80;
	private final int button_height = 40;

	JFrame frame = new JFrame();
	JPanel window = new JPanel();

	public Options() {
		this.setTitle("Options - Minefront Launcher");
		this.setSize(new Dimension(this.width, this.height));
		this.add(this.window);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		this.window.setLayout(null);

		this.drawButtons();
		this.window.repaint();
	}

	private void drawButtons() {
		this.OK = new JButton("OK");
		this.rOK = new Rectangle(this.width - 100, this.height - 70, this.button_width, this.button_height - 10);
		this.OK.setBounds(this.rOK);
		this.window.add(this.OK);

		this.rresolution = new Rectangle(50, 80, 80, 25);
		this.resolution.setBounds(this.rresolution);
		this.resolution.add("640 x 480");
		this.resolution.add("800 x 600");
		this.resolution.add("1024 x 768");
		this.resolution.select(1);
		this.window.add(this.resolution);

		this.lwidth = new JLabel("Width: ");
		this.lwidth.setBounds(30, 150, 120, 20);
		this.window.add(this.lwidth);

		this.lheight = new JLabel("Height: ");
		this.lheight.setBounds(30, 180, 120, 20);
		this.window.add(this.lheight);

		this.twidth = new JTextField();
		this.twidth.setBounds(80, 150, 60, 20);
		this.window.add(this.twidth);

		this.theight = new JTextField();
		this.theight.setBounds(80, 180, 60, 20);
		this.window.add(this.theight);

		this.OK.addActionListener(e -> {
			Options.this.dispose();
			new Launcher(0);
			Options.this.config.saveConfiguration("width", Options.this.parseWidth());
			Options.this.config.saveConfiguration("height", Options.this.parseHeight());
		});
	}

	private void drop() {
		int selection = Options.this.resolution.getSelectedIndex();

		if (selection == 0) {
			this.w = 640;
			this.h = 480;
		}
		if (selection == 1 || selection == -1) {
			this.w = 800;
			this.h = 600;
		}
		if (selection == 2) {
			this.w = 1024;
			this.h = 768;
		}

	}

	private int parseWidth() {
		try {
			return Integer.parseInt(this.twidth.getText());
		} catch (NumberFormatException e) {
			this.drop();
			return this.w;
		}
	}

	private int parseHeight() {
		try {
			return Integer.parseInt(this.theight.getText());
		} catch (NumberFormatException e) {
			this.drop();
			return this.h;
		}
	}

}
