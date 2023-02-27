package particle_gravity_simulation.objects;

import a.geometry.AVector;

public class Force {
    
    private static final double G = 10;
    
    public static AVector GravityV(WorldObject a, WorldObject b) {
        double f = GravityM(a, b);
        double alfa = a.getPosition().points(b.getPosition());
        AVector gravity = new AVector();
        gravity.set(alfa, f);
        
        if (a.getIsPositive() == 0 && b.getIsPositive() == 0) {
            return new AVector();
        }else if (a.getIsPositive() == b.getIsPositive()) {
            return AVector.neg(gravity);
        }
        
        return gravity;
    }
    
    private static double GravityM(WorldObject a, WorldObject b) {
    	double distanceSquared=a.getPosition().distanceSQ(b.getPosition());
    	
        if (distanceSquared < 625) {
            distanceSquared = 625;
        }
        
        double m1 = a.getMass();
        double m2 = b.getMass();
        double force = (G * m1 * m2) / distanceSquared;
        
        return force;
    }
}
