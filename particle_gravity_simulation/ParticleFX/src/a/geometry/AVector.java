package a.geometry;

public class AVector {
	private double x;
	private double y;
	
	public AVector(double x,double y){
		this.x=x;
		this.y=y;
	}
	public AVector(){
		this.x=0.0;
		this.y=0.0;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	
	//operazioni tra vettori
	
	public void add(AVector b) {
		this.setX(this.getX()+b.getX());
		this.setY(this.getY()+b.getY());
	}
	public void add(double b) {
		this.setX(this.getX()+b);
		this.setY(this.getY()+b);
	}
	public static AVector add(AVector a,AVector b) {
		AVector ris=new AVector();
		ris.setX(a.getX()+b.getX());
		ris.setY(a.getY()+b.getY());
		return ris;
	}
	public static AVector add(AVector a,double b) {
		AVector ris=new AVector();
		ris.setX(a.getX()+b);
		ris.setY(a.getY()+b);
		return ris;
	}
	
	public void sub(AVector b) {
		this.setX(this.getX()-b.getX());
		this.setY(this.getY()-b.getY());
	}
	public void sub(double b) {
		this.setX(this.getX()-b);
		this.setY(this.getY()-b);
	}
	public static AVector sub(AVector a,AVector b) {
		AVector ris=new AVector();
		ris.setX(a.getX()-b.getX());
		ris.setY(a.getY()-b.getY());
		return ris;
	}
	public static AVector sub(AVector a,double b) {
		AVector ris=new AVector();
		ris.setX(a.getX()-b);
		ris.setY(a.getY()-b);
		return ris;
	}
	
	public void mult(AVector b) {
		this.setX(this.getX()*b.getX());
		this.setY(this.getY()*b.getY());
	}
	public void mult(double b) {
		this.setX(this.getX()*b);
		this.setY(this.getY()*b);
	}
	public static AVector mult(AVector a,AVector b) {
		AVector ris=new AVector();
		ris.setX(a.getX()*b.getX());
		ris.setY(a.getY()*b.getY());
		return ris;
	}
	public static AVector mult(AVector a,double b) {
		AVector ris=new AVector();
		ris.setX(a.getX()*b);
		ris.setY(a.getY()*b);
		return ris;
	}
	
	public void div(AVector b) {
		this.setX(this.getX()/b.getX());
		this.setY(this.getY()/b.getY());
	}
	public void div(double b) {
		this.setX(this.getX()/b);
		this.setY(this.getY()/b);
	}
	public static AVector div(AVector a,AVector b) {
		AVector ris=new AVector();
		ris.setX(a.getX()/b.getX());
		ris.setY(a.getY()/b.getY());
		return ris;
	}
	public static AVector div(AVector a,double b) {
		AVector ris=new AVector();
		ris.setX(a.getX()/b);
		ris.setY(a.getY()/b);
		return ris;
	}
	
	
	
	//diminuisce la magnitudine di una certa percentuale passata come parametro
	public void perc(int p) {
		this.setY(this.getY()*p/100);
		this.setX(this.getX()*p/100);
		
	}
	
	//copia un'altro vettore
	public void copy(AVector ToCopy) {
		this.setX(ToCopy.getX());
		this.setY(ToCopy.getY());
	}
	
	//moltiplico per -1 per invertire il vettore
	public void neg() {
		this.mult(-1);
	}
	
	//moltiplico per -1 per invertire il vettore(versione statica, returna il vettore risultante)
	public static AVector neg(AVector a) {
		AVector copy = new AVector();
		copy.setY(a.getY());
		copy.setX(a.getX());
		copy.mult(-1);
		return copy;
	}
	
	//limita la magnitudine del vettore
	public void limit(double lim) {
		if(this.getMag()>lim) {
			this.setMag(lim);
		}
	}
	
	//calcola la direzione che avrebbe il vettore se puntasse verso il parametro passato, con la formula arcotan(ay-by/ax-by)
	public double points(AVector pos2) {
		double alfa;
		alfa=Math.atan2(this.y-pos2.getY(),this.x-pos2.getX());
		
		return alfa;
	}
	
	public double points(double x, double y) {
		double alfa;
		alfa=Math.atan2(this.getY()-y,this.getX()-x);
		return alfa;
	}
	
	//returna la direzione del vettore
	public double points() {
		double alfa;
		alfa=Math.atan2(this.getY(),this.getX());
		return alfa;
	}
	
	//imposta la magnitudine
	public void setMag(double magnitude) {
		this.setX(Math.cos(this.points())*magnitude);
		this.setY(Math.sin(this.points())*magnitude);
	}
	//imposta la magnitudine statico
	public static AVector setMag(AVector V, double magnitude) {
		AVector VCopy=new AVector();
		VCopy.copy(V);
		VCopy.setX(Math.cos(V.points())*magnitude);
		VCopy.setY(Math.sin(V.points())*magnitude);
		
		return VCopy;
	}
	//imposta la direzione
	public void setDir(double alfa) {
		this.setX(Math.cos(alfa)*this.getMag());
		this.setY(Math.sin(alfa)*this.getMag());
	}
	
	//imposta sia direzione che magnitudine
	public void set(double alfa, double magnitude) {
		this.setX(Math.cos(alfa)*magnitude);
		this.setY(Math.sin(alfa)*magnitude);
	}
	
	//restituisce la magnitudine
	public double getMag() {
		double mag;
		mag=Math.sqrt(Math.abs(this.getX()*this.getX()+this.getY()*this.getY()));
		
		return mag;
	}
	
	
	//distance al quadrato(senza sqrt per performance aggiuntive)
	public static double distanceSQ(AVector a,AVector b) {
		double distanceX = b.getX() - a.getX();
        double distanceY = b.getY() - a.getY();
        double distanceSquared = distanceX * distanceX + distanceY * distanceY;
        return distanceSquared;
	}
	
	public double distanceSQ(AVector b) {
		double distanceX = b.getX() - this.getX();
        double distanceY = b.getY() - this.getY();
        double distanceSquared = distanceX * distanceX + distanceY * distanceY;
        return distanceSquared;
	}
	//distanza tra due vettori
	public double distance(AVector b) {
		double risy= Math.abs(this.y-b.getY());
		double risx= Math.abs(this.x-b.getX());
		double dist=Math.sqrt((Math.pow(risx, 2)+Math.pow(risy, 2)));
		return dist;
	}
	public static double distance(AVector a, AVector b) {
		double risy= Math.abs(a.getY()-b.getY());
		double risx= Math.abs(a.getX()-b.getX());
		double dist=Math.sqrt((Math.pow(risx, 2)+Math.pow(risy, 2)));
		return dist;
	}
	
	
	public static double dot(AVector a, AVector b) {
		double result=0.0;
		result=(a.getX()*b.getX()) + (a.getY()*b.getY());
		return result;
	}
	public void normalize() {
	    double mag = getMag();
	    if (mag > 0) {
	        this.x /= mag;
	        this.y /= mag;
	    }
	}
	static public AVector normalize(AVector V) {
		
	    double mag = V.getMag();
	    if (mag > 0) {
	    	V.setX(V.getX() / mag);
	    	V.setY(V.getY() / mag);
	    }
		return V;
	}
	
	
}
