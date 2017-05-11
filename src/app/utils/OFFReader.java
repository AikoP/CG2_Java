package app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.Iterator;

import math.model3D.Face3D;
import math.model3D.Object3D;
import math.points.Point3D;

public class OFFReader {
	
	public static Object3D getObjectFromExt(String fileName) throws FileNotFoundException, IOException {
		
		FileReader fr = new FileReader(new File(fileName));
		BufferedReader br = new BufferedReader(fr);
		
		List<String> lineList = new ArrayList<>();
		String line;

		while ( (line = br.readLine()) != null){
			lineList.add(line);
		}
		
		return getObjectFromLines(lineList);
	}
	
	public static Object3D getNormalizedObjectFromExt(String fileName) throws FileNotFoundException, IOException {

		Object3D object =  getObjectFromExt(fileName);
		return object == null ? null : object.normalize();
		
	}
	
	public static Object3D getObjectFromResource(String fileName) throws FileNotFoundException, IOException {

		InputStream res = OFFReader.class.getResourceAsStream("/" + fileName + ".off");

		if (res == null)
			return null;
	
		BufferedReader br = new BufferedReader(new InputStreamReader(res));

		List<String> lineList = new ArrayList<>();
		String line;

		while ( (line = br.readLine()) != null){
			lineList.add(line);
		}
		
		return getObjectFromLines(lineList);
	}
	
	public static Object3D getNormalizedObjectFromResource(String fileName) throws FileNotFoundException, IOException {

		Object3D object =  getObjectFromResource(fileName);
		return object == null ? null : object.normalize();
		
	}

//	public static Object3D getObjectFromOff(String fileName) throws IOException {
//
//		List<Point3D> vertices = new ArrayList<>();
//		List<Face3D> faces = new ArrayList<>();
//		
//
//		InputStream res = OFFReader.class.getResourceAsStream("/" + fileName + ".off");
//
//		if (res == null){
//			return null;
//		}
//		
//		
//		BufferedReader reader = new BufferedReader(new InputStreamReader(res));
//
//		String line = null;
//
//		int vertexCount = 0, faceCount = 0;
//
//		// read header
//		try {
//			if ((line = reader.readLine()) != null) {
//				if (!line.equals("OFF")) {
//					System.out.println("no OFF file!");
//					return null;
//				}
//			} else {
//				reader.close();
//				return null;
//			}
//			if ((line = reader.readLine()) != null) {
//				String[] nums = line.split(" ");
//				if (nums.length > 1) {
//					vertexCount = Integer.valueOf(nums[0]);
//					faceCount = Integer.valueOf(nums[1]);
//				}
//			} else {
//				reader.close();
//				return null;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		// read vertices
//		int n = 0;
//		try {
//			while (n++ < vertexCount && (line = reader.readLine()) != null) {
//				String[] coords = line.trim().split("\\s+");
//				if (coords.length > 2) {
//					double x = Double.valueOf(coords[0]);
//					double y = Double.valueOf(coords[1]);
//					double z = Double.valueOf(coords[2]);
//					vertices.add(new Point3D(x, y, z));
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		// read faces
//		n = 0;
//		try {
//			while (n++ < faceCount && (line = reader.readLine()) != null) {
//				// line.replaceAll(" ", " ");
//				String[] info = line.trim().split("\\s+", 2);
//				if (info.length < 2)
//					continue;
//				String[] coords = info[1].trim().split("\\s+");
//
//				Point3D v1 = null, v2 = null, v3 = null, v4 = null;
//
//				if (coords.length > 1) {
//					v1 = vertices.get(Integer.valueOf(coords[0]));
//					v2 = vertices.get(Integer.valueOf(coords[1]));
//					if (v1 == null || v2 == null)
//						continue;
//				}
//				if (coords.length > 2) {
//					v3 = vertices.get(Integer.valueOf(coords[2]));
//					if (v3 == null)
//						continue;
//				}
//				if (coords.length > 3) {
//					v4 = vertices.get(Integer.valueOf(coords[3]));
//					if (v4 == null)
//						continue;
//				}
//
//				if (info[0].equals("2")) {
//					faces.add(new Face3D(v1, v2, v2));
//				} else if (info[0].equals("3")) {
//					faces.add(new Face3D(v1, v2, v3));
//				} else if (info[0].equals("4")) {
//					faces.add(new Face3D(v1, v2, v4));
//					faces.add(new Face3D(v3, v2, v4));
//				}
//
//			}
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return new Object3D(vertices, faces);
//
//	}
	
	public static Object3D getObjectFromLines(List<String> lines){

		List<Point3D> vertices = new ArrayList<>();
		List<Face3D> faces = new ArrayList<>();
		
		Iterator<String> it = lines.iterator();
		
		int vertexCount = 0, faceCount = 0;
		String line;

		// read header
		if (it.hasNext() && (line = it.next()) != null) {
			if (!line.trim().equals("OFF")) {
				System.out.println("no OFF file!");
				return null;
			}
		} else {
			return null;
		}
		if (it.hasNext() && (line = it.next()) != null) {
			String[] nums = line.trim().split(" ");
			if (nums.length > 1) {
				vertexCount = Integer.valueOf(nums[0]);
				faceCount = Integer.valueOf(nums[1]);
			}
		} else {
			return null;
		}

		// read vertices
		int n = 0;
		while (n++ < vertexCount && it.hasNext() && (line = it.next()) != null) {
			String[] coords = line.trim().split("\\s+");
			if (coords.length > 2) {
				double x = Double.valueOf(coords[0]);
				double y = Double.valueOf(coords[1]);
				double z = Double.valueOf(coords[2]);
				vertices.add(new Point3D(x, y, z));
			}
		}

		// read faces
		n = 0;
		while (n++ < faceCount && it.hasNext() && (line = it.next()) != null) {
			String[] info = line.trim().split("\\s+", 2);
			if (info.length < 2)
				continue;
			
			int dimension = Integer.valueOf(info[0].trim());
			
			String[] coords = info[1].trim().split("\\s+");

			Point3D v1 = null, v2 = null, v3 = null, v4 = null;

			if (dimension > 1 && coords.length > 1) {
				v1 = vertices.get(Integer.valueOf(coords[0]));
				v2 = vertices.get(Integer.valueOf(coords[1]));
				if (v1 == null || v2 == null)
					continue;
			}
			if (dimension > 2 && coords.length > 2) {
				v3 = vertices.get(Integer.valueOf(coords[2]));
				if (v3 == null)
					continue;
			}
			if (dimension > 3 && coords.length > 3) {
				v4 = vertices.get(Integer.valueOf(coords[3]));
				if (v4 == null)
					continue;
			}

			if (info[0].equals("2")) {
				faces.add(new Face3D(v1, v2, v2));
			} else if (info[0].equals("3")) {
				faces.add(new Face3D(v1, v2, v3));
			} else if (info[0].equals("4")) {
				faces.add(new Face3D(v1, v2, v4));
				faces.add(new Face3D(v3, v2, v4));
			}
		}

		return new Object3D(vertices, faces);

	}

//	public static Object3D getNormalizedObjectFromOff(String fileName) throws IOException{
//		Object3D object =  getObjectFromOff(fileName);
//		return object == null ? null : object.normalize();
//	}

}
