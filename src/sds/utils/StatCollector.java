package sds.utils;

import math.EuclidSpace;

public class StatCollector {
	
	public static long totalNodeTime, nodesVisited, totalGetAllTime, totalGetSingleTime, totalSPTime, totalOverlapTime, totalSearchTime;
	
	
	public static void reset(){
		totalNodeTime = 0;
		nodesVisited = 0;
		totalGetAllTime = 0;
		totalGetSingleTime = 0;
		totalSPTime = 0;
		totalOverlapTime = 0;
		totalSearchTime = 0;
	}
	
	public static void visitNode(){
		nodesVisited++;
	}
	
	public static double getAverageNodeTime(){
		return totalNodeTime / (double) nodesVisited;
	}
	
	public static void printStats(){
		
		System.out.println();
		System.out.println("totalNodeTime      = " + totalNodeTime / (double) 1000000 + "ms");
		System.out.println("nodesVisited       = " + nodesVisited);
		System.out.println("totalSPTime        = " + totalSPTime / (double) 1000000 + "ms");
		System.out.println("totalOverlapTime   = " + totalOverlapTime / (double) 1000000 + "ms");
		System.out.println("totalGetAllTime    = " + totalGetAllTime / (double) 1000000 + "ms");
		System.out.println("totalGetSingleTime = " + totalGetSingleTime / (double) 1000000 + "ms");
		System.out.println("totalSearchTime    = " + totalSearchTime / (double) 1000000 + "ms");
//		System.out.println();
//		System.out.println("inRange calls      = " + EuclidSpace.inRangeCalls);
		
		reset();
		
	}

}
