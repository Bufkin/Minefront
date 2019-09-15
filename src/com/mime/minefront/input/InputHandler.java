package com.mime.minefront.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		for (int i = 0; i < this.key.length; i++) {
			this.key[i] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

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
