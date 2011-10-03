package com.epl603.ava.activities;

import java.util.ArrayList;

import com.epl603.ava.R;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class StartActivity extends Activity {

	private ArrayList<Path> _graphics = new ArrayList<Path>();
	private Paint mPaint;
	
	private DrawingPanel roi_panel;
	private LinearLayout roi_options;
	
	private boolean isROIdrawMode = false;
	private Button btnOkROI;
	private Button btnUndoROI;
	FrameLayout mainView;

	private final int MENU_DRAW_ROI = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mainView = ((FrameLayout) findViewById(R.id.mainLayout));
		roi_options = ((LinearLayout)findViewById(R.id.roiOptions));
		
		roi_panel = new DrawingPanel(this);
//		roi_panel.setVisibility(View.INVISIBLE);
//		mainView.addView(roi_panel, 0,
//				new LayoutParams(LayoutParams.FILL_PARENT,
//						LayoutParams.FILL_PARENT));
		

		// initialize paint
		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(0xFFFFFF00);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(3);
		
		
		btnOkROI = (Button)findViewById(R.id.btnOk);
		btnUndoROI = (Button)findViewById(R.id.btnUndo);
		
		btnOkROI.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isROIdrawMode = false;
				//roi_panel.setVisibility(View.INVISIBLE);
				mainView.removeView(roi_panel);
				roi_panel.StopDrawThread();
				roi_options.setVisibility(View.INVISIBLE);
			}
		});
	}

	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		
		if (isROIdrawMode)
			return false;

		menu.add(0, MENU_DRAW_ROI, 0, R.string.draw_roi);
		
		return super.onPrepareOptionsMenu(menu);
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DRAW_ROI:
			enterDrawMode();
			return true;
		}
		return false;
	}

	private void enterDrawMode() {
		
		isROIdrawMode = true;
		
		mainView.addView(roi_panel, 0,
				new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));
		//roi_panel.setVisibility(View.VISIBLE);
		roi_options.setVisibility(View.VISIBLE);

	}

	class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback {
		private DrawingThread _thread;
		ArrayList<Point> points = new ArrayList<Point>();
		private Path myPath;

		public DrawingPanel(Context context) {
			super(context);
			getHolder().addCallback(this);
			_thread = new DrawingThread(getHolder(), this);
		}
		
		public void StopDrawThread()
		{
			_thread.stop();
			_thread.suspend();
		}

		public boolean onTouchEvent(MotionEvent event) {
			synchronized (_thread.getSurfaceHolder()) {

				if (points.size() == 0) {
					myPath = new Path();

					myPath.moveTo(event.getX(), event.getY());
					myPath.lineTo(event.getX(), event.getY());
					points.add(new Point((int) event.getX(), (int) event.getY()));
				}

				else {
					myPath.lineTo(event.getX(), event.getY());
					points.add(new Point((int) event.getX(), (int) event.getY()));

					_graphics.clear();
					_graphics.add(myPath);
				}

				return true;
			}
		}

		@Override
		public void onDraw(Canvas canvas) {
			for (Path path : _graphics) {
				// canvas.drawPoint(graphic.x, graphic.y, mPaint);
				canvas.drawPath(path, mPaint);
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub

		}

		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			_thread.setRunning(true);
			_thread.start();
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
	}

	class DrawingThread extends Thread {
		private SurfaceHolder _surfaceHolder;
		private DrawingPanel _panel;
		private boolean _run = false;
		private volatile boolean stop = false;

		public DrawingThread(SurfaceHolder surfaceHolder, DrawingPanel panel) {
			_surfaceHolder = surfaceHolder;
			_panel = panel;
		}

		public void setRunning(boolean run) {
			_run = run;
		}

		public SurfaceHolder getSurfaceHolder() {
			return _surfaceHolder;
		}

		@Override
		public void run() {
			if (stop) 
				return;
			
			Canvas c;
			while (_run) {
				c = null;
				try {
					c = _surfaceHolder.lockCanvas(null);
					synchronized (_surfaceHolder) {
						_panel.onDraw(c);
					}
				} finally {
					// do this in a finally so that if an exception is thrown
					// during the above, we don't leave the Surface in an
					// inconsistent state
					if (c != null) {
						_surfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}
	}
}