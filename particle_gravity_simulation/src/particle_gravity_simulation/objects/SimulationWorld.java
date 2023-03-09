package particle_gravity_simulation.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

import a.geometry.AVector;

import particle_gravity_simulation.graphics.SimulationControls;
import particle_gravity_simulation.graphics.SimulationPanel;


public class SimulationWorld {
	private List<WorldObject> WorldObjects;
	
	public SimulationWorld(){
		WorldObjects = Collections.synchronizedList(new ArrayList<WorldObject>());//thread safe array
	}
	
	public void update() {
    	for (int i = 0; i < WorldObjects.size(); i++) {
    		for (int j = i; j < WorldObjects.size(); j++) {//sarebbe meglio partire da i+1, non lo faccio perchè altrimenti non verrebbe calcolata la forza iniziale della prima particella, quindi faccio un if per compensare
    			if(WorldObjects.get(i)!=WorldObjects.get(j)) {
	    			
    				//per ogni coppia di worldobjects calcolo la gravità e la applico
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
						    WorldObjects.get(j).setPosition(AVector.sub(WorldObjects.get(j).getPosition(), correction));
						}
			        }
			        
    			}else {
    				WorldObjects.get(i).update(new AVector());
    			}
			}
		}
	}
	
	

	
	public void clear() {
		//da rendere threadsafe
		WorldObjects.clear();
	}
	

	//disegna tutti i world objects sulla buffered image.
	public void draw() {
		Iterator<WorldObject> it = WorldObjects.iterator();
		//thread safe loop
		while(it.hasNext()) {
			WorldObject cur = it.next();
			SimulationPanel.draw(cur);
		}
	}
	
	//aggiungi particella sulle coordinate specificate
	public void addP(double x, double y, boolean isMovable) {
		//controllo se la particella è movable o stationary (settabile in input dal controller), passato come parametro
		//aggiungo la particella in base alle impostazioni settate dagli slider
		WorldObjects.add(new Particle(x,y,SimulationControls.Masssl,5,SimulationControls.isPositiveSelected,isMovable));
	}
	
	public void removeLastP() {
		WorldObjects.remove(WorldObjects.size() - 1);
	}
	
	
	//calcolo e applicazione della spinta iniziale della particella
	public void calcInitForce(double x1, double y1,double x2, double y2) {
		int forceDamping=5;
		
		//calcolo l'angolo della forza
		double alfa=Math.atan2(y1-y2,x1-x2);
		//calcolo magnitudine della forza
		double mag=Math.sqrt(Math.abs( Math.abs(x1-x2)*Math.abs(x1-x2) + Math.abs(y1-y2)*Math.abs(y1-y2) ));
		//ridotta per forceDamping (sarebbe troppo forte altrimenti)
		mag/=forceDamping;
		
		AVector initForce=new AVector();
		initForce.set(alfa, mag);
		//e lo applico all'ultima particella, l'ultima appena creata
		WorldObjects.get(WorldObjects.size() - 1).update(initForce);;
	}
	
	
	
	
	
	
	
	
	

	public void trajectoryPreview(double  x1, double  y1, double  x2, double y2,GraphicsContext g2d) {
		SimulationWorld worldCopy = new SimulationWorld();
		
		//copio ogni particella del mondo originale
		for(int i=0;i<this.WorldObjects.size();i++) {
			worldCopy.WorldObjects.add(this.WorldObjects.get(i).clone());
		}

		//aggiungo una particella movable (per simulare la traiettoria)
		worldCopy.addP(x1, y1, true);
		
		//calcolo la distanza fra i due punti
	    double risy= Math.abs(y1-y2);
		double risx= Math.abs(x1-x2);
		double dist= Math.sqrt((Math.pow(risx, 2)+Math.pow(risy, 2)));
		
		//se la distanza è maggiore di 5, (se la distanza è abbastanza rilevante) allora dò la spinta iniziale
	    if(dist>5) {
	    	worldCopy.calcInitForce(x1, y1, x2, y2);
	    }

		
		
		
		int last=worldCopy.WorldObjects.size()-1;
		
		//100 iterazioni nel "futuro"
		for(int k=50;k>0;k--) {
			//calcolo tutte le forze del mondo, ma le applico solamente all'ultima particella (quella da simulare) del mondo copiato.
    		for (int j = 0; j < worldCopy.WorldObjects.size(); j++) {
    			if(worldCopy.WorldObjects.get(last)!=worldCopy.WorldObjects.get(j)) {
    				AVector f=Force.GravityV(worldCopy.WorldObjects.get(last), WorldObjects.get(j));
        			worldCopy.WorldObjects.get(last).update(AVector.neg(f));
    				
        			// controllo collisioni
    				if(SimulationControls.isCollisionOn) {
    					double distance = worldCopy.WorldObjects.get(last).getPosition().distance(WorldObjects.get(j).getPosition());
    					if (distance <= worldCopy.WorldObjects.get(last).getR() + WorldObjects.get(j).getR()) {
    					    // le particelle si sono scontrate, calcola le nuove velocità in base alle loro masse
    						
    					    AVector vi = worldCopy.WorldObjects.get(last).getVelocity();
    					    AVector vj = WorldObjects.get(j).getVelocity();
    					    double m1 = worldCopy.WorldObjects.get(last).getMass();
    					    double m2 = WorldObjects.get(j).getMass();
    					    
    					    //equazioni per la conservazione dell'energia e della quantità di moto.
    					    AVector normal = AVector.normalize(AVector.sub(worldCopy.WorldObjects.get(last).getPosition(), WorldObjects.get(j).getPosition()));
    					    double vi_new_mag = AVector.dot(vi, normal);
    					    double vj_new_mag = AVector.dot(vj, normal);
    					    double v1_new_mag = ((m1 - m2) * vi_new_mag + 2 * m2 * vj_new_mag) / (m1 + m2);
    					    AVector vi_new = AVector.add(AVector.mult(normal, v1_new_mag - vi_new_mag), vi); 
    					    
    					    //applico le velocità calcolate
    					    worldCopy.WorldObjects.get(last).setVelocity(vi_new);
    					    
    					    //Correzione di posizione per evitare l'attraversamento delle particelle
    					    double overlap = worldCopy.WorldObjects.get(last).getR() + WorldObjects.get(j).getR() - distance;
    					    AVector correction = AVector.mult(normal, overlap / 2.0);
    					    worldCopy.WorldObjects.get(last).setPosition(AVector.add(worldCopy.WorldObjects.get(last).getPosition(), correction));
    					}
    		        }
    			}else {
    				worldCopy.WorldObjects.get(last).update(new AVector());
    			}
			}
    		//size per la grandezza dei cerchi del trajectory prevuew disegnati sul canvas
    		int size=k/worldCopy.WorldObjects.get(last).getR()*2;
			if(size>(worldCopy.WorldObjects.get(last).getR())) {
				size=(worldCopy.WorldObjects.get(last).getR());
			}
			
			//disegno un cerchio sul canvas, con posizione della particella simulata, e grandezza size
			SimulationPanel.drawCircle( worldCopy.WorldObjects.get(last).getPosition().getX(), worldCopy.WorldObjects.get(last).getPosition().getY(), size);
		}
	}
}
