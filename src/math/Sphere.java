package math;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import math.EuclidSpace;
import math.GlobalScalarProduct;
import math.Intervall;
import math.ScalarProduct;
import math.points.PointBasic;
import math.points.Vector;

public class Sphere {

	public double radius;
	public Vector center;
	
	public Intervall orbit;
	
	public Sphere(Vector c, double r) {
		this.center = c;
		this.radius = r;
		this.orbit = new Intervall(0, 0);
		setOrbit(c.getNull());
	}
	
	public void setOrbit(Vector ref) {

		double dist = ref.distanceTo(this.center);
		
		orbit.set(Math.min(0, dist - this.radius), dist + this.radius);
		
	}

	public boolean inRange(Vector p) {
		
		return p.inRange(center, radius);

	}

	public boolean overLaps(Sphere s) {
		
		return center.inRange(s.center, (radius + s.radius));

	}

	public boolean contains(Sphere s) {
		
		return center.inRange(s.center, (radius - s.radius));

	}
	
	// volume and surface
	public double getVolume(){
		int n = this.center.getDimension();
		return Math.pow(this.radius, n) * recurrenceSphereVolume(n);
	}
	public double recurrenceSphereVolume(int dim){
		if (dim == 0)
			return 1;
		return recurrenceSphereSurface(dim - 1) / dim;
	}
	
	public double getSurface(){
		int n = this.center.getDimension();
		return Math.pow(this.radius, n - 1) * recurrenceSphereSurface(n);
	}
	public double recurrenceSphereSurface(int dim){
		if (dim == 0)
			return 2;
		return 2 * Math.PI * recurrenceSphereVolume(dim - 1);		
	}

	public void fitSphere(List<Vector> pts) {

		double d, maxShrink, deltaR, rSquared, sSquared;
		Vector w, s;

		for (Vector v : pts) {
			
//			fitNewPoint(p);

			if (!this.inRange(v)) {

				// Ritters Algorithm
				d = v.distanceTo(this.center);
				
				this.radius = (d + radius) / 2.0;
				
				w = center.minus(v).setLength(this.radius);

				this.center = v.plus(w);

				// shrink sphere if possible (not error-proof yet)
				
//				maxShrink = this.radius;
//				rSquared = radius * radius;
//				
//				for (Vector x : pts) {
//					
//					if (x == v)
//						continue;
//
//					s = x.minus(this.center);
//					sSquared = EuclidSpace.scalarProduct(s);
//
//					if (sSquared > rSquared)
//						continue;
//
//					deltaR = (radius * 0.5) * (sSquared - rSquared) / (EuclidSpace.scalarProduct(s, w) - rSquared);
//
//					if (deltaR < maxShrink)
//						maxShrink = deltaR;
//
//				}
//				
////				maxShrink *= radius * 0.5;
//
//				if (maxShrink > 0) {
//					this.center.sub(w.setLength(maxShrink));
//					this.radius -= maxShrink;
//				}
			}
		}

	}
	
	
	public void fitNewPoint(Vector p) {

		double sp = EuclidSpace.scalarProduct(this.center.minus(p));

		// Ritters Algorithm
		if (sp > this.radius * this.radius) {

			this.radius = (Math.sqrt(sp) + radius) * 0.5;
			this.center = center.minus(p).setLength(this.radius).add(p);

		}

	}
	
	public static Sphere getBoundingSphere(List<Vector> pts){
		
		if (pts.isEmpty()) return null;
		
		Vector center = pts.get(0);
		
		Vector start = VectorMath.getFurthest(center, pts);
		
		Vector end = VectorMath.getFurthest(start, pts);
		
		Sphere boundingSphere = new Sphere(VectorMath.getCenter(start, end), EuclidSpace.distance(start, end) / 2.0);
		boundingSphere.fitSphere(pts);
		
		return boundingSphere;
		
	}
	
	public void setBoundingSphere(List<Vector> pts){
		Sphere boundingSphere = getBoundingSphere(pts);
		this.center = boundingSphere.center;
		this.radius = boundingSphere.radius;
	}
	
	public static Sphere getRandom(int dim, Random r){
		return new Sphere(PointBasic.getRandom(dim, r), r.nextDouble());
	}
	
	
	

}
