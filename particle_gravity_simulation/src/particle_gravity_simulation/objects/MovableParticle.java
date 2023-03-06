package particle_gravity_simulation.objects;

import java.util.ArrayList;

import a.geometry.AVector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import particle_gravity_simulation.graphics.SimulationControls;
import particle_gravity_simulation.graphics.SimulationPanel;

public class MovableParticle extends StationaryParticle{
	//stationary particle non ha accelerazione e volcità perchè essendo immobile non ne ha bisogno
	private AVector acceleration;
	private AVector velocity;
	private ArrayList<AVector> trail;
	
	public MovableParticle(int x, int y, int mass, int r,int isPositive){
		super(x,y,mass,r,isPositive);
		this.acceleration=new AVector();
		this.velocity=new AVector();
		this.setR(r);
		this.trail=new ArrayList<AVector>();
		this.trail.add(new AVector(this.getPosition().getX(),this.getPosition().getY()));
	}
	public MovableParticle(){
		super();
		this.acceleration=new AVector();
		this.velocity=new AVector();
	}
	@Override
	public void setPosition(AVector position) {
		this.getPosition().copy(position);
		
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
		this.edge((int)SimulationPanel.width,(int)SimulationPanel.height); // controllo se tocca un bordo dello schermo
		
		//scia
		if(SimulationControls.isTrailOn) {
			//controllo che il punto precedente e il corrente siano abbastanza distanti
			//altrimenti si aggiornerebbe ad ogni decimale
			if((this.trail.get(this.trail.size()-1).distance(this.getPosition())>0.1) ) {
				this.trail.add(new AVector(this.getPosition().getX(),this.getPosition().getY()));
			}	
			if(this.trail.size()>100) {
				this.trail.remove(0);
			}
		}else {
			//senza questo clear, una volta disattivata e riattivata la trail, riprende dalla posizione precedente.
			this.trail.clear();
			//una volta cancellata aggiungo il primo elemento, perchè nell'if sopra controllo this.trail.size()-1, 
			//che darebbe IndexOutofBounds se l'array fosse vuoto
			this.trail.add(new AVector(this.getPosition().getX(),this.getPosition().getY()));
		}
		
		
		//aplico forze
		
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
			g.fillOval(this.getPosition().getX(), this.getPosition().getY(), this.getR()*2, this.getR()*2);
			
			
			if(SimulationControls.isTrailOn) {
				//disegno tutte le posizioni salvate in trail creando un effetto scia
				//nella grandezza dell'ovale, i/(this.getR()*2) per rendere l'ovale sempre più piccolo, 
				//ma non più grande della dimensione della particella
				double size=(this.getR()*2);
				
				for(int i=0;i<trail.size();i++) {
					size=i/(this.getR()*2);
					if(size>(this.getR()*2)) {
						size=(this.getR()*2);
					}
					g.fillOval(trail.get(i).getX(), trail.get(i).getY(), size, size);
				}
			}
	}
}



