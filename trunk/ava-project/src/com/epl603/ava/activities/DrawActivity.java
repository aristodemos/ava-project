package com.epl603.ava.activities;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.epl603.ava.R;
import com.epl603.ava.views.DrawingPanel;

public class DrawActivity extends Activity {

	private DrawingPanel roi_panel;
	FrameLayout mainView;

	private final int MENU_DRAW_ROI = 0;
	private final int MENU_PAUSE_DRAW = 1;
	private final int MENU_CLOSE_ROI = 2;
	private final int MENU_UNDO = 3;
	private final int MENU_CLEAR = 4;
	private final int MENU_SAVE = 5;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw);

		mainView = (FrameLayout) findViewById(R.id.mainLayout);
		roi_panel = (DrawingPanel) findViewById(R.id.roiPanel);
		
		final ToggleButton togglebutton = (ToggleButton) findViewById(R.id.togglebutton);
		togglebutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//perform actions on clicks
				if (togglebutton.isChecked()){
					roi_panel.enterDrawMode();
				} else {
					roi_panel.exitDrawMode();
				}
				
			}
		});

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();

		if (roi_panel.isDrawMode) {
			menu.add(0, MENU_CLOSE_ROI, 0, R.string.close_ROI);
			menu.add(0, MENU_UNDO, 0, R.string.undo);
			menu.add(0, MENU_CLEAR, 0, R.string.clear);
		} else {
			menu.add(0, MENU_SAVE, 0, R.string.save_rois);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SAVE:
			//TODO: save points using getROIpoints method(aris)
			saveROI2XMLfile(roi_panel.get_graphics());
			return true;
		case MENU_PAUSE_DRAW:
			roi_panel.exitDrawMode();
			return true;
		case MENU_CLOSE_ROI:
			roi_panel.CloseActivePath();
			//roi_panel.exitDrawMode();	
			//togglebutton.toggle();
			return true;
		case MENU_CLEAR:
			roi_panel.ClearROIs();
			return true;
		case MENU_UNDO:
			roi_panel.UndoLastPoint();
			return true;
		}
		return false;
	}
	
	//aris - method to output arraylist of ROI points to XML
	private void saveROI2XMLfile(String path){
		 String outFile =
                 Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ava1test.xml";
		
		XmlSerializer serializer = Xml.newSerializer();
	    
	    try{
	    	FileWriter writer = new FileWriter(outFile);
	    	serializer.setOutput(writer);
	    	serializer.startDocument("UTF-8", true);
	    	serializer.startTag("pathofpoints", "path");
	    	serializer.attribute("Regio_n1", "ROIi", path);
	    	serializer.endTag("pathofpoints", "path");
	    	serializer.endDocument();
	    	
	    	writer.write(writer.toString());	    	
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// when orientation changes
	  super.onConfigurationChanged(newConfig);
	  //setContentView(R.layout.main);
	}

}