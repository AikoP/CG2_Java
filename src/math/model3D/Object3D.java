package math.model3D;

import java.util.ArrayList;
import java.util.List;

import math.points.Point3D;

public class Object3D {
	
	private List<Point3D> vertices;
	private List<Face3D> faces;
	
	public Object3D(List<Point3D> vertices,	List<Face3D> faces){
		
		this.vertices = new ArrayList<>();
		this.faces = new ArrayList<>();
		
		if (vertices != null){
			this.vertices.addAll(vertices);
		}
		
		if (faces != null){
			this.faces.addAll(faces);
		}
		
	}
	
	public List<Point3D> getVertices(){
		return this.vertices;
	}
	
	public List<Face3D> getFaces(){
		return this.faces;
	}
	
	public Object3D normalize(){
		
		if (vertices.isEmpty()) return this;
		
		Point3D p = vertices.get(0);
		
		double maxX = p.x, minX = p.x, maxY = p.y, minY = p.y, maxZ = p.z, minZ = p.z; 
		
		for (Point3D v : vertices){
			if (v.x < minX) minX = v.x;
			if (v.x > maxX) maxX = v.x;
			if (v.y < minY) minY = v.y;
			if (v.y > maxY) maxY = v.y;
			if (v.z < minZ) minZ = v.z;
			if (v.z > maxZ) maxZ = v.z;
		}
		
		double xSize = maxX - minX, ySize = maxY - minY, zSize = maxZ - minZ;
		
		double scale = 2 / Math.max(xSize, Math.max(ySize, zSize));
		
		double xOffSet = - (maxX + minX) / 2, yOffSet = - (maxY + minY) / 2, zOffSet = - (maxZ + minZ) / 2;
		
		for (Point3D v : vertices){
			v.x = (v.x + xOffSet) * scale;
			v.y = (v.y + yOffSet) * scale;
			v.z = (v.z + zOffSet) * scale;
		}
		
		return this;
		
		
	}
	

}
