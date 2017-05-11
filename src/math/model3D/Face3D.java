package math.model3D;

import math.points.Point3D;

public class Face3D {
	
	public Point3D p1, p2, p3;
	
	public Face3D(Point3D p1, Point3D p2, Point3D p3){
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public String toString(){
		return "[" + p1.toString() + ", " + p2.toString() + ", " + p3.toString() + "]";
	}

}
