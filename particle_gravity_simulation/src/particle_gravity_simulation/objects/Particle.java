package particle_gravity_simulation.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import a.geometry.AVector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import particle_gravity_simulation.graphics.SimulationControls;
import particle_gravity_simulation.graphics.SimulationPanel;

public class Particle implements WorldObject{
	private int r;
	private int mass;
	private int isPositive; // <0 negative, ==0 neutral, >0 positive
	private boolean isMovable;
	private AVector position;
	private AVector acceleration;
	private AVector velocity;
	public static final Semaphore trailSemaphore = new Semaphore(1);
	private List<AVector> trail;//thread safe array
	
	public Particle(double x, double y, int mass, int r,int isPositive, boolean isMovable){
		this.isMovable=isMovable;
		this.r=r;
		this.isPositive = isPositive;
		this.position =new AVector(x,y); 
	    this.mass=mass;
		this.acceleration=new AVector();
		this.velocity=new AVector();
		this.setR(r);
		trail = Collections.synchronizedList(new ArrayList<AVector>());
		this.trail.add(new AVector(this.getPosition().getX(),this.getPosition().getY()));
	}

	public Particle(){
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
		if(isMovable()) {
			this.getPosition().copy(position);	
		}
	}
	
	public AVector getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(AVector acceleration) {
		if(isMovable()) {
			this.acceleration = acceleration;
		}
	}
	public AVector getVelocity() {
		return velocity;
	}
	public void setVelocity(AVector velocity) {
		if(isMovable()) {
			this.velocity = velocity;
		}
	}
	public boolean isMovable() {
		return isMovable;
	}

	public void setMovable(boolean isMovable) {
		this.isMovable = isMovable;
	}

	public List<AVector> getTrail() {
		return trail;
	}

	public void setTrail(ArrayList<AVector> trail) {
		this.trail = trail;
	}
	
	
	//applica la forza passata
	@Override
	public void update(AVector force) {
		if(isMovable()) {
			this.edge((int)SimulationPanel.width,(int)SimulationPanel.height); // controllo se tocca un bordo dello schermo
			
			//scia
			
			
			try {
				trailSemaphore.acquire();
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
			} catch (InterruptedException e) {
				System.out.println(e);
				e.printStackTrace();
			}finally {
				trailSemaphore.release();
		    }
			
			
			
			//aplico forze
			
			this.acceleration.add(AVector.div(force, this.getMass()));//aggiungo la forza divisa per la messa, all'accelerazione
			this.velocity.add(this.acceleration);//sommo velocità e accelerazione
			this.getPosition().add(this.velocity);//sommo posizione a velocità
			this.acceleration.mult(0);//resetto accelerazione ogni frame
		}
	}

	public void edge(int scrWidth,int scrHeight) {
		if(isMovable()) {
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
	
	@Override
	public void draw(GraphicsContext g) {
		
		//colore in base alle impostazioni date
		if(this.getIsPositive()>0)
			g.setFill(Color.RED);
		else if(this.getIsPositive()<0)
			g.setFill(Color.BLUE);
		else
			g.setFill(Color.GRAY);
			
			
			//riempo ovale con posizioni della particella (le casto in int perchè sono double) e con altezza e larghezza uguali a raggio^2
			g.fillOval(this.getPosition().getX(), this.getPosition().getY(), this.getR()*2, this.getR()*2);
	}
	
	@Override
	public void drawTrail(GraphicsContext g) {
		if(isMovable()) {
			
			try {
				trailSemaphore.acquire();
				
				//colore in base alle impostazioni date
				if(this.getIsPositive()>0)
					g.setFill(Color.RED);
				else if(this.getIsPositive()<0)
					g.setFill(Color.BLUE);
				else
					g.setFill(Color.GRAY);

				//disegno tutte le posizioni salvate in trail creando un effetto scia
				//nella grandezza dell'ovale, i/(this.getR()*2) per rendere l'ovale sempre più piccolo, 
				//ma non più grande della dimensione della particella
				double size=(this.getR()*2);
				int i=0;
				Iterator<AVector> it = trail.iterator();
				while(it.hasNext()) {//thread safe loop
					AVector cur = it.next();
					if(cur==null) {
						break;
					}
					i++;
					size=i/(this.getR()*2);
					if(size>(this.getR()*2)) {
						size=(this.getR()*2);
					}
					g.fillOval(cur.getX(), cur.getY(), size, size);
				}
			} catch (InterruptedException e) {
				System.out.println(e);
				e.printStackTrace();
			}finally {
				trailSemaphore.release();
		    }
		}	
	}
	
	
	
	
	

	@Override
	public Particle clone() {
	    Particle cloned = new Particle();
	    cloned.setPosition(this.getPosition().clone());
	    cloned.setMass(this.getMass());
	    cloned.setR(this.getR());
	    cloned.setIsPositive(this.getIsPositive());
	    cloned.setAcceleration(this.getAcceleration().clone());
	    cloned.setVelocity(this.getVelocity().clone());
	    cloned.setTrail(new ArrayList<AVector>());
	    return cloned;
	}
	
	


	


	
}
