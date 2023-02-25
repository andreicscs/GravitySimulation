package particle_gravity_simulation.objects;

import java.awt.Graphics;

import a.geometry.AVector;

interface WorldObject {

	int getMass();
	void setMass(int mass);
	int getIsPositive();
	void setIsPositive(int isPositive);
	AVector getPosition();
	void setPosition(AVector position);
	void update(AVector force);
	void draw(Graphics g);
}
