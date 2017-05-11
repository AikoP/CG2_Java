package math.points;

import java.util.Random;

public class Point2D extends PointBasic implements Vector{
	
	public double x, y;
	
	public Point2D(double x, double y){
		super(2);
		this.x = x;
		this.y = y;
	}
	
	public Point2D(Point2D p){
		super(2);
		this.x = p.x;
		this.y = p.y;
	}
	
	public Point2D clone(){
		return new Point2D(this);
	}
	
	public double get(int index){
		switch (index) {
			case 0: return x;
			case 1: return y;
			default: return 0;
		}
	}
	
	public Point2D set(int index, double value) {
		switch (index) {
			case 0: this.x = value; return this;
			case 1: this.y = value; return this;
			default: return this;
		}
	}
	
	public Point2D add(Vector v) {
		this.x += v.get(0);
		this.y += v.get(1);
		return this;
	}
	
	public Point2D sub(Vector v) {
		this.x -= v.get(0);
		this.y -= v.get(1);
		return this;
	}
	
	public Point2D mult(double m){
		this.x *= m;
		this.y *= m;
		return this;
	}
	
	public Point2D getNull(){
		return new Point2D(0,0);
	}
	
	public Point2D getRandom(Random r){
		return new Point2D(r.nextDouble(), r.nextDouble());
	}
	
	public String toString(){
		return "(" + x + ", " + y + ")" ;
	}
	
//	public Point2D add(Point2D p){
//		this.x += p.x;
//		this.y += p.y;
//		return this;
//	}
//	
//	public Point2D sub(Point2D p){
//		this.x -= p.x;
//		this.y -= p.y;
//		return this;
//	}
//	
//	public Point2D div(double m){
//		this.x /= m;
//		this.y /= m;
//		return this;
//	}
//	
//	public Point2D invert(){
//		this.x = -x;
//		this.y = -y;
//		return this;
//	}
//	
//	public double length(){
//		return EuclidSpace.length(this);
//	}
//	
//	public Point2D norm(){
//		double len = length();
//		this.x /= len;
//		this.y /= len;
//		return this;
//	}
//	
//	public Point2D setLength(double l){
//		double len = length();
//		this.x *= l / len;
//		this.y *= l / len;
//		return this;
//	}
//	
//	public Point2D minus(Point2D p){
//		return new Point2D(this.x - p.x, this.y - p.y);
//	}
//	
//	public Point2D plus(Point2D p){
//		return new Point2D(this.x + p.x, this.y + p.y);
//	}
//	
//	 //scalarproduct stuff
//	public double EuclidScalarProduct(){
//		return EuclidSpace.scalarProduct(this);
//	}
//	
//	public double EuclidScalarProduct(Point2D p){
//		return EuclidSpace.scalarProduct(this, p);
//	}
//	
//	public double distanceTo(Point2D p){
//		return EuclidSpace.distance(this, p);
//	}
//	
//	public boolean inRange(Point2D p, double r){
//		return EuclidSpace.inRange(this, p, r);
//	}
//
//	@Override
//	public Point2D minus(Vector v) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Point2D plus(Vector v) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public double EuclidScalarProduct(Vector v) {
//		return EuclidSpace.scalarProduct(this, v);
//	}
//
//	@Override
//	public double distanceTo(Vector v) {
//		return EuclidSpace.distance(this, v);		
//	}
//
//	@Override
//	public boolean inRange(Vector v, double r) {
//		return EuclidSpace.inRange(this, v, r);
//	}



}
