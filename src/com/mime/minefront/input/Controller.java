package com.mime.minefront.input;

public class Controller {

	public double x, y, z, rotation, xa, za, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	public static boolean walk = false;
	public static boolean crouchWalk = false;
	public static boolean runWalk = false;

	public void tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch,
			boolean run) {
		double rotationSpeed = 0.025;
		double walkSpeed = 0.5;
		double jumpHeight = 0.5;
		double crouchHeight = 0.35;
		double xMove = 0;
		double zMove = 0;

		if (forward) {
			zMove++;
			walk = true;
		}

		if (back) {
			zMove--;
			walk = true;
		}

		if (left) {
			xMove--;
			walk = true;
		}

		if (right) {
			xMove++;
			walk = true;
		}

		if (turnLeft) {
			this.rotationa -= rotationSpeed;
		}

		if (turnRight) {
			this.rotationa += rotationSpeed;
		}

		if (jump) {
			this.y += jumpHeight;
		}

		if (crouch) {
			this.y -= crouchHeight;
			run = false;
			crouchWalk = true;
			walkSpeed = 0.2;
		}

		if (run) {
			walkSpeed = 1;
			walk = true;
			runWalk = true;
		}

		if (!forward && !back && !left && !right && !turnLeft && !turnRight && !run) {
			walk = false;
		}

		if (!crouch) {
			crouchWalk = false;
		}

		if (!run) {
			runWalk = false;
		}

		this.xa += (xMove * Math.cos(this.rotation) + zMove * Math.sin(this.rotation)) * walkSpeed;
		this.za += (zMove * Math.cos(this.rotation) + xMove * Math.sin(this.rotation)) * walkSpeed;

		this.x += this.xa;
		this.y *= 0.9;
		this.z += this.za;
		this.xa *= 0.1;
		this.za *= 0.1;
		this.rotation += this.rotationa;
		this.rotationa *= 0.5;
	}
}
