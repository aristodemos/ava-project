package com.epl603.ava.classes;

import com.epl603.ava.views.DrawingPanel;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawingThread extends Thread {
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
				if (_panel.needsRedraw()) {
					c = _surfaceHolder.lockCanvas(null);
					synchronized (_surfaceHolder) {

						_panel.onDraw(c);
					}
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
