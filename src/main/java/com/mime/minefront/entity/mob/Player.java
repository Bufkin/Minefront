package main.java.com.mime.minefront.entity.mob;

import com.mime.minefront.Display;
import com.mime.minefront.input.InputHandler;

public class Player extends Mob {

	public double y, rotation, xa, za, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	public static boolean walk = false;
	public static boolean crouchWalk = false;
	public static boolean runWalk = false;

	private final InputHandler input;

	public Player(InputHandler input) {
		this.input = input;
	}

	@Override
	public void tick() {
		double rotationSpeed = 0.002 * Display.MouseSpeed;
		double walkSpeed = 0.5;
		double jumpHeight = 0.5;
		double crouchHeight = 0.35;
		int xa = 0;
		int za = 0;

		if (this.input.forward) {
			za++;
			walk = true;
		}

		if (this.input.back) {
			za--;
			walk = true;
		}

		if (this.input.left) {
			xa--;
			walk = true;
		}

		if (this.input.right) {
			xa++;
			walk = true;
		}

		if (xa != 0 || za != 0) {
			this.move(xa, za, this.rotation);
		}
		this.rotation += this.rotationa;
		this.rotationa *= 0.5;

		if (turnLeft) {
			this.rotationa -= rotationSpeed;
		}

		if (turnRight) {
			this.rotationa += rotationSpeed;
		}

		if (this.input.jump) {
			this.y += jumpHeight;
		}

		if (this.input.crouch) {
			this.y -= crouchHeight;
			this.input.run = false;
			crouchWalk = true;
			walkSpeed = 0.2;
		}

		if (this.input.run) {
			walkSpeed = 1;
			walk = true;
			runWalk = true;
		}

		if (!this.input.forward && !this.input.back && !this.input.left && !this.input.right && !this.input.run) {
			walk = false;
		}

		if (!this.input.crouch) {
			crouchWalk = false;
		}

		if (!this.input.run) {
			runWalk = false;
		}

		this.y *= 0.9;
	}
}
