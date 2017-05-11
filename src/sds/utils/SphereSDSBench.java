package sds.utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import app.utils.ArgScanner;
import app.utils.OFFReader;
import math.Sphere;
import math.model3D.Object3D;
import math.points.Point3D;
import math.points.PointBasic;
import math.points.Vector;
import sds.structures.SphereSDSNode;
import sds.structures.SphereSDSRoot;

public class SphereSDSBench {
	
	public static boolean DEBUG = true;
	
	private static long timeLog = 0;
	
	public static List<Vector> getRandomPoints(int dim, int n) {
		
		if (DEBUG) timeLog = System.currentTimeMillis();

		List<Vector> pts = new LinkedList<>();
		Random r = new Random();
		
		pts = new ArrayList<>();
		for (int i = 0; i < n; i++){
			pts.add(PointBasic.getRandom(dim, r)); 
		}
		
		if (DEBUG) System.out.println("created " + pts.size() + " points of dimension " + dim + " (" + (System.currentTimeMillis() - timeLog) + "ms)\n");
		
		return pts;
	}
	
	public static List<Sphere> getRandomSpheres(int dim, int n) {

		if (DEBUG) timeLog = System.currentTimeMillis();
		
		List<Sphere> spheres = new LinkedList<>();
		Random r = new Random();
		
		for (int i = 0; i < n; i++){
			spheres.add(Sphere.getRandom(dim, r)); 
		}
		
		if (DEBUG) System.out.println("created " + spheres.size() + " spheres of dimension " + dim + " (" + (System.currentTimeMillis() - timeLog) + "ms)\n");
		
		return spheres;
	}
	
	public static List<SphereSDSNode> getRandomTreeList(int pointNr){
		
		List<Vector> pts = getRandomPoints(3, pointNr);
		
		SphereSDSRoot sphereRoot = new SphereSDSRoot(pts.get(0), 0, 100, -1);
		sphereRoot.add(pts);
		
		return sphereRoot.getTreeList();
		
	}
	
	public static List<SphereSDSNode> getRandomTreeList(List<Vector> pts){
		
		SphereSDSRoot sphereRoot = new SphereSDSRoot(pts.get(0), 0, 100, -1);
		sphereRoot.add(pts);
		
		return sphereRoot.getTreeList();
		
	}

	public static void main(String[] args) {
		
		HashMap<String, String> argList = ArgScanner.getArgList(args);
		
		int DIMENSION = 2;
		int N = 1000000;
		
		int NODECAP = 100;
		int REQUESTS = 100;
		int K = 10;
		int MAX_DEPTH = -1;
		
		boolean COMPARE = false;
		
		if (argList.containsKey("-dim")) DIMENSION = Integer.valueOf(argList.get("-dim"));
		if (argList.containsKey("-n")) N = Integer.valueOf(argList.get("-n"));
		if (argList.containsKey("-cap")) NODECAP = Integer.valueOf(argList.get("-cap"));
		if (argList.containsKey("-req")) REQUESTS = Integer.valueOf(argList.get("-req"));
		if (argList.containsKey("-k")) K = Integer.valueOf(argList.get("-k"));
		if (argList.containsKey("-maxd")) MAX_DEPTH = Integer.valueOf(argList.get("-maxd"));
		
		if (argList.containsKey("-comp")) COMPARE = true;
		
		List<Vector> pts = new ArrayList<>();
		
		if (argList.containsKey("-model")){
			
			Object3D object = null;
			try {
				object = OFFReader.getNormalizedObjectFromResource(argList.get("-model"));
			} catch (IOException e) {}
			
			if (object == null){
				System.out.println("file '" + argList.get("-model") + "' not found..");
				return;
			}
			
			pts.addAll(object.getVertices());
			
			DIMENSION = 3;
			N = pts.size();
			
			System.out.println("succesfully loaded model '" + argList.get("-model") + "'..");
			
		} else {
			pts = getRandomPoints(DIMENSION, N);
		}
		
		System.out.println("Dimension = " + DIMENSION);
		System.out.println("Vertex Count = " + N);
		System.out.println("Bucket Size = " + NODECAP);
		System.out.println("Request Count = " + REQUESTS);
		System.out.println("Maximum Tree Depth = " + MAX_DEPTH);
		System.out.println("K (getNearest) = " + K);
		System.out.println();
		
		
		SphereSDSRoot sphereRoot = new SphereSDSRoot(pts.get(0), 0, NODECAP, MAX_DEPTH);
		
		System.out.println("building tree..");
		timeLog = System.currentTimeMillis();
		sphereRoot.add(pts);
		System.out.println("build time = " + (System.currentTimeMillis() - timeLog) + "ms\n");	
		
		System.out.println("number of spheres = " + sphereRoot.getTreeList().size());
		System.out.println("treeDepth = " + sphereRoot.getHeight());
		
		System.out.println("\n---------------------------\n");
		
//		Benchmark requests
		
		List<Sphere> requestSpheres = getRandomSpheres(DIMENSION, REQUESTS);
		List<Vector> requestPoints = getRandomPoints(DIMENSION, REQUESTS);
		
		// getInRange
		System.out.println("benchmarking 'getInRange'..");
		timeLog = System.currentTimeMillis();
		for (Sphere req : requestSpheres){
			sphereRoot.getInRange(req);
		}
		System.out.println("average request time (getInRange, " + REQUESTS + " samples) = " + ((System.currentTimeMillis() - timeLog) / (double) REQUESTS) + " ms\n");	
		
		// getNearestK
		System.out.println("benchmarking 'getNearestK'..");
		timeLog = System.currentTimeMillis();
		for (Vector p : requestPoints){
			sphereRoot.getNearest(K, p);
		}
		System.out.println("average request time (getNearest[" + K + "], " + REQUESTS + " samples) = " + ((System.currentTimeMillis() - timeLog) / (double) REQUESTS) + " ms");

		if (!COMPARE) return;
		
		System.out.println("\n---------------------------\n");
		
		// linear banchmark
		System.out.println("benchmarking 'getInRange' (linear)..");
		timeLog = System.currentTimeMillis();
		List<Vector> inRange = new LinkedList<>();
		for (Sphere req : requestSpheres){
			for (Vector v : pts){
				if (v.distanceTo(req.center) < req.radius){
					inRange.add(v);
				}
			}
		}
		System.out.println("average request time (getInRange (linear), " + REQUESTS + " samples) = " + ((System.currentTimeMillis() - timeLog) / (double) REQUESTS) + " ms\n");	
		
		System.out.println("benchmarking 'getNearestK' (linear)..");
		timeLog = System.currentTimeMillis();
		List<Vector> nearestK = new LinkedList<>();
		for (Vector p : requestPoints) {
			nearestK.clear();
			while (nearestK.size() < K && pts.size() > nearestK.size()) {
				Vector min = pts.get(0);
				double minDist = min.distanceTo(p);
				for (Vector v : pts) {
					if (nearestK.contains(v)) continue;
					double d = v.distanceTo(p);
					if (d < minDist) {
						min = v;
						minDist = d;
					}
				}
				nearestK.add(min);
			}
		}
		System.out.println("average request time (getNearest[" + K + "] (linear), " + REQUESTS + " samples) = " + ((System.currentTimeMillis() - timeLog) / (double) REQUESTS) + " ms");
		
		
	}

}
