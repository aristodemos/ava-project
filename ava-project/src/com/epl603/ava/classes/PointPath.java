package com.epl603.ava.classes;

import java.util.ArrayList;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;

public class PointPath extends Path{
	public ArrayList<TranslatedPoint> points;
	public int isClosed;
	
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
	
	public void addPoint(float x, float y)
	{
		addPoint(x, y, 0, 0, 1, false);
	}
	
	public void addPoint(float x, float y, float dx, float dy, float scale)
	{
		addPoint(x, y, dx, dy, scale, false);
	}
	
	public void addPoint(float x, float y, float dx, float dy, float scale, boolean isBreak)
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
		points.add(new TranslatedPoint((x-dx)/scale, (y-dy)/scale, dx, dy, scale, isBreak));

	}
	
	@Override
	public void close() {
		if (points.size() > 0)
		{
			points.get(points.size()-1).isClosing = true;
			this.isClosed = AppConstants.CLOSED_TRUE;
			super.close();
		}
	}

	// returns false if the point list is empty
	public boolean RemoveLastPoint()
	{
		//points.remove(getPointsCount()-1);
		
		if (getPointsCount() == 0)
			return false;
		
		while (true)
		{			
			points.remove(getPointsCount()-1);
			
			if (points.size() == 0)
				break;
			
			if (points.get(getPointsCount()-1).isBreak)
				break;
		}
		this.reset();
		
		if (getPointsCount() == 0)
			return true;
		
		TranslatedPoint pt = points.get(0);
		//pt.postMatrix(matrix);
		
		/*this.moveTo((pt.x - pt.dx - pt.dx)/pt.scale/pt.scale, (pt.y - pt.dy - pt.dy)/pt.scale/pt.scale);
		Log.d("START_POINT", points.get(0).x + ", " + points.get(0).y);
		for (int i=1; i<getPointsCount(); i++)
		{
			pt = points.get(i);
			this.lineTo((pt.x - pt.dx - pt.dx)/pt.scale/pt.scale, (pt.y - pt.dy - pt.dy)/pt.scale/pt.scale);
			//Log.d("MID_POINT", points.get(0).x + ", " + points.get(0).y);
		}*/
		
		this.moveTo(pt.x, pt.y);
		for (int i=1; i<getPointsCount(); i++)
		{
			pt = points.get(i);
			this.lineTo(pt.x, pt.y);
		}

		return true;
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
			isClosing = false;
		}
		
		public TranslatedPoint(float x, float y, float dx, float dy, float scale)
		{
			this(x, y, dx, dy, scale, false);
		}
		
		public TranslatedPoint(float x, float y, float dx, float dy, float scale, boolean breaks)
		{
			super(x, y);
			this.dx = dx;
			this.dy = dy;
			this.scale = scale;
			this.isBreak = breaks;
			this.isClosing = false;
		}
		
		public void postMatrix(Matrix matrix)
		{
			float[] values = new float[9];
			matrix.getValues(values);
			
			this.dx += values[Matrix.MTRANS_X];
			this.dy += values[Matrix.MTRANS_Y];
			this.scale *= values[Matrix.MSCALE_X];
		}
	
		public float scale;
		public float dx;
		public float dy;
		public boolean isBreak;
		public boolean isClosing;
		
	}

}
