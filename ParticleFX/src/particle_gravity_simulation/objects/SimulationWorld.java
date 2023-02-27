package particle_gravity_simulation.objects;

import java.util.ArrayList;
import a.geometry.AVector;
import javafx.scene.canvas.GraphicsContext;
import particle_gravity_simulation.graphics.Sliders;

public class SimulationWorld {
	private ArrayList<WorldObject> WorldObjects;
	//nel costruttore inizializzo l'array list
	public SimulationWorld(){
		WorldObjects=new ArrayList<WorldObject>();
		for(int i=0;i<2;i++) {
			WorldObjects.add(new MovableParticle(110,101,100,5,1));//	test
			WorldObjects.add(new MovableParticle(100,110,100,5,-1));//	test
		}
		
	}
	
	public void update() {
    	for (int i = 0; i < WorldObjects.size(); i++) {
    		for (int j = i + 1; j < WorldObjects.size(); j++) {
				AVector f=Force.GravityV(WorldObjects.get(i), WorldObjects.get(j));
				WorldObjects.get(i).update(AVector.neg(f));
				WorldObjects.get(j).update(f);
			}
		}
	}
	
	//disegna tutti i world objects sulla buffered image.
	public void draw(GraphicsContext g2d) {
		for(int i=0;i<WorldObjects.size();i++) {
			WorldObjects.get(i).draw(g2d);

		}
		
	}
	
	//aggiungi particella sulle coordinate specificate
	public void addP(int x, int y) {
		//controllo se la particella è movable o stationary (settato in input dallo slider)
		//aggiungo la particella in base alle impostazioni settate dagli slider
		if(Sliders.isMovable) {
			WorldObjects.add(new MovableParticle(x,y,Sliders.Masssl,5,Sliders.isPositive));
		}
		else {
			WorldObjects.add(new MovableParticle(x,y,100,5,-1));//	test
			//WorldObjects.add(new StationaryParticle(x,y,Sliders.Masssl,5,Sliders.isPositive));
		}
	}
		
}
