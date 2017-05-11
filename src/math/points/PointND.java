package math.points;

import java.util.Arrays;
import java.util.Random;

public class PointND extends PointBasic implements Vector {
	
	public double[] coords;
	
	public PointND(double[] coords){
		super(coords.length);
		this.coords = Arrays.copyOf(coords, this.dimension);
	}
	
	public PointND(PointND p){
		super(p.dimension);
		this.coords = Arrays.copyOf(p.coords, this.dimension);
	}
	
	public PointND clone(){
		return new PointND(this);
	}
	
	public double get(int index){
		if (index >= this.dimension) return 0;
		return this.coords[index];
	}
	
	public PointND set(int i, double value){
		this.coords[i] = value;
		return this;
	}
	
	public PointND add(Vector v){
		for (int i = 0; i < Math.min(this.dimension, v.getDimension()); i++){
			this.coords[i] += v.get(i);
		}
		return this;
	}
	
	public PointND sub(Vector v){
		for (int i = 0; i < Math.min(this.dimension, v.getDimension()); i++){
			this.coords[i] -= v.get(i);
		}
		return this;
	}
	
	public PointND mult(double m){
		for (int i = 0; i < this.dimension; i++){
			this.coords[i] *= m;
		}
		return this;
	}
	
	public PointND getNull(){
		return new PointND(new double[this.dimension]);
	}
	
	public PointND getRandom(Random r){
		PointND p = this.getNull();
		for (int i = 0; i < this.dimension; i++){
			p.coords[i] = r.nextDouble();
		}
		return p;
	}
	
	public String toString(){
		String s = "(" + coords[0];
		for (int i = 1; i < this.dimension; i++){
			s += ", " + this.coords[i];
		}
		return s + ")" ;
	}
	
//	public PointND div(double m){
//		return this.mult(1 / m);
//	}
//	
//	public PointND invert(){
//		return this.mult(-1);
//	}
//	
//	public double length(){
//		return EuclidSpace.length(this);
//	}
//	
//	public PointND norm(){
//		this.div(this.length());
//		return this;
//	}
//	
//	public PointND setLength(double l){
//		this.mult(l / this.length());
//		return this;
//	}
//	
//	public PointND minus(PointND p){
////		return new Point(this).sub(p);
//		PointND q = this.clone();
//		return q.sub(p);
//	}
//	
//	public PointND plus(PointND p){
//		return new PointND(this).add(p);
//	}
//	
//	// scalarproduct stuff
//	public double EclidScalarProduct(){
//		return EuclidSpace.scalarProduct(this);
//	}
//	
//	public double EclidScalarProduct(PointND p){
//		return EuclidSpace.scalarProduct(this, p);
//	}
//	
//	public double distanceTo(PointND p){
//		return EuclidSpace.distance(this, p);
//	}
//	
//	public boolean inRange(PointND p, double r){
//		return EuclidSpace.inRange(this, p, r);
//	}

}
