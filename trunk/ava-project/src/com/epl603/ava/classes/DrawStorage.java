package com.epl603.ava.classes;

import java.util.ArrayList;

public class DrawStorage {
	private static DrawStorage storage = null;
	
	private ArrayList<PointPath> paths;
	private ArrayList<FlagPair> pairs;
	
	public ArrayList<FlagPair> getPairs() {
		
		return pairs;
	}

	public void setPairs(ArrayList<FlagPair> pairs) {
		
		this.pairs = pairs;
	}

	public ArrayList<PointPath> getPaths() {
		
		return paths;
	}
		
	public void setPaths(ArrayList<PointPath> paths)
	{
		this.paths = paths;
	}
	
	public DrawStorage() {
		paths = new ArrayList<PointPath>();
		pairs = new ArrayList<FlagPair>();
	}
	
	public static DrawStorage getStorage()
	{
		if (storage == null)
			storage = new DrawStorage();
		
		return storage;
	}
	
	public void clearStorage()
	{
		paths.clear();
		pairs.clear();
	}
}
