package sds.utils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import math.Sphere;
import math.points.Point3D;
import math.points.Vector;
import sds.structures.SphereSDSNode;
import sds.structures.SphereSDSRoot;

public abstract class SDSManager {

	private List<Vector> vertices;
	private SphereSDSRoot sdsRoot;
	
	private Vector requestRoot;

	public SDSManager() {
		vertices = new ArrayList<>();
	}
	
	public int getVertexCount(){

		return vertices == null ? 0 : vertices.size();
		
	}
	
	public abstract void doneBuilding(List<SphereSDSNode> sdsNodes);
	public abstract void doneGetInRange(List<Vector> inRange);
	public abstract void doneGetNearestK(List<Vector> nearestK);
	
	public void setRequestRoot(Vector root){
		this.requestRoot = root;
	}

	public synchronized void build(List<Point3D> pts, int bucketSize, int maxTreeDepth) {
		
		if (pts == null) return;

		this.vertices.clear();
		this.vertices.addAll(pts);
		
		if (vertices.isEmpty()) return;
		
		SDSBuildWorker sdsBuilder = new SDSBuildWorker(bucketSize, maxTreeDepth);
		sdsBuilder.execute();
	}
	
	public synchronized void getInRange(double range) {
		
		if (sdsRoot != null){
			SDSGetInRangeWorker getInRangeWorker = new SDSGetInRangeWorker(range);
			getInRangeWorker.execute();
		}
		
	}
	
	public synchronized void getNearestK(int k) {
		
		if (sdsRoot != null){
			SDSNearestKWorker getNearestKWorker = new SDSNearestKWorker(k);
			getNearestKWorker.execute();
		}
		
	}

	class SDSBuildWorker extends SwingWorker<String, Object> {
		
		long timelog;
		
		int bucketSize, maxTreeDepth;
		
		public SDSBuildWorker(int bucketSize, int maxTreeDepth) {
			this.bucketSize = bucketSize;
			this.maxTreeDepth = maxTreeDepth;
		}
		
		@Override
		public String doInBackground() {
			timelog = System.currentTimeMillis();
			sdsRoot = new SphereSDSRoot(vertices.get(0), 0, bucketSize, maxTreeDepth);
			sdsRoot.add(vertices);
			System.out.println("finished SDSBuild (" + (System.currentTimeMillis() - timelog) + "ms)");
			return null;
		}

		@Override
		protected void done() {
			System.out.println("finished Buidling!");
			doneBuilding(sdsRoot.getTreeList());
		}
	}
	
	class SDSGetInRangeWorker extends SwingWorker<String, Object> {
		
		List<Vector> inRange;
		double range;
		long timelog;
		
		public SDSGetInRangeWorker(double range) {
			this.range = range;
		}

		@Override
		public String doInBackground() {
			timelog = System.nanoTime();
			inRange = sdsRoot.getInRange(new Sphere(requestRoot, range));
			System.out.println("finished inRange (" + (System.nanoTime() - timelog) / 1000 + "\u00B5s)");
			return null;
		}

		@Override
		protected void done() {
			doneGetInRange(inRange);
		}
	}
	
	class SDSNearestKWorker extends SwingWorker<String, Object> {

		List<Vector> nearestK;
		int k;
		long timelog;

		public SDSNearestKWorker(int k) {
			this.k = k;
//			timelog = System.nanoTime();
		}

		@Override
		public String doInBackground() {
			timelog = System.nanoTime();
			nearestK = sdsRoot.getNearest(k, requestRoot);
			System.out.println("finished nearestK (" + (System.nanoTime() - timelog) / 1000 + "\u00B5s)");
			return null;
		}

		@Override
		protected void done() {
//			System.out.println("finished nearestK! (" + (System.nanoTime() - timelog) / 1000000 + "ms)");
			doneGetNearestK(nearestK);
		}
	}

}
