package com.epl603.ava.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.epl603.ava.activities.BioMedActivity;
import com.epl603.ava.classes.DrawingThread;
import com.epl603.ava.classes.PointPath;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback {
	private DrawingThread _thread;
	private ArrayList<PointPath> _graphics = new ArrayList<PointPath>();

	private Bitmap mBitMap = BitmapFactory.decodeFile(BioMedActivity
			.getSelectedImagePath());

	private Paint mPaint;
	public boolean isDrawMode;
	public int currentPathIndex = 0;
	private boolean isCleanRequest = true;
	private boolean pointsChange = false;

	private Matrix matrix = new Matrix();
	//private PointF matrix_translate = new PointF(0, 0);
	//private PointF start_translate = new PointF(0, 0);
	//private float scaleFactor = 1;
	private PointF mid = new PointF();
	private float oldDist = 1f;
	//private boolean isZoom = false;
	private float scale = 1F;

	/*private int screenHeight;
	private int screenWidth;
	private int bitmapHeight;
	private int bitmapWidth;*/

	private static final String TAG = "Touch";
	// These matrices will be used to move and zoom image
	// private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	//private Bitmap image;
	// private File imageFile;

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	private PointF start = new PointF();

	// private PointF mid = new PointF();
	// private float oldDist = 1f;

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

	public void save_graphics() {
	
		//create a new file called "new.xml" in the SD card
        File newxmlfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "avatest1.xml");
        try{
                newxmlfile.createNewFile();
        }catch(IOException e){
                Log.e("IOException", "exception in createNewFile() method");
        }
        //we have to bind the new file with a FileOutputStream
        FileOutputStream fileos = null;        
        try{
                fileos = new FileOutputStream(newxmlfile);
        }catch(FileNotFoundException e){
                Log.e("FileNotFoundException", "can't create FileOutputStream");
        }
        //we create a XmlSerializer in order to write xml data
        XmlSerializer serializer = Xml.newSerializer();
        try {
                //we set the FileOutputStream as output for the serializer, using UTF-8 encoding
                        serializer.setOutput(fileos, "UTF-8");
                        //Write <?xml declaration with encoding (if encoding not null) and standalone flag (if standalone not null)
                        serializer.startDocument(null, Boolean.valueOf(true));
                        //set indentation option
                        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                        //start a tag called "image_name"
                        serializer.startTag(null, "image_name");
                        serializer.attribute("", "filepath", BioMedActivity.getSelectedImagePath());
                        //i indent code just to have a view similar to xml-tree
                             serializer.startTag(null, "ROIs");
							 for (PointPath myPath : _graphics) {   
								serializer.startTag(null, "roi");
								for (PointF p : myPath.points){
										serializer.attribute("", "x-value", ""+p.x);
										serializer.attribute("", "y-value", ""+p.y);
								}		
								serializer.endTag(null, "roi");
							}	
								serializer.endTag(null, "ROIs"); 	
                        serializer.endTag(null, "image_name");
                        serializer.endDocument();
                        //write xml data into the FileOutputStream
                        serializer.flush();
                        //finally we close the file stream
                        fileos.close();
                       
                
            } catch (Exception e) {
                        Log.e("Exception","error occurred while creating xml file");
					}
	
	}

	public void set_graphics(ArrayList<PointPath> _graphics) {
		this._graphics = _graphics;
	}

	private void initializeView() {
		getHolder().addCallback(this);
		_thread = new DrawingThread(getHolder(), this);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		((WindowManager) this.getContext().getSystemService(
				Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
				displaymetrics);
		/*screenHeight = displaymetrics.heightPixels;
		screenWidth = displaymetrics.widthPixels;

		bitmapHeight = mBitMap.getHeight();
		bitmapWidth = mBitMap.getWidth();*/

		// Display display = ((WindowManager)
		// this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		// screenHeight = display.getHeight();
		// screenWidth = display.getWidth();

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
				// dumpEvent(event);

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					savedMatrix.set(matrix);
					start.set(event.getX(), event.getY());
					Log.d(TAG, "mode=DRAG");
					mode = DRAG;
					break;
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
					/*if (mode == DRAG) {
						matrix_translate.x += event.getX() - start.x;
						matrix_translate.y += event.getY() - start.y;
					}
					else 
					{
						scaleFactor += scale; 
					}*/
					//TranslateROIs();
					pointsChange = true;
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					Log.d(TAG, "mode=NONE");
					break;
				case MotionEvent.ACTION_MOVE:
					if (mode == DRAG) {

						matrix.set(savedMatrix);
						matrix.postTranslate(event.getX() - start.x,
								event.getY() - start.y);
											
					} else if (mode == ZOOM) {
						float newDist = spacing(event);
						Log.d(TAG, "newDist=" + newDist);
						if (newDist > 10f) {
							matrix.set(savedMatrix);
							scale = newDist / oldDist;
							matrix.postScale(scale, scale, mid.x, mid.y);
						}
					}
					//TranslateROIs();
					pointsChange = true;
					break;
				}

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

			//myPath.addPoint(event.getX(), event.getY());
			
			//myPath.addPoint(event.getX(), event.getY(), matrix_translate.x, matrix_translate.y, matrix.MSCALE_X);
			float[] arr = new float[9];
			matrix.getValues(arr);
			//myPath.addPoint(event.getX(), event.getY(), matrix_translate.x, matrix_translate.y, matrix.MSCALE_X);
			myPath.addPoint(event.getX(), event.getY(), arr[2], arr[5], arr[0]);
			_graphics.remove(currentPathIndex);
			_graphics.add(myPath);
			return true;
		}
	}

	/*private void TranslateROIs() {
		
		ArrayList<PointPath> newPaths = new ArrayList<PointPath>();
	//	int x=220; int y = 220;
		for (PointPath path : _graphics) {
			ArrayList<Point> pts = new ArrayList<Point>();
			PointPath newPath = new PointPath();
			for (Point p : path.points) {
				
				//Point tp = new Point((int)(p.x + matrix_translate.x), (int)(p.y + matrix_translate.y));
				//Point tp = new Point(x, y); x++; y++;
				//pts.add(tp);
				newPath.addPoint(p.x + matrix_translate.x, p.y + matrix_translate.y);
				//Log.d("point", "(" + p.x + ", " + p.y + ")");
			}
			newPaths.add(newPath);
		}
		_graphics = newPaths;
		pointsChange = true;
		this.invalidate();
	}*/

	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
		// ...
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
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

		for (PointPath path : _graphics) {
			Path trPath = new Path();
			//path.offset(matrix_translate.x, matrix_translate.y, trPath);
			path.transform(matrix, trPath);
			
			canvas.drawPath(trPath, mPaint); 
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

	public void UndoLastPoint() {
		// TODO Auto-generated method stub
		
	}

	public void loadRoi() {
		// TODO Auto-generated method stub
		
	}

	

}
