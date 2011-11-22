package com.epl603.ava.classes;

import java.util.Random;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;

public class FlagPair {
	private PointF start = null;
	private PointF finish = null;
	public boolean isPaired = false;
	private Path path;
	private float density;
	private double distanceInPixels;
	private int color = 0xFF93cd5f;
	
	private final int cross_side_size = 6;

	public FlagPair()
	{
		
	}
	
	public FlagPair(float start_x, float start_y, float _density, Matrix matrix)
	{
		//start = new PointF(start_x, start_y);
		setStart(start_x, start_y, matrix);
		path = new Path();
		density = _density;
		//path.moveTo(start_x, start_y);
	}
	
	public PointF getStart() {
		return start;
	}
	
	public void setStart(float start_x, float start_y) {
		this.start = new PointF(start_x, start_y);
	}
	
	public void setStart(float start_x, float start_y, Matrix matrix) {
		
		float[] values = new float[9];
		matrix.getValues(values);
		float dx = values[Matrix.MTRANS_X];
		float dy = values[Matrix.MTRANS_Y];
		float scale = values[Matrix.MSCALE_X];
	
		this.start = new PointF((start_x - dx)/scale, (start_y - dy)/scale);
	}
	
	public PointF getFinish() {
		return finish;
	}
	
	public void ClearFinish()
	{
		finish = null;
		isPaired = false;
	}
	
	public void setFinish(float finish_x, float finish_y) {
		this.finish = new PointF(finish_x, finish_y);
		path.reset();
		path.moveTo(start.x/*+cross_side_size/2*density*/, start.y/*+cross_side_size/2*density*/);
		path.lineTo(finish_x/*+cross_side_size/2*density*/, finish_y/*+cross_side_size/2*density*/);
	}
	
	public void setFinish(float finish_x, float finish_y, Matrix matrix) {
		
		float[] values = new float[9];
		matrix.getValues(values);
		float dx = values[Matrix.MTRANS_X];
		float dy = values[Matrix.MTRANS_Y];
		float scale = values[Matrix.MSCALE_X];
		
		this.finish = new PointF((finish_x - dx)/scale, (finish_y - dy)/scale);
		path.reset();
		path.moveTo(start.x/*+cross_side_size/2*density*/, start.y/*+cross_side_size/2*density*/);
		path.lineTo(finish.x/*+cross_side_size/2*density*/, finish.y/*+cross_side_size/2*density*/);
	}
	
	public void Pair()
	{
		CalculateDistance();
		
		isPaired = true;
	}
	
	public void CalculateDistance()
	{
		distanceInPixels = Math.sqrt(
				Math.pow(finish.y - start.y, 2) +
				Math.pow(finish.x - start.x, 2));
	}
	
	public int getColor() {
		return color;
	}

	public void setColor(Random rnd) {
		this.color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
	}

	public Path getPath() {
		return path;
	}
	
	public double getDistanceInPixels() {
		return distanceInPixels;
	}

	public void setDistanceInPixels(double distanceInPixels) {
		this.distanceInPixels = distanceInPixels;
	}

	public Path getStartCrossPath()
	{
		float left_x = start.x-cross_side_size/2*density;
		float right_x = start.x+cross_side_size/2*density;
		float top_y = start.y-cross_side_size/2*density;
		float bottom_y = start.y+cross_side_size/2*density;
		
		Path p = new Path();
		p.moveTo(left_x, top_y);
		p.lineTo(right_x, bottom_y);
		p.moveTo(left_x, bottom_y);
		p.lineTo(right_x, top_y);
		return p;
	}
	
	public Path getFinishCrossPath()
	{
		float left_x = finish.x-cross_side_size/2*density;
		float right_x = finish.x+cross_side_size/2*density;
		float top_y = finish.y-cross_side_size/2*density;
		float bottom_y = finish.y+cross_side_size/2*density;
		
		Path p = new Path();
		p.moveTo(left_x, top_y);
		p.lineTo(right_x, bottom_y);
		p.moveTo(left_x, bottom_y);
		p.lineTo(right_x, top_y);
		return p;
	}
	
	public void setPath(Path path) {
		this.path = path;
	}
	
}
