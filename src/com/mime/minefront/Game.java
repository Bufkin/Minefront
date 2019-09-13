package com.mime.minefront;

import java.awt.event.KeyEvent;

import com.mime.minefront.input.Controller;

public class Game {
	public int time;
	public Controller controls;

	public Game() {
		this.controls = new Controller();
	}

	public void tick(boolean[] key) {
		this.time++;
		boolean forward = key[KeyEvent.VK_W];
		boolean back = key[KeyEvent.VK_S];
		boolean left = key[KeyEvent.VK_A];
		boolean right = key[KeyEvent.VK_D];
		boolean jump = key[KeyEvent.VK_SPACE];
		boolean crouch = key[KeyEvent.VK_CONTROL];
		boolean run = key[KeyEvent.VK_SHIFT];

		this.controls.tick(forward, back, left, right, jump, crouch, run);
	}
}
