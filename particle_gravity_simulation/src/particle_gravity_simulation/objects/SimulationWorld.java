package particle_gravity_simulation.objects;

import java.awt.Graphics;
import java.util.ArrayList;

import a.geometry.AVector;
import particle_gravity_simulation.graphics.Sliders;

public class SimulationWorld {
	private ArrayList<WorldObject> WorldObjects;
	
	//nel costruttore inizializzo l'array list
	public SimulationWorld(){
		WorldObjects=new ArrayList<WorldObject>();
	}
	
	public void update() {
			for(int i=0;i<WorldObjects.size();i++) {
				for(int j=0; j<i;j++) {
					AVector f=Force.GravityV(WorldObjects.get(i), WorldObjects.get(j));
					WorldObjects.get(i).update(AVector.neg(f));
					WorldObjects.get(j).update(f);
					
				}
			}
	}
	
	//disegna tutti i world objects sulla buffered image.
	public void draw(Graphics g) {
		for(int i=0;i<WorldObjects.size();i++) {
			WorldObjects.get(i).draw(g);

		}
	}
	
	//aggiungi particella sulle coordinate specificate
	public void addP(int x, int y) {
		//controllo se la particella è movable o stationary (settato in input dallo slider)
		//aggiungo la particella in base alle impostazioni settate dagli slider
		if(Sliders.isMovable)
			WorldObjects.add(new MovableParticle(x,y,Sliders.Masssl,5,Sliders.isPositive));
		else
			WorldObjects.add(new StationaryParticle(x,y,Sliders.Masssl,5,Sliders.isPositive));
	}
		
}
