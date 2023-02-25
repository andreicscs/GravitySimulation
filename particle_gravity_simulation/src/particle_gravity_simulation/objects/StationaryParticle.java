package particle_gravity_simulation.objects;

import java.awt.Color;
import java.awt.Graphics;
import a.geometry.AVector;
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
		this.position.copy(position);
		
	}
	
	//vuoto perchè non ha modo di muoversi applicando forze
	//si potrebbe mettere dentro un collision detection se venisse implementato
	@Override
	public void update(AVector force) {
		// TODO Auto-generated method stub
		
	}
	
	//come disegnare la particella
	@Override
	public void draw(Graphics g) {
		//colore in base alle impostazioni date
		if(this.getIsPositive()>0)
			g.setColor(Color.red);
				else 
					if(this.getIsPositive()<0)
						g.setColor(Color.blue);
						else
							g.setColor(Color.gray);
				
				
			//riempo ovale con posizioni della particella (le casto in int perchè sono double) e con altezza e larghezza uguali a raggio^2
			g.fillOval((int)this.getPosition().getX(), (int)this.getPosition().getY(), this.getR()*2, this.getR()*2);
		
	}

	
	


	


	
}
