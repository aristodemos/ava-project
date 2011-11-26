package com.epl603.ava.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.epl603.ava.R;
import com.epl603.ava.classes.AppConstants;
import com.epl603.ava.classes.FlagPair;
import com.epl603.ava.classes.DrawStorage;
import com.epl603.ava.classes.PointPath;
import com.epl603.ava.classes.XMLParser;
import com.epl603.ava.views.DrawingPanel;

public class DrawActivity extends Activity {

	Context _context;
	private static String xmlPath = null;
	private DrawingPanel roi_panel;
	FrameLayout mainView;

	

	ToggleButton togglebuttonFlag;
	ToggleButton togglebuttonDraw;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw);

		mainView = (FrameLayout) findViewById(R.id.mainLayout);
		roi_panel = (DrawingPanel) findViewById(R.id.roiPanel);

		String xmlFilePath = getIntent().getStringExtra(
				AppConstants.EXTRA_FILE_PATH);
		if (xmlFilePath != null) {
			LoadData(xmlFilePath);
		}

		togglebuttonDraw = (ToggleButton) findViewById(R.id.togglebutton);
		togglebuttonDraw.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// perform actions on clicks
				if (togglebuttonDraw.isChecked()) {
					roi_panel.enterDrawMode();
					togglebuttonFlag.setChecked(false);
				} else {
					roi_panel.exitDrawMode();
				}
			}
		});

		togglebuttonFlag = (ToggleButton) findViewById(R.id.togglebutton_flag);
		togglebuttonFlag
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						roi_panel.switchFlagMode(isChecked);
						if (isChecked) {
							togglebuttonDraw.setChecked(false);
							roi_panel.exitDrawMode();
						}
					}
				});

	}

	@Override
	protected void onResume() {
		ArrayList<PointPath> paths = DrawStorage.getStorage().getPaths();
		roi_panel.setPointPaths(paths);
		roi_panel.setFlagPairs(DrawStorage.getStorage().getPairs());
		roi_panel.setFlagPairsDensity();
		roi_panel.setCurrentPathIndex(paths.size());
		roi_panel.pointsChange = true;
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

		if (roi_panel.isFlagMode) {
			menu.add(0, AppConstants.MENU_UNDO_FLAG, 0, R.string.undo_flag);
			menu.add(0, AppConstants.MENU_CLEAR_FLAGS, 0, R.string.clear);
		} else if (roi_panel.isDrawMode) {
			menu.add(0, AppConstants.MENU_CLOSE_ROI, 0, R.string.close_ROI);
			menu.add(0, AppConstants.MENU_NEXT, 0, R.string.next);
			menu.add(0, AppConstants.MENU_UNDO, 0, R.string.undo);
			menu.add(0, AppConstants.MENU_CLEAR, 0, R.string.clear);
		} else {
			menu.add(0, AppConstants.MENU_SAVE, 0, R.string.save_rois);
			menu.add(0, AppConstants.MENU_LOAD_ROI, 0, R.string.load_roi);
			menu.add(0, AppConstants.MENU_SHARE, 0, R.string.share);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case AppConstants.MENU_SAVE:
			saveToXML();
			return true;
		case AppConstants.MENU_PAUSE_DRAW:
			roi_panel.exitDrawMode();
			return true;
		case AppConstants.MENU_LOAD_ROI:
			loadRoi();
		case AppConstants.MENU_CLOSE_ROI:
			roi_panel.CloseActivePath();
			return true;
		case AppConstants.MENU_NEXT:
			roi_panel.NextPath();
			return true;
		case AppConstants.MENU_CLEAR:
			roi_panel.ClearROIs();
			return true;
		case AppConstants.MENU_UNDO:
			roi_panel.UndoLastPoint();
			return true;
		case AppConstants.MENU_UNDO_FLAG:
			roi_panel.UndoLastFlag();
			return true;
		case AppConstants.MENU_SHARE:
			ShareData();
			return true;
		case AppConstants.MENU_CLEAR_FLAGS:
			roi_panel.clearFlags();
			return true;
		}
		return false;
	}

	private void ShareData() {
		String filePath = saveToXML();

		Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
		share.setType("text/html");

		/*
		 * share.putExtra(Intent.EXTRA_STREAM,
		 * Uri.parse("file:///sdcard/MedImagePro/" +
		 * BioMedActivity.getImageName() + ".xml"));
		 * 
		 * share.putExtra(Intent.EXTRA_STREAM,
		 * Uri.parse("file:///sdcard/MedImagePro/" +
		 * BioMedActivity.getImageName() + ".BMP"));
		 */
		ArrayList<Uri> uris = new ArrayList<Uri>();
		uris.add(Uri.parse("file:///" + BioMedActivity.getSelectedImagePath())); 
				
				/*sdcard/MedImagePro/"
				+ BioMedActivity.getImageName() + ".BMP"));*/
		uris.add(Uri.parse("file:///" + filePath));
				
				/*sdcard/MedImagePro/"
				+ BioMedActivity.getImageName() + ".xml"));*/
		share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
		share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
		share.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));

		startActivityForResult(Intent.createChooser(share, getString(R.string.share_file)), 0);
	}

	// aris - method to output arraylist of ROI points to XML

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// when orientation changes
		roi_panel.pointsChange = true;
		roi_panel.invalidate();
		super.onConfigurationChanged(newConfig);
		// setContentView(R.layout.main);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP
				|| keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			roi_panel.ChangeLastFlagColor();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ShowConfirmMessage();
			//DrawStorage.getStorage().clearStorage();
			return false; //super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void ShowConfirmMessage() {
		new AlertDialog.Builder(this)
		.setTitle(getString(R.string.attention))
		.setMessage(getString(R.string.save))
		.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface v, int arg1) {
				saveToXML();
				DrawStorage.getStorage().clearStorage();
				finish();
			}

		})
		.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface v, int arg1) {
				DrawStorage.getStorage().clearStorage();
				finish();
				
			}
		}).setCancelable(true).show(); 
	}
	
	private void LoadData(String xmlPath) {
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

	// returns filename
	public String saveToXML() {

		String dateStr = getDateString();

		ArrayList<PointPath> _graphics = roi_panel.getPointPaths();
		ArrayList<FlagPair> _flagPairs = roi_panel.getFlagPairs();
		// create a new file called "new.xml" in the SD card
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "MedImagePro";

		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}

		String fileName = path + File.separator + BioMedActivity.getImageName()
				+ "_" + dateStr + ".xml";

		File newxmlfile = new File(fileName);
		try {
			newxmlfile.createNewFile();
		} catch (IOException e) {
			Log.e("IOException", "exception in createNewFile() method");
		}
		// we have to bind the new file with a FileOutputStream
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(newxmlfile);
		} catch (FileNotFoundException e) {
			Log.e("FileNotFoundException", "can't create FileOutputStream");
		}
		// we create a XmlSerializer in order to write xml data
		XmlSerializer serializer = Xml.newSerializer();
		try {
			// we set the FileOutputStream as output for the serializer, using
			// UTF-8 encoding
			serializer.setOutput(fileos, "UTF-8");
			// Write <?xml declaration with encoding (if encoding not null) and
			// standalone flag (if standalone not null)
			serializer.startDocument(null, Boolean.valueOf(true));
			// set indentation option
			serializer.setFeature(
					"http://xmlpull.org/v1/doc/features.html#indent-output",
					true);
			// start a tag called "image_name"
			serializer.startTag("", AppConstants.MED_IMG);
			serializer.startTag("", AppConstants.ROIS);
			for (PointPath myPath : _graphics) {
				serializer.startTag("", "roi");

				for (PointF p : myPath.points) {
					serializer.startTag("", AppConstants.POINT);
					
					serializer.attribute("", AppConstants.POINT_X, Float.toString(p.x));
					
					serializer.attribute("", AppConstants.POINT_Y, Float.toString(p.y));
					

					serializer.endTag("", AppConstants.POINT);
				}
				serializer.endTag("", AppConstants.ROI);

				serializer.startTag("", AppConstants.IS_CLOSED);
				serializer.attribute("", AppConstants.IS_CLOSED,
						Integer.toString(myPath.isClosed));
				serializer.endTag("", AppConstants.IS_CLOSED);

			}
			serializer.endTag("", AppConstants.ROIS);
			serializer.startTag("", AppConstants.FLAG_PAIRS);
			for (FlagPair myFlagPairs : _flagPairs) {
				serializer.startTag("", AppConstants.PAIR);
				serializer.startTag("", AppConstants.START);
				serializer.attribute("", AppConstants.START_X,
						Float.toString(myFlagPairs.getStart().x));
				serializer.attribute("", AppConstants.START_Y,
						Float.toString(myFlagPairs.getStart().y));
				serializer.endTag("", AppConstants.START);
				serializer.startTag("", AppConstants.COLOR);
				serializer.attribute("", AppConstants.COLOR,
						Integer.toString(myFlagPairs.getColor()));
				serializer.endTag("", AppConstants.COLOR);
				serializer.startTag("", AppConstants.DISTANCE);
				serializer.attribute("", AppConstants.DISTANCE,
						Double.toString(myFlagPairs.getDistanceInPixels()));
				serializer.endTag("", AppConstants.DISTANCE);
				serializer.startTag("", AppConstants.FINISH);
				serializer.attribute("", AppConstants.FINISH_X,
						Float.toString(myFlagPairs.getFinish().x));
				serializer.attribute("", AppConstants.FINISH_Y,
						Float.toString(myFlagPairs.getFinish().y));
				serializer.endTag("", AppConstants.FINISH);
				serializer.endTag("", AppConstants.PAIR);
			}
			serializer.endTag("", AppConstants.FLAG_PAIRS);
			serializer.endTag("", AppConstants.MED_IMG);
			serializer.endDocument();
			// write xml data into the FileOutputStream
			serializer.flush();
			// finally we close the file stream
			fileos.close();

		} catch (Exception e) {
			Log.e("Exception", "error occurred while creating xml file");
			return "";
		}

		Toast.makeText(this, fileName + " saved.", Toast.LENGTH_LONG).show(); 
		
		return fileName;
	}

	private String getDateString() {
		Date d = new Date();
		CharSequence s = DateFormat.format("dd-MM-yy_hhmmss", d.getTime());
		return s.toString();
	}

	public void loadRoi() {
		Intent i = new Intent(DrawActivity.this, SelectFileActivity.class);
		i.putExtra(AppConstants.EXTRA_FILTER, BioMedActivity.getImageName());
		i.putExtra(AppConstants.EXTRA_FOLDERS, AppConstants.DOWNLOADS_FOLDER
				+ ";" + AppConstants.APP_FOLDER);
		i.putExtra(AppConstants.EXTRA_PATTERN, ".xml");
		startActivityForResult(i, AppConstants.SELECT_FILE);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == AppConstants.SELECT_XML) {
				xmlPath = data.getData().getPath();
				LoadData(xmlPath);
			}
		} else if (resultCode == AppConstants.SELECT_FILE_OK) {
			String xmlFilePath = data
					.getStringExtra(AppConstants.EXTRA_FILE_PATH);
			if (xmlFilePath != null) {
				LoadData(xmlFilePath);
			}
		}
	}

	public void getPoints(final String path)
			throws ParserConfigurationException, SAXException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		InputSource is = new InputSource(br);
		XMLParser parser = new XMLParser();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser sp = factory.newSAXParser();
		XMLReader reader = sp.getXMLReader();
		reader.setContentHandler(parser);
		reader.parse(is);
		ArrayList<PointPath> xmlRoi = parser.roiPaths;
		ArrayList<FlagPair> xmlFlagPair = parser.flags;
		DrawStorage.getStorage().setPaths(xmlRoi);
		DrawStorage.getStorage().setPairs(xmlFlagPair);
	}

}