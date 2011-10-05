package com.epl603.ava.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.epl603.ava.classes.DrawingThread;
import com.epl603.ava.classes.PointPath;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback {
	private DrawingThread _thread;
	private ArrayList<PointPath> _graphics = new ArrayList<PointPath>();

	private Paint mPaint;
	public boolean isDrawMode;
	public int currentPathIndex = 0;

	public DrawingPanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initializeView();
	}

	public DrawingPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initializeView();
	}

	public DrawingPanel(Context context) {
		super(context);
		initializeView();
	}

	private void initializeView() {
		getHolder().addCallback(this);
		_thread = new DrawingThread(getHolder(), this);

		initializePaint();
	}

	public void StopDrawThread() {
		_thread.stop();
		_thread.suspend();
	}

	public void enterDrawMode() {
		isDrawMode = true;
	}

	public void exitDrawMode() {
		isDrawMode = false;
	}
	
	public void ClearROIs()
	{
		_graphics = new ArrayList<PointPath>();
		currentPathIndex = 0;
		this.invalidate();
	}

	public void CloseActivePath() {
		if (_graphics.size() == 0 || _graphics.get(currentPathIndex) == null) {
			return;
		}

		((PointPath) _graphics.get(currentPathIndex)).close();
		currentPathIndex++;
	}

	public boolean onTouchEvent(MotionEvent event) {
		synchronized (_thread.getSurfaceHolder()) {

			if (!isDrawMode)
				return true;

			if (currentPathIndex > _graphics.size() - 1) {
				_graphics.add(new PointPath());
			}

			PointPath myPath = _graphics.get(currentPathIndex);

			if (myPath == null) {
				myPath = new PointPath();
			}

			myPath.addPoint(event.getX(), event.getY());
			_graphics.remove(currentPathIndex);
			_graphics.add(myPath);

			return true;
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		
		for (PointPath path : _graphics) {
			canvas.drawPath(path, mPaint);
		}
		if (_graphics.size() == 0)
		{
			canvas = new Canvas();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (_thread.isAlive()) {

		} else {
			try {
				_thread.setRunning(true);
				_thread.start();
			} catch (Exception ex) {
				_thread = null;
				_thread = new DrawingThread(getHolder(), this);
				_thread.setRunning(true);
				_thread.start();
			}
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		boolean retry = true;
		_thread.setRunning(false);
		while (retry) {
			try {
				_thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// we will try it again and again...
			}
		}
	}

	private void initializePaint() {
		// initialize paint
		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(0xFFFFFF00);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(3);
	}
}
