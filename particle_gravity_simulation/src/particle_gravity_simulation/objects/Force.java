package particle_gravity_simulation.objects;

import a.geometry.AVector;
import particle_gravity_simulation.graphics.Sliders;

public class Force {
	
	//calcolo direzione della forza
	public static AVector GravityV(WorldObject a,WorldObject b) {
		double f=GravityM(a,b);//prendo la magnitudine della forza tra a e b
		double alfa;//angolo(direzione vettore)
		AVector gravity = new AVector();
		
		alfa=a.getPosition().points(b.getPosition());//angolo fra a e b
		gravity.set(alfa, f); //imposto direzione e magnitudine al vettore
		
		//controllo se il world object è negativo positivo o neutro
		//come con i magneti
		//neutri non attraggono, sono attratti da tutto, positivi respingono positivi,
		//negativi respingono negativi, negativi e positivi si attraggono
		if((a.getIsPositive()==0 && b.getIsPositive()==0))return new AVector();
		if(a.getIsPositive()==b.getIsPositive())  return AVector.neg(gravity);
			
		return gravity;
	}
	
	//calcolo magnitudine della forza
	private static double GravityM(WorldObject a,WorldObject b) { 
		double f;
		//Gravitational constant
		final double G=Sliders.Gslider;
		
		double distance=a.getPosition().distance(b.getPosition());//distanza fra i due punti
		int m1 =a.getMass();
		int m2 =b.getMass();
		
		//non applica una forza con distanza più bassa di 25
		//più la distanza si avvicina a 0 più la forza si avvicina a infinito, romperebbe il programma
		//si potrebbe risolvere con le collisioni (ad esempio impulse collision)
		if (distance<25)distance=25;
		double r2=distance*distance;//distanza al quadrato
		f=((m1*m2)/r2)*G;//formula gravitazionale
		return f;
	}
}