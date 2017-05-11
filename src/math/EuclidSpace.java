package math;

import math.points.Vector;

public class EuclidSpace {
	
	private static ScalarProduct scalarProduct = new EuclidScalarProduct();
	
	public static ScalarProduct getScalarProduct(){
		return scalarProduct;
	}
	
	public static void setScalarProduct(ScalarProduct sp){
		scalarProduct = sp;
	}
	
	// scalar Products
	public static double scalarProduct(Vector v, Vector w){
		return scalarProduct.product(v, w);
	}
	public static double scalarProduct(Vector v){
		return scalarProduct.product(v);
	}
	
	// length (norm)
	public static double length(Vector v){
		return Math.sqrt(scalarProduct(v));
	}

	// distance
	public static double distance(Vector v, Vector w){
		return length(v.minus(w));
	}
	
	// ranges
	public static boolean inRange(Vector v, Vector w, double range) {
		return scalarProduct(v.minus(w)) <= range * range;
	}
	
	// angles
	public static double angleBetween(Vector v, Vector w){
		return Math.acos(scalarProduct(v, w) / (length(v) * length(w)));
	}

		
	
}
