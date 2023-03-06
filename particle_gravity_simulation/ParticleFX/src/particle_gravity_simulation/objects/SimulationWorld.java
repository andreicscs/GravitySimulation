package particle_gravity_simulation.objects;

import java.util.ArrayList;
import a.geometry.AVector;
import javafx.scene.canvas.GraphicsContext;
import particle_gravity_simulation.graphics.SimulationControls;

public class SimulationWorld {
	static private ArrayList<WorldObject> WorldObjects;
	
	//nel costruttore inizializzo l'array list
	public SimulationWorld(){
		WorldObjects=new ArrayList<WorldObject>();
		
	}
	
	public void update() {
    	for (int i = 0; i < WorldObjects.size(); i++) {
    		for (int j = i; j < WorldObjects.size(); j++) {
				AVector f=Force.GravityV(WorldObjects.get(i), WorldObjects.get(j));
				WorldObjects.get(i).update(AVector.neg(f));
				WorldObjects.get(j).update(f);
				
				
				//controllo collisioni
				if(SimulationControls.isCollisionOn) {
					double distance = WorldObjects.get(i).getPosition().distance(WorldObjects.get(j).getPosition());
					if (distance <= WorldObjects.get(i).getR() + WorldObjects.get(j).getR()) {
					    // le particelle si sono scontrate, calcola le nuove velocità in base alle loro masse
						
					    AVector vi = WorldObjects.get(i).getVelocity();
					    AVector vj = WorldObjects.get(j).getVelocity();
					    double m1 = WorldObjects.get(i).getMass();
					    double m2 = WorldObjects.get(j).getMass();
					    
					    //equazioni per la conservazione dell'energia e della quantità di moto.
					    AVector normal = AVector.normalize(AVector.sub(WorldObjects.get(i).getPosition(), WorldObjects.get(j).getPosition()));
					    double vi_new_mag = AVector.dot(vi, normal);
					    double vj_new_mag = AVector.dot(vj, normal);
					    double v1_new_mag = ((m1 - m2) * vi_new_mag + 2 * m2 * vj_new_mag) / (m1 + m2);
					    double v2_new_mag = ((m2 - m1) * vj_new_mag + 2 * m1 * vi_new_mag) / (m1 + m2);
					    AVector vi_new = AVector.add(AVector.mult(normal, v1_new_mag - vi_new_mag), vi);
					    AVector vj_new = AVector.add(AVector.mult(normal, v2_new_mag - vj_new_mag), vj); 
					    
					    //applico le velocità calcolate
					    WorldObjects.get(i).setVelocity(vi_new);
					    WorldObjects.get(j).setVelocity(vj_new);
					    
					    //Correzione di posizione per evitare l'attraversamento delle particelle
					    double overlap = WorldObjects.get(i).getR() + WorldObjects.get(j).getR() - distance;
					    AVector correction = AVector.mult(normal, overlap / 2.0);
					    WorldObjects.get(i).setPosition(AVector.add(WorldObjects.get(i).getPosition(), correction));
					    WorldObjects.get(j).setPosition(AVector.sub(WorldObjects.get(j).getPosition(), correction));}
		        }
				
			}
		}
	}
	
	

	
	public static void clear() {
		WorldObjects.clear();
	}
	

	//disegna tutti i world objects sulla buffered image.
	public void draw(GraphicsContext g2d) {
		for(int i=0;i<WorldObjects.size();i++) {
			WorldObjects.get(i).draw(g2d);

		}
		
	}
	
	//aggiungi particella sulle coordinate specificate
	public void addP(int x, int y, boolean isMovable) {
		//controllo se la particella è movable o stationary (settato in input dallo slider)
		//aggiungo la particella in base alle impostazioni settate dagli slider
		if(isMovable) {
			WorldObjects.add(new MovableParticle(x,y,SimulationControls.Masssl,5,SimulationControls.isPositiveSelected));
		}
		else {
			WorldObjects.add(new StationaryParticle(x,y,SimulationControls.Masssl,5,SimulationControls.isPositiveSelected));
		}
	}
	
	public void removeLastP() {
		WorldObjects.remove(WorldObjects.size() - 1);
	}
	
	
	public void calcInitForce(int x1, int y1,int x2, int y2) {
		int forceDamping=5;
		
		double alfa=Math.atan2(y1-y2,x1-x2);
		double mag=Math.sqrt(Math.abs( Math.abs(x1-x2)*Math.abs(x1-x2) + Math.abs(y1-y2)*Math.abs(y1-y2) ));
		mag/=forceDamping;
		AVector initForce=new AVector();
		initForce.set(alfa, mag);
		WorldObjects.get(WorldObjects.size() - 1).update(initForce);;
	}
		
}
