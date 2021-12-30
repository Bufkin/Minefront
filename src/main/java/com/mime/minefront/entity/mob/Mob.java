package com.mime.minefront.entity.mob;

import com.mime.minefront.entity.Entity;

public class Mob extends Entity {

	public void move(int xa, int za, double rot) {
		if (xa != 0 && za != 0) {
			this.move(xa, 0, rot);
			this.move(0, za, rot);
			return;
		}

		double nx = xa * Math.cos(rot) + za * Math.sin(rot);
		double nz = za * Math.cos(rot) - xa * Math.sin(rot);

		this.x += nx;
		this.z += nz;
	}
}
