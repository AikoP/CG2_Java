package sds.structures;

import java.util.ArrayList;
import java.util.List;

import math.EuclidSpace;
import math.Sphere;
import math.VectorMath;
import math.points.Vector;

public class SphereSDSNode extends Sphere {

	public List<SphereSDSNode> children;
	public List<Vector> points;
	private Vector rootCenter;
	
	public int capacity = 10, depth = 0, containedPoints = 0, maxDepth = 0;
	
	public SphereSDSNode(Vector c, double r, int capacity, int depth, int maxDepth, Vector rootCenter) {
		
		super(c, r);

		this.children = new ArrayList<SphereSDSNode>();
		this.points = new ArrayList<Vector>();
		
		this.capacity = capacity;
		this.depth = depth;
		this.maxDepth = maxDepth;
		this.rootCenter = rootCenter;
		
	}
	
	public boolean isLeaf(){
		return children.isEmpty();
	}

	public void add(List<Vector> pts) {

		this.setBoundingSphere(pts);
		
		setOrbit(rootCenter);
		
		containedPoints = pts.size();
		
		if (pts.size() <= capacity || (maxDepth >= 0 && depth >= maxDepth)) {
			points.addAll(pts);
		} else {
			this.split(pts);
		}

	}

	private void split(List<Vector> pts) {

		List<Vector> isolated = VectorMath.getIsolated(pts, (int) Math.pow(2, pts.get(0).getDimension()));
		if (isolated.isEmpty())
			return;

		List<List<Vector>> splits = new ArrayList<>();

		for (Vector isoPt : isolated) {
			this.children.add(new SphereSDSNode(isoPt, 0, this.capacity, depth + 1, maxDepth, rootCenter));
			splits.add(new ArrayList<>());
		}

		double minDist, d;
		int bestFit;

		for (Vector p : pts) {

			bestFit = 0;
			minDist = isolated.get(0).distanceTo(p);

			for (int i = 0; i < isolated.size(); i++) {
				d = isolated.get(i).distanceTo(p);
				if (d < minDist) {
					minDist = d;
					bestFit = i;
				}
			}

			splits.get(bestFit).add(p);

		}

		for (int i = 0; i < isolated.size(); i++) {
			this.children.get(i).add(splits.get(i));
		}

	}

	public void getTreeList(List<SphereSDSNode> treeList) {

		treeList.add(this);

		for (SphereSDSNode sN : children) {
			sN.getTreeList(treeList);
		}

	}

	public int getDepth() {
		return depth;
	}
	
	public int getHeight() {
		
		int maxHeight = - 1;
		for (SphereSDSNode sN : children){
			maxHeight = Math.max(maxHeight, sN.getHeight());
		}	
		return maxHeight + 1;
		
	}
	
	public void getAllPoints(List<Vector> allPoints){
		allPoints.addAll(points);
		for (SphereSDSNode sN : children){
			sN.getAllPoints(allPoints);
		}	
	}

	public void getInRange(Sphere sphere, List<Vector> inRange) {
		
		double sp = EuclidSpace.scalarProduct(this.center.minus(sphere.center));
		
		// if spheres do not overlap
		double radiiSum = this.radius + sphere.radius;
		if (sp > radiiSum * radiiSum){
			return;
		}
		
		// if 'sphere' fully contains 'this' SDSNode
		double radiiDiff = sphere.radius - this.radius;
		if(radiiDiff >= 0 && sp <= radiiDiff * radiiDiff){
			this.getAllPoints(inRange);
			return;
		}
		
		for (Vector v : points) {
			if (sphere.inRange(v)) {
				inRange.add(v);
			}
		}
		
		for (SphereSDSNode sN : children) {
			sN.getInRange(sphere, inRange);
		}
		
	}
	
	public int countInRange(Sphere sphere) {
		
		double sp = EuclidSpace.scalarProduct(this.center.minus(sphere.center));
		
		double radiiSum = this.radius + sphere.radius;
		if (sp > radiiSum * radiiSum){
			return 0;
		}
		
		double radiiDiff = sphere.radius - this.radius;
		if(radiiDiff >= 0 && sp <= radiiDiff * radiiDiff){
			return this.containedPoints;
		}

		int inRange = 0;
		for (Vector v : points) {
			if (sphere.inRange(v)) {
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
		
		int inRange = 0;
		for (Vector v : points) {
			if (sphere.inRange(v) && !sphere.center.inRange(v, innerRadius)){
				inRange++;
			}
		}

		for (SphereSDSNode sN : children) {
			inRange += sN.countInOrbit(sphere, innerRadius);
		}

		return inRange;

	}

	public void getInOrbit(Sphere sphere, double innerRadius, List<Vector> inOrbit) {

		double sp = EuclidSpace.scalarProduct(this.center.minus(sphere.center));
		
		double radiiSum = this.radius + sphere.radius;
		double innerDiff = innerRadius - this.radius;
		if ((sp > radiiSum * radiiSum) || (innerDiff >= 0 && sp <= innerDiff * innerDiff)) {
			return;
		}

		double radiiDiff = sphere.radius - this.radius;
		double innerSum = this.radius + innerRadius;
		if ((radiiDiff >= 0 && sp <= radiiDiff * radiiDiff) && (sp > innerSum * innerSum)) {
			this.getAllPoints(inOrbit);
			return;
		}

		for (Vector v : points) {
			if (sphere.inRange(v) && !sphere.center.inRange(v, innerRadius)){
				inOrbit.add(v);
			}
		}

		for (SphereSDSNode sN : children) {
			sN.getInOrbit(sphere, innerRadius, inOrbit);
		}
		
	}

}
