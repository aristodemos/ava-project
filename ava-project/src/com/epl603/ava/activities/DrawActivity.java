package com.epl603.ava.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.epl603.ava.R;
import com.epl603.ava.classes.PointPath;
import com.epl603.ava.views.DrawingPanel;

public class DrawActivity extends Activity {

	private DrawingPanel roi_panel;
	FrameLayout mainView;

	private final int MENU_LOAD_ROI = 0;
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
			menu.add(0, MENU_LOAD_ROI, 0, R.string.load_roi);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SAVE:
			//TODO: save points using getROIpoints method(aris)
			save_graphics(roi_panel.save_graphics());
			return true;
		case MENU_PAUSE_DRAW:
			roi_panel.exitDrawMode();
			return true;
		case MENU_LOAD_ROI:
			loadRoi();
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// when orientation changes
	  super.onConfigurationChanged(newConfig);
	  //setContentView(R.layout.main);
	}
	public void save_graphics(ArrayList<PointPath> _graphics) {
		
		//create a new file called "new.xml" in the SD card
        File newxmlfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + BioMedActivity.getImageName() +".xml");
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
                        serializer.startTag(null, "image:");
                        serializer.attribute("", "filepath", BioMedActivity.getSelectedImagePath());
                        //i indent code just to have a view similar to xml-tree
                             serializer.startTag(null, "ROIs");
							 for (PointPath myPath : _graphics) {   
								serializer.startTag(null, "roi");
								
								for (PointF p : myPath.points){
									serializer.startTag(null, "Points");
										serializer.attribute("", "x-value", ""+p.x);
										serializer.attribute("", "y-value", ""+p.y);
										serializer.endTag(null, "Points");
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
	public void loadRoi() {
		// TODO Auto-generated method stub
		
	}


}