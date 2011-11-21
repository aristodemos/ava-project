package com.epl603.ava.classes;

import android.graphics.Path;
import android.graphics.PointF;

public class FlagPair {
	private PointF start = null;
	private PointF finish = null;
	public boolean isPaired = false;
	private Path path;
	private float density;
	
	private final int cross_side_size = 6;

	public FlagPair()
	{
		
	}
	
	public FlagPair(float start_x, float start_y, float _density)
	{
		start = new PointF(start_x, start_y);
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
	public PointF getFinish() {
		return finish;
	}
	public void setFinish(float finish_x, float finish_y) {
		this.finish = new PointF(finish_x, finish_y);
		path.reset();
		path.moveTo(start.x/*+cross_side_size/2*density*/, start.y/*+cross_side_size/2*density*/);
		path.lineTo(finish_x/*+cross_side_size/2*density*/, finish_y/*+cross_side_size/2*density*/);
	}
	public Path getPath() {
		return path;
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
