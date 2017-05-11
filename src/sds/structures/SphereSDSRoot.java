package sds.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import math.EuclidSpace;
import math.Sphere;
import math.points.Vector;

public class SphereSDSRoot extends SphereSDSNode {

	public SphereSDSRoot(Vector c, double r, int capacity, int maxDepth) {
		super(c, r, capacity, 0, maxDepth, c);
	}

	public List<SphereSDSNode> getTreeList() {

		List<SphereSDSNode> treeList = new LinkedList<>();

		treeList.add(this);

		for (SphereSDSNode s2d : children) {
			s2d.getTreeList(treeList);
		}

		return treeList;

	}

	public List<Vector> getAllPoints() {

		List<Vector> allPoints = new LinkedList<>();

		allPoints.addAll(points);
		for (SphereSDSNode sN : children) {
			sN.getAllPoints(allPoints);
		}

		return allPoints;
	}

	public List<Vector> getInRange(Sphere sphere) {

		List<Vector> inRange = new ArrayList<>();

		double sp = EuclidSpace.scalarProduct(this.center.minus(sphere.center));

		double radiiSum = this.radius + sphere.radius;
		if (sp > radiiSum * radiiSum) {
			return inRange;
		}

		double radiiDiff = sphere.radius - this.radius;
		if (sphere.radius >= this.radius && sp <= radiiDiff * radiiDiff) {
			this.getAllPoints(inRange);
			return inRange;
		}

		for (Vector p : points) {
			if (sphere.inRange(p)) {
				inRange.add(p);
			}
		}

		for (SphereSDSNode sN : children) {
			sN.getInRange(sphere, inRange);
		}

		return inRange;

	}

	public int countInRange(Sphere sphere) {

		double sp = EuclidSpace.scalarProduct(this.center.minus(sphere.center));

		double radiiSum = this.radius + sphere.radius;
		if (sp > radiiSum * radiiSum) {
			return 0;
		}

		double radiiDiff = sphere.radius - this.radius;
		if (sphere.radius >= this.radius && sp <= radiiDiff * radiiDiff) {
			return this.containedPoints;
		}

		int inRange = 0;

		for (Vector p : points) {
			if (sphere.inRange(p)) {
				inRange++;
			}
		}

		for (SphereSDSNode sN : children) {
			inRange += sN.countInRange(sphere);
		}

		return inRange;

	}

	public int countInOrbit(Sphere sphere, double innerRadius) {

		double sp = EuclidSpace.scalarProduct(this.center.minus(sphere.center));
		
		double radiiSum = this.radius + sphere.radius;
		double innerDiff = innerRadius - this.radius;
		if ((sp > radiiSum * radiiSum) || (innerDiff >= 0 && sp <= innerDiff * innerDiff)) {
			return 0;
		}

		double radiiDiff = sphere.radius - this.radius;
		double innerSum = this.radius + innerRadius;
		if ((radiiDiff >= 0 && sp <= radiiDiff * radiiDiff) && (sp > innerSum * innerSum)) {
			return this.containedPoints;
		}

		int inOrbit = 0;
		for (Vector v : points) {
			if (sphere.inRange(v) && !sphere.center.inRange(v, innerRadius)) {
				inOrbit++;
			}
		}

		for (SphereSDSNode sN : children) {
			inOrbit += sN.countInOrbit(sphere, innerRadius);
		}

		return inOrbit;

	}

	public List<Vector> getInOrbit(Sphere sphere, double innerRadius) {

		List<Vector> inOrbit = new LinkedList<>();

		double sp = EuclidSpace.scalarProduct(this.center.minus(sphere.center));
		
		double radiiSum = this.radius + sphere.radius;
		double innerDiff = innerRadius - this.radius;
		if ((sp > radiiSum * radiiSum) || (innerDiff >= 0 && sp <= innerDiff * innerDiff)) {
			return inOrbit;
		}

		double radiiDiff = sphere.radius - this.radius;
		double innerSum = this.radius + innerRadius;
		if ((radiiDiff >= 0 && sp <= radiiDiff * radiiDiff) && (sp > innerSum * innerSum)) {
			this.getAllPoints(inOrbit);
			return inOrbit;
		}

		for (Vector v : points) {
			if (sphere.inRange(v) && !sphere.center.inRange(v, innerRadius)) {
				inOrbit.add(v);
			}
		}

		for (SphereSDSNode sN : children) {
			sN.getInOrbit(sphere, innerRadius, inOrbit);
		}

		return inOrbit;

	}

	public List<Vector> getNearest(int k, Vector p) {
		
		List<Vector> nearestK = new LinkedList<>();

		if (k >= containedPoints) {
			nearestK.addAll(getAllPoints());
			return nearestK;
		}
		
		if (k == 0)
			return nearestK;

		double d = this.center.distanceTo(p);
		Sphere collector = new Sphere(p, 0);
		
		int tooMuchCollected = containedPoints, notEnoughCollected = 0;
		int collected = 0;

		int steps = 0;
		int maxSteps = Math.max(10, (int) Math.ceil(Math.log(this.containedPoints / (double) k) / Math.log(2)));
		
		double lastNotEnoughRadius = Math.max(0, d - this.radius);
		double lastTooMuchRadius = d + this.radius;
		double collectRadius;
		double weight;
		
		while (collected != k && steps++ < maxSteps) {
			
			weight =  (k - notEnoughCollected) / (double) (tooMuchCollected - notEnoughCollected);
			
			collectRadius = weight * lastTooMuchRadius + (1 - weight) * lastNotEnoughRadius;
			collector.radius = collectRadius;
			collected = countInRange(collector);
			
			if (collected > k) {
				
				lastTooMuchRadius = collectRadius;
				tooMuchCollected = collected;
				
				collectRadius = (lastTooMuchRadius + lastNotEnoughRadius) / 2.0; // at least halve the interval
				collector.radius = collectRadius;
				collected = countInRange(collector);
				
				if (collected > k) {
					
					lastTooMuchRadius = collectRadius;
					tooMuchCollected = collected;
					
				} else {
	
					lastNotEnoughRadius = collectRadius;
					notEnoughCollected = collected;
				}
				
			} else {
				
				lastNotEnoughRadius = collectRadius;
				notEnoughCollected = collected;
				
				collectRadius = (lastTooMuchRadius + lastNotEnoughRadius) / 2.0; // at least halve the interval
				collector.radius = collectRadius;
				collected = countInRange(collector);
				
				if (collected > k) {
					
					lastTooMuchRadius = collectRadius;
					tooMuchCollected = collected;
					
				} else {
	
					lastNotEnoughRadius = collectRadius;
					notEnoughCollected = collected;
				}

			}

		}

		collector.radius = lastNotEnoughRadius;
		nearestK = getInRange(collector);
		
		k -= nearestK.size();

		if (k > 0) {
			
			collector.radius = lastTooMuchRadius;
			List<Vector> candidates = getInOrbit(collector, lastNotEnoughRadius);
			
			if (k <= candidates.size()){						
				Collections.sort(candidates, (v1,v2) -> Double.compare(v1.distanceTo(p),(v2.distanceTo(p))));
				nearestK.addAll(candidates.subList(0, k));
			} else {
				System.out.println("numerical error (getNearest), k = " + k + ", candidates = " + candidates.size());
			}

		}
		
		return nearestK;

	}

}
