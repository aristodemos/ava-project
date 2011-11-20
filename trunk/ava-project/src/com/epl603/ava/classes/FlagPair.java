package com.epl603.ava.classes;

import android.graphics.Path;
import android.graphics.PointF;

public class FlagPair {
	private PointF start = null;
	private PointF finish = null;
	public boolean isPaired = false;
	private Path path;
	private float density;

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
		path.moveTo(start.x+24*density, start.y+24*density);
		path.lineTo(finish_x+24*density, finish_y+24*density);
	}
	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	
}
