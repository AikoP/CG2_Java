package math.points;

import java.util.Random;

public interface Vector {
	
	public abstract Vector clone();
	
	public abstract int getDimension();
	
	public abstract double get(int index);
	public abstract Vector set(int i, double value);
	
	public abstract Vector add(Vector v);
	public abstract Vector sub(Vector v);
	public abstract Vector mult(double m);
	public abstract Vector div(double d);
	
	public abstract Vector invert();
	
	public abstract double length();
	public abstract Vector norm();
	public abstract Vector setLength(double l);
	
	public abstract Vector minus(Vector v);
	public abstract Vector plus(Vector v);
	
	public abstract Vector getNull();
	public abstract Vector getRandom(Random r);
	
	public abstract String toString();
	
	// scalarproduct stuff
	public abstract double EuclidScalarProduct();
	public abstract double EuclidScalarProduct(Vector p);
	public abstract double distanceTo(Vector p);
	public abstract boolean inRange(Vector p, double r);
	
}
