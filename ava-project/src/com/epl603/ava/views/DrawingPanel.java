package com.epl603.ava.views;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.epl603.ava.R;
import com.epl603.ava.activities.BioMedActivity;
import com.epl603.ava.classes.DrawingThread;
import com.epl603.ava.classes.FlagPair;
import com.epl603.ava.classes.PointPath;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback {
	private DrawingThread _thread;
	
	private ArrayList<PointPath> _pointPaths = new ArrayList<PointPath>();
	public int currentPathIndex = 0;
	
	private ArrayList<FlagPair> _flagPairs = new ArrayList<FlagPair>();
	public int currentFlagIndex = 0;

	private Bitmap mBitMap = BitmapFactory.decodeFile(BioMedActivity
			.getSelectedImagePath());
	
	private Bitmap target_img = BitmapFactory.decodeResource(getResources(),
			R.drawable.target);
	
	private Bitmap cross_img = BitmapFactory.decodeResource(getResources(),
			R.drawable.cross);
	
	// Constants
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	
	private static final float TARGET_SIZE = 48F;
	private static final float TARGET_PADDING = 150F;
	
	private static final float minScale = 0.5F;
	private static final float maxScale = 6F;
	
	private static final String TAG = "Touch";

	// //////////////////////////////////
	
	private Paint mPaint;
	private Paint mPaintCrosses;
	public boolean isDrawMode;
	public boolean isFlagMode;
	private boolean isCleanRequest = true;
	public boolean pointsChange = false;
	private boolean targetDown = false;

	private Matrix matrix = new Matrix();
	private Matrix targetMatrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Matrix helperDrawMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float oldDist = 1f;
	private float scale = 1F;

	int mode = NONE;
	float density;

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

	public ArrayList<PointPath> save_graphics() {

		return _pointPaths;

	}

	public void set_graphics(ArrayList<PointPath> _graphics) {
		this._pointPaths = _graphics;
	}

	private void initializeView() {
		getHolder().addCallback(this);
		_thread = new DrawingThread(getHolder(), this);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		((WindowManager) this.getContext().getSystemService(
				Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
				displaymetrics);

		density = displaymetrics.density;

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

	public void switchFlagMode(boolean enabled) {
		isFlagMode = enabled;
	}

	public void ClearROIs() {
		_pointPaths = new ArrayList<PointPath>();
		currentPathIndex = 0;
		isCleanRequest = true;
		this.invalidate();
	}

	public void CloseActivePath() {
		if (_pointPaths.size() == currentPathIndex
				|| _pointPaths.get(currentPathIndex) == null) {
			return;
		}

		((PointPath) _pointPaths.get(currentPathIndex)).close();
		currentPathIndex++;
		pointsChange = true;
	}

	public void NextPath() {
		if (_pointPaths.size() == currentPathIndex
				|| _pointPaths.get(currentPathIndex) == null) {
			return;
		}
		currentPathIndex++;
	}

	public boolean onTouchEvent(MotionEvent event) {
		synchronized (_thread.getSurfaceHolder()) {

			boolean isBreakPoint = false;

			if (isFlagMode) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// savedMatrix.set(matrix);
					// start.set(event.getX(), event.getY());
					// Log.d(TAG, "mode=DRAG");
					// mode = DRAG;
					
					
					
					targetDown = true;
					targetMatrix.setTranslate(event.getX()-density*(TARGET_SIZE/2), event.getY()-(TARGET_PADDING/2)*density);
					break;
				/*
				 * case MotionEvent.ACTION_POINTER_2_DOWN: case
				 * MotionEvent.ACTION_POINTER_DOWN: oldDist = spacing(event);
				 * Log.d(TAG, "oldDist=" + oldDist); if (oldDist > 10f) {
				 * savedMatrix.set(matrix); midPoint(mid, event); mode = ZOOM;
				 * Log.d(TAG, "mode=ZOOM"); } break;
				 */
				case MotionEvent.ACTION_UP:
					if (currentFlagIndex < 0)
						currentFlagIndex = 0;
				
					if (_flagPairs.size() == 0 || _flagPairs.get(_flagPairs.size()-1).isPaired)
					{
						_flagPairs.add(new FlagPair(event.getX()-(TARGET_SIZE/2)*density, event.getY()-(TARGET_PADDING/2)*density, density));
					}
					else
					{
						_flagPairs.get(_flagPairs.size()-1).setFinish(event.getX()-(TARGET_SIZE/2)*density, event.getY()-(TARGET_PADDING/2)*density);
						_flagPairs.get(_flagPairs.size()-1).isPaired = true;
					}
										
					targetDown = false;
					break;
				case MotionEvent.ACTION_MOVE:
					targetMatrix.setTranslate(event.getX()-(TARGET_SIZE/2)*density, event.getY()-(TARGET_PADDING/2)*density);
					break;
				}
				
				pointsChange = true;
				return true;
			}

			if (!isDrawMode) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					savedMatrix.set(matrix);
					start.set(event.getX(), event.getY());
					Log.d(TAG, "mode=DRAG");
					mode = DRAG;
					break;
				case MotionEvent.ACTION_POINTER_2_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					oldDist = spacing(event);
					Log.d(TAG, "oldDist=" + oldDist);
					if (oldDist > 10f) {
						savedMatrix.set(matrix);
						midPoint(mid, event);
						mode = ZOOM;
						Log.d(TAG, "mode=ZOOM");
					}
					break;
				case MotionEvent.ACTION_UP:
					int xDiff = (int) Math.abs(event.getX() - start.x);
					int yDiff = (int) Math.abs(event.getY() - start.y);
					if (xDiff < 15 && yDiff < 15) {
						performClick();
					}
					pointsChange = true;
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					Log.d(TAG, "mode=NONE");
					break;
				case MotionEvent.ACTION_MOVE:
					float[] arr = new float[9]; // translate : x,y : [2],[5]
					matrix.getValues(arr);

					if (mode == DRAG) {
						matrix.set(savedMatrix);
						// float x_offset = event.getX() - start.x;
						matrix.postTranslate(event.getX() - start.x,
								event.getY() - start.y);

					} else if (mode == ZOOM) {
						float newDist = spacing(event);
						Log.d(TAG, "newDist=" + newDist);
						if (newDist > 10f) {
							matrix.set(savedMatrix);
							scale = newDist / oldDist;

							float currentScale = arr[Matrix.MSCALE_X];
							if (currentScale * scale < minScale) {
								matrix.postScale(minScale / currentScale,
										minScale / currentScale, mid.x, mid.y);
							} else if (currentScale * scale > maxScale) {
								matrix.postScale(maxScale / currentScale,
										maxScale / currentScale, mid.x, mid.y);
							} else {
								matrix.postScale(scale, scale, mid.x, mid.y);
							}

							// matrix.postScale(scale, scale, mid.x, mid.y);
						}
					}

					int screenHeight = this.getHeight();
					int screenWidth = this.getWidth();

					matrix.getValues(arr);
					if (arr[Matrix.MTRANS_X] > 0) {
						matrix.postTranslate(arr[Matrix.MTRANS_X] * (-1), 0);

					}
					if (arr[Matrix.MTRANS_Y] > 0) {
						matrix.postTranslate(0, arr[Matrix.MTRANS_Y] * (-1));

					}

					float maxTranslateX = (Math.max(
							(mBitMap.getWidth() * arr[Matrix.MSCALE_X]),
							screenWidth) - screenWidth)
							* (-1);
					float maxTranslateY = (Math.max(
							(mBitMap.getHeight() * arr[Matrix.MSCALE_Y]),
							screenHeight) - screenHeight)
							* (-1);

					if (arr[Matrix.MTRANS_X] < maxTranslateX) {
						matrix.postTranslate(
								(maxTranslateX - arr[Matrix.MTRANS_X]), 0);
					}
					if (arr[Matrix.MTRANS_Y] < maxTranslateY) {
						matrix.postTranslate(0,
								(maxTranslateY - arr[Matrix.MTRANS_Y]));

					}

					pointsChange = true;
					break;
				}

				return true;
			}

			pointsChange = true;

			if (currentPathIndex < 0)
				currentPathIndex = 0;

			if (currentPathIndex > _pointPaths.size() - 1) {
				_pointPaths.add(new PointPath());
			}

			PointPath myPath = _pointPaths.get(currentPathIndex);

			if (myPath == null) {
				myPath = new PointPath();
			}

			if (event.getAction() == MotionEvent.ACTION_UP)
				isBreakPoint = true;

			// myPath.addPoint(event.getX(), event.getY());

			// myPath.addPoint(event.getX(), event.getY(), matrix_translate.x,
			// matrix_translate.y, matrix.MSCALE_X);
			float[] arr = new float[9];
			matrix.getValues(arr);
			// myPath.addPoint(event.getX(), event.getY(), matrix_translate.x,
			// matrix_translate.y, matrix.MSCALE_X);

			myPath.addPoint(event.getX(), event.getY(), arr[Matrix.MTRANS_X],
					arr[Matrix.MTRANS_Y], arr[Matrix.MSCALE_X], isBreakPoint);
			_pointPaths.remove(currentPathIndex);
			_pointPaths.add(myPath);
			return true;
		}
	}

	/*
	 * private void TranslateROIs() {
	 * 
	 * ArrayList<PointPath> newPaths = new ArrayList<PointPath>(); // int x=220;
	 * int y = 220; for (PointPath path : _graphics) { ArrayList<Point> pts =
	 * new ArrayList<Point>(); PointPath newPath = new PointPath(); for (Point p
	 * : path.points) {
	 * 
	 * //Point tp = new Point((int)(p.x + matrix_translate.x), (int)(p.y +
	 * matrix_translate.y)); //Point tp = new Point(x, y); x++; y++;
	 * //pts.add(tp); newPath.addPoint(p.x + matrix_translate.x, p.y +
	 * matrix_translate.y); //Log.d("point", "(" + p.x + ", " + p.y + ")"); }
	 * newPaths.add(newPath); } _graphics = newPaths; pointsChange = true;
	 * this.invalidate(); }
	 */

	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
		// ...
		try {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		} catch (Exception ex) {
			return 0;
		}
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
		// ...
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	@Override
	public void onDraw(Canvas canvas) {

		canvas.drawColor(Color.BLACK);

		// mBitMap.getHeight()
		canvas.drawBitmap(mBitMap, matrix, null);
		isCleanRequest = false;

		for (PointPath path : _pointPaths) {
			Path trPath = new Path();
			// path.offset(matrix_translate.x, matrix_translate.y, trPath);
			path.transform(matrix, trPath);

			canvas.drawPath(trPath, mPaint);
		}

		if (targetDown) 
		{
			canvas.drawBitmap(target_img, targetMatrix, null);
		}
		if (isFlagMode)
		{
			for (int i=0; i<_flagPairs.size(); i++)
			{
				FlagPair fp = _flagPairs.get(i);
				//helperDrawMatrix.reset();
				helperDrawMatrix.setTranslate(fp.getStart().x, fp.getStart().y);
				canvas.drawBitmap(cross_img, helperDrawMatrix, null);
				if (fp.isPaired)
				{
					helperDrawMatrix.setTranslate(fp.getFinish().x, fp.getFinish().y);
					canvas.drawBitmap(cross_img, helperDrawMatrix, null);
					canvas.drawPath(fp.getPath(), mPaintCrosses);
				}
			}
		}

		pointsChange = false;
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
		
		mPaintCrosses = new Paint();
		mPaintCrosses.setDither(true);
		mPaintCrosses.setColor(0xFF5fca00);
		mPaintCrosses.setStyle(Paint.Style.STROKE);
		mPaintCrosses.setStrokeJoin(Paint.Join.ROUND);
		mPaintCrosses.setStrokeCap(Paint.Cap.ROUND);
		mPaintCrosses.setStrokeWidth(3);
		mPaintCrosses.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
	}

	public void UndoLastPoint() {
		if (currentPathIndex < 0)
			return;

		if (!(_pointPaths.size() > currentPathIndex)) {
			currentPathIndex--;
		}
		if (_pointPaths.size() > currentPathIndex) // (_graphics.get(currentPathIndex)
													// != null)
		{
			// int index = (_graphics.get(currentPathIndex).points.size() > 0) ?
			// currentPathIndex
			// : currentPathIndex - 1;
			boolean has_more = _pointPaths.get(currentPathIndex)
					.RemoveLastPoint();
			if (!has_more) {
				_pointPaths.remove(currentPathIndex);
				currentPathIndex--;
			}

			pointsChange = true;
		}
		/*
		 * else { currentPathIndex--; }
		 */
	}

	/** Show an event in the LogCat view, for debugging */
	/*
	 * private void dumpEvent(MotionEvent event) { String names[] = { "DOWN",
	 * "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?",
	 * "8?", "9?" }; StringBuilder sb = new StringBuilder(); int action =
	 * event.getAction(); int actionCode = action & MotionEvent.ACTION_MASK;
	 * sb.append("event ACTION_").append(names[actionCode]); if (actionCode ==
	 * MotionEvent.ACTION_POINTER_DOWN || actionCode ==
	 * MotionEvent.ACTION_POINTER_UP) { sb.append("(pid ").append( action >>
	 * MotionEvent.ACTION_POINTER_ID_SHIFT); sb.append(")"); } sb.append("[");
	 * for (int i = 0; i < event.getPointerCount(); i++) {
	 * sb.append("#").append(i);
	 * sb.append("(pid ").append(event.getPointerId(i));
	 * sb.append(")=").append((int) event.getX(i)); sb.append(",").append((int)
	 * event.getY(i)); if (i + 1 < event.getPointerCount()) sb.append(";"); }
	 * sb.append("]"); Log.d("dumpEvent", sb.toString()); }
	 */

}
