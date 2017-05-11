package math.points;

import java.util.Random;

import math.EuclidSpace;

public abstract class PointBasic implements Vector {
	
	public int dimension;
	
	public abstract Vector clone();
	
	public PointBasic(int dimension){
		this.dimension = dimension;
	}
	
	public int getDimension(){
		return this.dimension;
	}
	
	public abstract double get(int index);
	public abstract Vector set(int i, double value);
	
	public abstract Vector add(Vector v);
	public abstract Vector sub(Vector v);
	public abstract Vector mult(double m);
	
	public Vector div(double d){
		this.mult(1 / d);
		return this;
	}
	
	public Vector invert(){
		this.mult(-1);
		return this;
	}
	
	public double length(){
		return EuclidSpace.length(this);
	}
	
	public Vector norm(){
		this.div(this.length());
		return this;
	}
	
	public Vector setLength(double l){
		this.mult(l / this.length());
		return this;
	}
	
	public Vector minus(Vector v){
		return this.clone().sub(v);
	}
	
	public Vector plus(Vector v){
		return this.clone().add(v);
	}
	
	public abstract Vector getNull();
	public abstract Vector getRandom(Random r);
	
	public abstract String toString();
	
	// scalarproduct stuff
	public double EuclidScalarProduct(){
		return EuclidSpace.scalarProduct(this);
	}
	
	public double EuclidScalarProduct(Vector v){
		return EuclidSpace.scalarProduct(this, v);
	}
	
	public double distanceTo(Vector v){
		return EuclidSpace.distance(this, v);
	}
	
	public boolean inRange(Vector v, double r){
		return EuclidSpace.inRange(this, v, r);
	}
	
	public static Vector getNull(int dimension){
		switch (dimension) {
			case 2: return new Point2D(0,0);
			case 3: return new Point3D(0,0,0);
			default: return new PointND(new double[dimension]);
		}
	}
	
	public static Vector getRandom(int dimension, Random r){
		switch (dimension) {
			case 2: return new Point2D(r.nextDouble(), r.nextDouble());
			case 3: return new Point3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
			default: break;
		}
		
		double[] randoms = new double[dimension];
		for (int i = 0; i < dimension; i++){
			randoms[i] = r.nextDouble();
		}
		
		return new PointND(randoms);
	}

}
