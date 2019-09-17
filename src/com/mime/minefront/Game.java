package com.mime.minefront;

import com.mime.minefront.entity.mob.Player;
import com.mime.minefront.input.InputHandler;
import com.mime.minefront.level.Level;

public class Game {
	public int time;
	public Player player;
	public Level level;

	public Game(InputHandler input) {
		this.player = new Player(input);
		this.level = new Level(8, 8);
		this.level.addEntity(this.player);
	}

	public void tick() {
		this.time++;
		this.level.tick();
	}
}
