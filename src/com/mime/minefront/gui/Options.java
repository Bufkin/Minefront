package com.mime.minefront.gui;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.mime.minefront.Display;

public class Options extends Launcher {

	private static final long serialVersionUID = -7743258581038005972L;

	private Choice resolution = new Choice();

	private int width = 540;
	private int height = 450;
	private JButton OK;
	private Rectangle rOK, rresolution;

	public Options() {
		super(1);
		this.setTitle("Options - Minefront Launcher");
		this.setSize(new Dimension(this.width, this.height));
		this.setLocationRelativeTo(null);

		this.drawButtons();
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

		this.OK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Display.selection = Options.this.resolution.getSelectedIndex();
				Options.this.dispose();
				new Launcher(0);
			}
		});
	}
}
