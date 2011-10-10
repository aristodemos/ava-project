package com.epl603.ava.classes;

import java.util.ArrayList;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

public class PointPath extends Path{
	public ArrayList<PointF> points;
	//public float dx = 0;
	//public float dy = 0;
	
	public PointPath()
	{
		super();
		points = new ArrayList<PointF>();
	}
	
	public PointPath(ArrayList<PointF> pts)
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
			points = new ArrayList<PointF>();
		}
		
		if (points.size() == 0)
		{
			//this.moveTo(x- dx, y- dy);
			this.moveTo((x-dx)/scale, (y-dy)/scale);
		}
	
		this.lineTo((x-dx)/scale, (y-dy)/scale);
		points.add(new PointF((x+dx)*scale, (x+dx)*scale));
		
		//this.dx = dx;
		//this.dy = dy;
	}
	
	public PointF getPoint(int index)
	{
		return (PointF)points.get(index);
	}
	
	public int getPointsCount()
	{
		if (points == null)
			return 0;
		
		return points.size();
	}
	
	//aris added this, to export points array list
	public ArrayList<PointF> getROIpoints(){
		return points;
	}
	
	/*class TranslatedPoint extends TranslatedPoint
	{
		public TranslatedPoint(int x, int y)
		{
			super(x, y);
			dx = 0;
			dy = 0;
			scale = 1;
		}
		
		public TranslatedPoint(int x, int y, float dx, float dy, float scale)
		{
			super(x, y);
			this.dx = dx;
			this.dy = dy;
			this.scale = scale;
		}
		
	
		public float scale;
		public float dx;
		public float dy;
	}*/

}
