package com.epl603.ava.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.epl603.ava.classes.DrawingThread;
import com.epl603.ava.classes.PointPath;
import com.epl603.ava.activities.BioMedActivity;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback {
	private DrawingThread _thread;
	private ArrayList<PointPath> _graphics = new ArrayList<PointPath>();

	private Paint mPaint;
	public boolean isDrawMode;
	public int currentPathIndex = 0;
	private boolean isCleanRequest = true;
	private boolean pointsChange = false;
	
	private Matrix matrix = new Matrix();
	private float matrix_x = 0;
	private float matrix_y = 0;
	private float start_x = 0;
	private float start_y = 0;

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

	public ArrayList<PointPath> get_graphics() {
		return _graphics;
	}

	public void set_graphics(ArrayList<PointPath> _graphics) {
		this._graphics = _graphics;
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

	public void ClearROIs() {
		_graphics = new ArrayList<PointPath>();
		currentPathIndex = 0;
		isCleanRequest = true;
		this.invalidate();
	}

	public void CloseActivePath() {
		if (_graphics.size() == currentPathIndex
				|| _graphics.get(currentPathIndex) == null) {
			return;
		}

		((PointPath) _graphics.get(currentPathIndex)).close();
		currentPathIndex++;
		pointsChange = true;
	}

	public boolean onTouchEvent(MotionEvent event) {
		synchronized (_thread.getSurfaceHolder()) {

			if (!isDrawMode) {
				dumpEvent(event);

				boolean isZoom = false;
				boolean isMove = false;

				//float start_x = 0;
				//float start_y = 0;

				int action = event.getAction();
				int actionCode = action & MotionEvent.ACTION_MASK;

				int numevents = event.getPointerCount();
				// int action = event.getAction();
				// 1 = normal action (scroll); 2 = multitouch (zoom in/out)
				Log.v("Zoom", "PointerCount=" + numevents);
				if (numevents == 1) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// Remember our initial down event location.
						start_x = event.getRawX();
						start_y = event.getRawY();
						break;
					case MotionEvent.ACTION_MOVE:
						float x = event.getRawX();
						float y = event.getRawY();

						
						matrix.setTranslate(x - start_x + matrix_x, y - start_y + matrix_y);
						
						//matrix_x += x - start_x;
						//matrix_y += x - start_y;
						
						// TODO: how to handle zoom in/out?
					//	invalidate(); // force a redraw
						pointsChange = true;
						
						
						
						break;
					case MotionEvent.ACTION_UP:
						matrix_x += event.getRawX() - start_x;
						matrix_y += event.getRawY() - start_y;
						break;
					}
				}
				/*
				 * else { switch (event.getAction()) { case
				 * MotionEvent.ACTION_DOWN: // Remember our initial down event
				 * location. start_x = event.getRawX(); start_y =
				 * event.getRawY(); break; case MotionEvent.ACTION_MOVE: float x
				 * = event.getRawX(); float y = event.getRawY(); scrollByX = x -
				 * startX; //move update x increment scrollByY = y - startY;
				 * //move update y increment startX = x; //reset initial values
				 * to latest startY = y; invalidate(); //force a redraw break; }
				 */
				/*
				 * if (actionCode == MotionEvent.ACTION_POINTER_DOWN) { start_x
				 * = event.getX(); start_y = event.getX(); } else if (actionCode
				 * == MotionEvent.ACTION_POINTER_UP) { end_x = event.getX();
				 * end_y = event.getX();
				 * 
				 * isZoom = true; }
				 * 
				 * if (actionCode == MotionEvent.ACTION_DOWN) { start_x =
				 * event.getX(); start_y = event.getX(); } else if (actionCode
				 * == MotionEvent.ACTION_UP) { end_x = event.getX(); end_y =
				 * event.getX();
				 * 
				 * isMove = true; }
				 * 
				 * if (isZoom) {
				 * 
				 * }
				 * 
				 * if (isMove) {
				 * 
				 * }
				 */

				return true;
			}

			pointsChange = true;

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

		// if (isCleanRequest) {
		// Bitmap bMap = BitmapFactory.decodeFile("/sdcard/test2.png");
		// canvas.drawBitmap(bMap, 0, 0, mPaint);
		canvas.drawColor(Color.BLACK);
		Bitmap mBitMap = BitmapFactory.decodeFile(BioMedActivity
				.getSelectedImagePath());
		// canvas.drawBitmap(mBitMap, 0, 0, null);
		canvas.drawBitmap(mBitMap, matrix, null);
		isCleanRequest = false;
		// }

		// if (pointsChange) {
		for (PointPath path : _graphics) {
			canvas.drawPath(path, mPaint);
		}
		pointsChange = false;
		// }

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

	public boolean needsRedraw() {
		return isCleanRequest || pointsChange;
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

	/** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
				"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				|| actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(
					action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";");
		}
		sb.append("]");
		Log.d("dumpEvent", sb.toString());
	}

}
