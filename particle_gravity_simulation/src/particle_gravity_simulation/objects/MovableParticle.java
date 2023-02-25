package particle_gravity_simulation.objects;

import a.geometry.AVector;
import particle_gravity_simulation.graphics.SimulationPanel;

public class MovableParticle extends StationaryParticle{
	//stationary particle non ha accelerazione e volcità perchè essendo immobile non ne ha bisogno
	private AVector acceleration;
	private AVector velocity;
	
	public MovableParticle(int x, int y, int mass, int r,int isPositive){
		super(x,y,mass,r,isPositive);
		this.acceleration=new AVector();
		this.velocity=new AVector();
		this.setR(r);
	}
	public MovableParticle(){
		super();
		this.acceleration=new AVector();
		this.velocity=new AVector();
	}
	
	public AVector getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(AVector acceleration) {
		this.acceleration = acceleration;
	}
	public AVector getVelocity() {
		return velocity;
	}
	public void setVelocity(AVector velocity) {
		this.velocity = velocity;
	}
	
	//applica la forza passata
	@Override
	public void update(AVector force) {
		this.edge(SimulationPanel.getwidth(),SimulationPanel.getheight()); // controllo se tocca un bordo dello schermo
		
		this.acceleration.add(AVector.div(force, this.getMass()));//aggiungo la forza divisa per la messa, all'accelerazione
		this.velocity.add(this.acceleration);//sommo velocità e accelerazione
		this.getPosition().add(this.velocity);//sommo posizione a velocità
		this.acceleration.mult(0);//resetto accelerazione ogni frame
	}

	public void edge(int scrWidth,int scrHeight) {
		int p =90;
		if(this.getPosition().getX()<=0) {//se la pallina oltrepassa 0(bordo sinistra)
			this.velocity.setX((this.velocity.getX()*-1)); // inverto la velocità (cambiano direzione)
			this.getPosition().setX(this.getPosition().getX()+1); // le palline riescono ad attraversare di poco il muro quindi le sposto di 1 per non farle bloccare
			this.velocity.perc(p);//riduco la velocità el 10%
		}
		else if(this.getPosition().getX()>=scrWidth-this.getR()) {
			this.velocity.setX((this.velocity.getX()*-1));
			this.getPosition().setX(this.getPosition().getX()-1);
			this.velocity.perc(p);
		}
		else if(this.getPosition().getY()<=0) {
			this.velocity.setY((this.velocity.getY()*-1));
			this.getPosition().setY(this.getPosition().getY()+1);
			this.velocity.perc(p);
		}
		else if(this.getPosition().getY()>=scrHeight-this.getR()) {
			this.velocity.setY((this.velocity.getY()*-1));
			this.getPosition().setY(this.getPosition().getY()-1);
			this.velocity.perc(p);
		}
	}
}



