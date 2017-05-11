package math;

import math.points.Point2D;
import math.points.Point3D;
import math.points.Vector;

public class EuclidScalarProduct implements ScalarProduct {

	// 2D for performance
	public double product(Point2D p1, Point2D p2) {
		return p1.x * p2.x + p1.y * p2.y;
	}
	public double product(Point2D p) {
		return p.x * p.x + p.y * p.y;
	}

	// 3D for performance
	public double product(Point3D p1, Point3D p2) {
		return p1.x * p2.x + p1.y * p2.y + p1.z * p2.z;
	}
	
	public double product(Point3D p) {
		return p.x * p.x + p.y * p.y + p.z * p.z;
	}
	
	@Override
	public double product(Vector v, Vector w) {
		
		if (v instanceof Point2D && w instanceof Point2D){
			return product((Point2D) v, (Point2D) w);
		}
		if (v instanceof Point3D && w instanceof Point3D){
			return product((Point3D) v, (Point3D) w);
		}
		
		double sp = 0;
		for (int i = 0; i < v.getDimension(); i++){
			sp += v.get(i) * w.get(i);
		}
		return sp;
		
	}
	
	@Override
	public double product(Vector v) {
		
		if (v instanceof Point2D){
			return product((Point2D) v);
		}
		if (v instanceof Point3D){
			return product((Point3D) v);
		}
		
		double sp = 0;
		for (int i = 0; i < v.getDimension(); i++){
			sp += v.get(i) * v.get(i);
		}
		return sp;
		
	}

	
}
