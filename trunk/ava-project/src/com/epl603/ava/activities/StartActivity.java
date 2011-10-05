package com.epl603.ava.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.epl603.ava.R;
import com.epl603.ava.views.DrawingPanel;

public class StartActivity extends Activity {

	private DrawingPanel roi_panel;
	FrameLayout mainView;

	private final int MENU_DRAW_ROI = 0;
	private final int MENU_PAUSE_DRAW = 1;
	private final int MENU_CLOSE_ROI = 2;
	private final int MENU_UNDO = 3;
	private final int MENU_CLEAR = 3;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mainView = (FrameLayout) findViewById(R.id.mainLayout);
		roi_panel = (DrawingPanel) findViewById(R.id.roiPanel);

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();

		if (roi_panel.isDrawMode) {
			menu.add(0, MENU_PAUSE_DRAW, 0, R.string.pause_draw);
			menu.add(0, MENU_CLOSE_ROI, 0, R.string.close_ROI);
			menu.add(0, MENU_UNDO, 0, R.string.undo);
			menu.add(0, MENU_CLEAR, 0, R.string.clear);
		} else {
			menu.add(0, MENU_DRAW_ROI, 0, R.string.draw_roi);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DRAW_ROI:
			roi_panel.enterDrawMode();
			return true;
		case MENU_PAUSE_DRAW:
			roi_panel.exitDrawMode();
			return true;
		case MENU_CLOSE_ROI:
			roi_panel.CloseActivePath();
			return true;
		case MENU_CLEAR:
			roi_panel.ClearROIs();
		}
		return false;
	}

}