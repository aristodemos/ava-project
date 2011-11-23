package com.epl603.ava.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.epl603.ava.R;
import com.epl603.ava.classes.DrawStorage;
import com.epl603.ava.classes.PointPath;
import com.epl603.ava.classes.XMLParser;
import com.epl603.ava.views.DrawingPanel;


public class DrawActivity extends Activity {

	//Context _context;
	private static String xmlPath = null;
	private DrawingPanel roi_panel;
	FrameLayout mainView;

	private final int MENU_LOAD_ROI = 0;
	private final int MENU_PAUSE_DRAW = 1;
	private final int MENU_CLOSE_ROI = 2;
	private final int MENU_UNDO = 3;
	private final int MENU_CLEAR = 4;
	private final int MENU_SAVE = 5;
	private final int MENU_NEXT = 6;
	private static final int SELECT_XML = 7;
	private final int MENU_UNDO_FLAG = 8;
	private final int MENU_SHARE = 9;
	private final int MENU_CLEAR_FLAGS = 10;

	ToggleButton togglebuttonFlag;
	ToggleButton togglebuttonDraw;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw);

		mainView = (FrameLayout) findViewById(R.id.mainLayout);
		roi_panel = (DrawingPanel) findViewById(R.id.roiPanel);
		
		
		
		togglebuttonDraw = (ToggleButton) findViewById(R.id.togglebutton);
		togglebuttonDraw.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//perform actions on clicks
				if (togglebuttonDraw.isChecked()){
					roi_panel.enterDrawMode();
					togglebuttonFlag.setChecked(false);
				} else {
					roi_panel.exitDrawMode();
				}
			}
		});
		
		togglebuttonFlag = (ToggleButton) findViewById(R.id.togglebutton_flag);
		togglebuttonFlag.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				roi_panel.switchFlagMode(isChecked);
				if (isChecked)
				{
					togglebuttonDraw.setChecked(false);
					roi_panel.exitDrawMode();
				}
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		roi_panel.setPointPaths(DrawStorage.getStorage().getPaths());
		roi_panel.setFlagPairs(DrawStorage.getStorage().getPairs());
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		
		DrawStorage.getStorage().setPairs(roi_panel.getFlagPairs());
		DrawStorage.getStorage().setPaths(roi_panel.getPointPaths());
		super.onDestroy();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		
		if (roi_panel.isFlagMode)
		{
			menu.add(0, MENU_UNDO_FLAG, 0, R.string.undo_flag);
			menu.add(0, MENU_CLEAR_FLAGS, 0, R.string.clear);
		}
		else if (roi_panel.isDrawMode) {
			menu.add(0, MENU_CLOSE_ROI, 0, R.string.close_ROI);
			menu.add(0, MENU_NEXT, 0, R.string.next);
			menu.add(0, MENU_UNDO, 0, R.string.undo);
			menu.add(0, MENU_CLEAR, 0, R.string.clear);
		} else {
			menu.add(0, MENU_SAVE, 0, R.string.save_rois);
			menu.add(0, MENU_LOAD_ROI, 0, R.string.load_roi);
			menu.add(0, MENU_SHARE, 0, R.string.share);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SAVE:
			saveToXML();
			return true;
		case MENU_PAUSE_DRAW:
			roi_panel.exitDrawMode();
			return true;
		case MENU_LOAD_ROI:
			loadRoi();
		case MENU_CLOSE_ROI:
			roi_panel.CloseActivePath();
			return true;
		case MENU_NEXT:
			roi_panel.NextPath();
			return true;
		case MENU_CLEAR:
			roi_panel.ClearROIs();
			return true;
		case MENU_UNDO:
			roi_panel.UndoLastPoint();
			return true;
		case MENU_UNDO_FLAG:
			roi_panel.UndoLastFlag();
			return true;
		case MENU_SHARE:
			ShareData();
			return true;
		case MENU_CLEAR_FLAGS:
			roi_panel.clearFlags();
			return true;
		}
		return false;
	}
	
	private void ShareData()
	{
		saveToXML();
		
		Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
		share.setType("text/html");

	/*	share.putExtra(Intent.EXTRA_STREAM,
				  Uri.parse("file:///sdcard/MedImagePro/" + BioMedActivity.getImageName() + ".xml"));
		
		share.putExtra(Intent.EXTRA_STREAM,
				  Uri.parse("file:///sdcard/MedImagePro/" + BioMedActivity.getImageName() + ".BMP"));
*/
		ArrayList<Uri> uris = new ArrayList<Uri>();
		uris.add(Uri.parse("file:///sdcard/MedImagePro/" + BioMedActivity.getImageName() + ".BMP"));
		uris.add(Uri.parse("file:///sdcard/MedImagePro/" + BioMedActivity.getImageName() + ".xml"));
		share.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris);
		
		startActivityForResult(Intent.createChooser(share, "Share File"), 0);
	}
	
	//aris - method to output arraylist of ROI points to XML

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// when orientation changes
	  roi_panel.pointsChange = true;
	  roi_panel.invalidate();
	  super.onConfigurationChanged(newConfig);
	  //setContentView(R.layout.main);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) 
	    {
	        roi_panel.ChangeLastFlagColor();
	        return true;
	    }
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			DrawStorage.getStorage().clearStorage();
			return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	public void saveToXML() {
		
		ArrayList<PointPath> _graphics = roi_panel.getPointPaths();
		
		//create a new file called "new.xml" in the SD card
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MedImagePro";
		
		File folder = new File(path);
		if (!folder.exists())
		{
			folder.mkdir();
		}
        File newxmlfile = new File(path + File.separator + BioMedActivity.getImageName() +".xml");
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
                        serializer.startTag("", "image:");
                        serializer.attribute("", "filepath", BioMedActivity.getSelectedImagePath());
                        serializer.endTag("", "image:");
                        //i indent code just to have a view similar to xml-tree
                             serializer.startTag("", "ROIs");
							 for (PointPath myPath : _graphics) {   
								serializer.startTag("", "roi");
								
								for (PointF p : myPath.points){
									serializer.startTag("", "Point");
										serializer.startTag("", "x");
											serializer.attribute("", "", ""+p.x);
										serializer.endTag("", "x");
										
										serializer.startTag("", "y");
											serializer.attribute("", "", ""+p.y);
										serializer.endTag("", "y");
										
										serializer.endTag("", "Point");
								}		
								
								serializer.endTag("", "roi");
							}	
								serializer.endTag("", "ROIs"); 	
                        
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
		Intent intent = new Intent();
		intent.setType("text/plain");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, 
				"Select XML to load ROIs from"), SELECT_XML);

	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_XML) {
               xmlPath = data.getData().getPath();
               try {
				getPoints(xmlPath);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
               
            }
        }     
    }
	
	public ArrayList<PointPath> getPoints(final String path) throws
		ParserConfigurationException, SAXException, IOException
		{
			BufferedReader br = new BufferedReader(new FileReader(path));
			InputSource is = new InputSource(br);
			XMLParser parser = new XMLParser();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sp=factory.newSAXParser();
			XMLReader reader = sp.getXMLReader();
			reader.setContentHandler(parser);
			reader.parse(is);
			ArrayList<PointPath> xmlRoi = parser.roiPaths;
			return xmlRoi;
		
		}


}