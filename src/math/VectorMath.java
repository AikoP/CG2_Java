package math;

import java.util.ArrayList;
import java.util.List;

import math.EuclidSpace;
import math.GlobalScalarProduct;
import math.ScalarProduct;
import math.points.Vector;

public class VectorMath {
	
	public static List<Vector> getIsolated(List<Vector> pts, int k){
		
		if (k >= pts.size()) return pts;
		
		List<Vector> isolated = new ArrayList<>(k);
		
		if (k == 0) return isolated;
		
		isolated.add(getFurthest(getCenter(pts), pts));
		
		while (k-- > 0){
			
			Vector firstIso = isolated.get(0); 
			Vector mostIsolated = firstIso;
			
			double maxIsoDist = 0;
					
			for (Vector pt : pts){
				
				double isoDist = EuclidSpace.distance(pt, firstIso);
				
				for (Vector iso : isolated){
					isoDist = Math.min(isoDist, EuclidSpace.distance(pt, iso));
				}
				
				if (isoDist > maxIsoDist){
					maxIsoDist = isoDist;
					mostIsolated = pt;
				}
				
			}
			
			isolated.add(mostIsolated);
			
		}

		
		return isolated;
		
	}

	public static Vector getFurthest(Vector p, List<Vector> pts) {

		Vector furthest = p.clone();
		double maxDist = 0;
		
		for (Vector pt : pts){
			double d = EuclidSpace.distance(pt, p);
			if (d > maxDist){
				furthest = pt;
				maxDist = d;
			}
		}
		
		return furthest;
		
	}
	
	public static Vector getNearest(Vector p, List<Vector> pts) {

		if (pts.isEmpty()) return null;
		
		Vector nearest = pts.get(0).clone();
		double minDist = EuclidSpace.distance(p, pts.get(0));
		
		for (Vector pt : pts){
			double d = EuclidSpace.distance(pt, p);
			if (pt != p && d < minDist){
				nearest = pt;
				minDist = d;
			}
		}
		
		return nearest;
		
	}

	public static Vector getCenter(List<Vector> pts) {

		if (pts.isEmpty()) return null;
		
		Vector center = pts.get(0).getNull();
		
		for(Vector p : pts){
			center.add(p);
		}
		
		return center.div((double) pts.size());
		
	}
	
	public static Vector getCenter(Vector a, Vector b) {

		return a.plus(b).div(2.0);
		
	}

}
