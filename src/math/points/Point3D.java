package math.points;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Point3D extends PointBasic implements Vector{
	
	public double x, y, z;
	
	public Point3D(double x, double y, double z){
		super(3);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D(Point3D p){
		super(3);
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}
	
	public Point3D clone(){
		return new Point3D(this);
	}
	
	public double get(int index){
		switch (index) {
			case 0: return x;
			case 1: return y;
			case 2: return z;
			default: return 0;
		}
	}
	
	public Point3D set(int index, double value) {
		switch (index) {
			case 0: this.x = value; return this;
			case 1: this.y = value; return this;
			case 2: this.z = value; return this;
			default: return this;
		}
	}
	
	public Point3D add(Vector p){
		this.x += p.get(0);
		this.y += p.get(1);
		this.z += p.get(2);
		return this;
	}
	
	public Point3D sub(Vector p){
		this.x -= p.get(0);
		this.y -= p.get(1);
		this.z -= p.get(2);
		return this;
	}
	
	public Point3D mult(double m){
		this.x *= m;
		this.y *= m;
		this.z *= m;
		return this;
	}
	
	public Point3D minus(Point3D v){
		return this.clone().sub(v);
	}
	
	public Point3D plus(Point3D v){
		return this.clone().add(v);
	}
	
	public Point3D getNull(){
		return new Point3D(0,0,0);
	}
	
	public Point3D getRandom(Random r){
		return new Point3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
	}
	
	public String toString(){
		return "(" + x + ", " + y + ", " + z + ")" ;
	}
	
	public static List<Vector> asVector(List<Point3D> pts){
		List<Vector> list = new ArrayList<>();
		list.addAll(pts);
		return list;
	}
	
	public Point3D crossProduct(Point3D p){
		
		double newX = y * p.z - z * p.y;
		double newY = z * p.x - x * p.z;
		double newZ = x * p.y - y * p.x;
				
		return new Point3D(newX, newY, newZ);
		
	}

}
