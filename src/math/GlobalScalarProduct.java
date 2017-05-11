package math;

import math.points.Vector;

public class GlobalScalarProduct implements ScalarProduct {
	
	public static ScalarProduct scalProd = new EuclidScalarProduct();
	
	public static void setMetric(ScalarProduct sp){
		scalProd = sp;
	}
	
	@Override
	public double product(Vector v, Vector w) {
		return scalProd.product(v, w);
	}
	@Override
	public double product(Vector v) {
		return scalProd.product(v);
	}
	
}
