package com.epl603.ava.classes;

import java.util.ArrayList;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class PointPath extends Path{
	public ArrayList<TranslatedPoint> points;
	//public float dx = 0;
	//public float dy = 0;
	
	public PointPath()
	{
		super();
		points = new ArrayList<TranslatedPoint>();
	}
	
	public PointPath(ArrayList<TranslatedPoint> pts)
	{
		super();
		points = pts;
		this.moveTo(pts.get(0).x, pts.get(0).y);
		for (int i=1; i<pts.size(); i++)
		{
			this.lineTo(pts.get(i).x, pts.get(i).y);
		}
	}
	
	//public void addPoint(float x, float y)
	//{
	//	addPoint(x, y, 0, 0, 1);
	//}
	
	public void addPoint(float x, float y, float dx, float dy, float scale)
	{
		if (scale == 0)
			scale = 1;
		
		if (points == null)
		{
			points = new ArrayList<TranslatedPoint>();
		}
		
		if (points.size() == 0)
		{
			//this.moveTo(x- dx, y- dy);
			this.moveTo((x-dx)/scale, (y-dy)/scale);
		}
	
		this.lineTo((x-dx)/scale, (y-dy)/scale);
		points.add(new TranslatedPoint((x+dx)*scale, (y+dy)*scale, dx, dy, scale));
		
		//this.dx = dx;
		//this.dy = dy;
	}
	
	public void RemoveLastPoint()
	{
		points.remove(getPointsCount()-1);
		this.reset();
		
		TranslatedPoint pt = points.get(0);
		
		this.moveTo((pt.x - pt.dx - pt.dx)/pt.scale/pt.scale, (pt.y - pt.dy - pt.dy)/pt.scale/pt.scale);
		Log.d("START_POINT", points.get(0).x + ", " + points.get(0).y);
		for (int i=1; i<getPointsCount(); i++)
		{
			pt = points.get(i);
			this.lineTo((pt.x - pt.dx - pt.dx)/pt.scale/pt.scale, (pt.y - pt.dy - pt.dy)/pt.scale/pt.scale);
			Log.d("MID_POINT", points.get(0).x + ", " + points.get(0).y);
		}
		
	}
	
	public TranslatedPoint getPoint(int index)
	{
		return (TranslatedPoint)points.get(index);
	}
	
	public int getPointsCount()
	{
		if (points == null)
			return 0;
		
		return points.size();
	}
	
	//aris added this, to export points array list
	public ArrayList<TranslatedPoint> getROIpoints(){
		return points;
	}
	
	class TranslatedPoint extends PointF
	{
		public TranslatedPoint(float x, float y)
		{
			super(x, y);
			dx = 0;
			dy = 0;
			scale = 1;
		}
		
		public TranslatedPoint(float x, float y, float dx, float dy, float scale)
		{
			super(x, y);
			this.dx = dx;
			this.dy = dy;
			this.scale = scale;
		}
		
	
		public float scale;
		public float dx;
		public float dy;
	}

}
