package particle_gravity_simulation.objects;

import a.geometry.AVector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class StationaryParticle implements WorldObject{
	private int r;
	private AVector position;
	private int mass;
	int isPositive; // <0 negative, ==0 neutral, >0 positive
	
	public StationaryParticle(int x, int y, int mass, int r,int isPositive){
		this.r=r;
		this.isPositive = isPositive;
		this.position =new AVector(x,y); 
	    this.mass=mass;
	}
	public StationaryParticle(){
		this.position =new AVector(100,100);
	    this.mass=2;
	    this.isPositive=0;
	}
	
	public int getMass() {
		return mass;
	}
	public void setMass(int mass) {
		this.mass = mass;
	}
	public int getIsPositive() {
		return isPositive;
	}
	public void setIsPositive(int isPositive) {
		this.isPositive = isPositive;
	}
	@Override
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	@Override
	public AVector getPosition() {
		return this.position;
		
	}
	@Override
	public void setPosition(AVector position) {
		
	}
	
	//vuoto perchè non ha modo di muoversi applicando forze
	//si potrebbe mettere dentro un collision detection se venisse implementato
	@Override
	public void update(AVector force) {
		// TODO Auto-generated method stub
		
	}
	
	//come disegnare la particella
	@Override
	public void draw(GraphicsContext g) {
		
		//colore in base alle impostazioni date
		if(this.getIsPositive()>0)
			g.setFill(Color.RED);
				else 
					if(this.getIsPositive()<0)
						g.setFill(Color.BLUE);
						else
							g.setFill(Color.GRAY);
				
				
			//riempo ovale con posizioni della particella (le casto in int perchè sono double) e con altezza e larghezza uguali a raggio^2
			g.fillOval((int)this.getPosition().getX(), (int)this.getPosition().getY(), this.getR()*2, this.getR()*2);
		
	}
	@Override
	public AVector getVelocity() {
		// TODO Auto-generated method stub
		return new AVector();
	}
	@Override
	public void setVelocity(AVector velocity) {
		// TODO Auto-generated method stub
		
	}

	
	


	


	
}
