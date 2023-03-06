package particle_gravity_simulation.objects;

import a.geometry.AVector;
import javafx.scene.canvas.GraphicsContext;

interface WorldObject {

	int getMass();
	void setMass(int mass);
	int getIsPositive();
	void setIsPositive(int isPositive);
	AVector getPosition();
	AVector getVelocity();
	void setVelocity(AVector velocity);
	void setPosition(AVector position);
	void update(AVector force);
	void draw(GraphicsContext g2d);
	int getR();
	
}
