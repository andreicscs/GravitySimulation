package particle_gravity_simulation.objects;

import a.geometry.AVector;
import particle_gravity_simulation.graphics.SimulationControls;

public class Force {
    
    //direction
    public static AVector GravityV(WorldObject a, WorldObject b) {
    	if (a.getIsPositive() == 0 && b.getIsPositive() == 0) {
            return new AVector();
        }
    	
    	double f = GravityM(a, b);
        double alfa = a.getPosition().points(b.getPosition());
        AVector gravity = new AVector();
        gravity.set(alfa, f);
        
        if (a.getIsPositive() == b.getIsPositive()) {
            return AVector.neg(gravity);
        }
        
        return gravity;
    }
    
    //magnitude
    private static double GravityM(WorldObject a, WorldObject b) {
    	double distanceSquared=a.getPosition().distanceSQ(b.getPosition());
    	
    	
        if (distanceSquared < 625) {
            distanceSquared = 625;
        }
        
    	
        double m1 = a.getMass();
        double m2 = b.getMass();
        double force = (SimulationControls.gSlider * m1 * m2) / distanceSquared;
        
        return force;
    }
}
