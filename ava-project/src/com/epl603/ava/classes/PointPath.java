package com.epl603.ava.classes;

import java.util.ArrayList;

import android.graphics.Path;
import android.graphics.Point;

public class PointPath extends Path{
	public ArrayList<Point> points;

	
	public PointPath()
	{
		super();
		points = new ArrayList<Point>();
	}
	
	public void addPoint(float x, float y)
	{
		if (points == null)
		{
			points = new ArrayList<Point>();
		}
		
		if (points.size() == 0)
		{
			this.moveTo(x, y);
		}
	
		this.lineTo(x, y);
		points.add(new Point((int) x, (int) x));
	}
	
	public Point getPoint(int index)
	{
		return (Point)points.get(index);
	}
	
	public int getPointsCount()
	{
		if (points == null)
			return 0;
		
		return points.size();
	}

}
