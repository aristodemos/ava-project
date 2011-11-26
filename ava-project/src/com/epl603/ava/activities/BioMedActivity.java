package com.epl603.ava.activities;

import java.io.File;

import com.epl603.ava.R;
import com.epl603.ava.classes.AppConstants;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BioMedActivity extends Activity {

	private static String selectedImagePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		((Button) findViewById(R.id.mloadButton))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_PICK);
						startActivityForResult(
								Intent.createChooser(intent, getString(R.string.select_picture)),
								AppConstants.SELECT_PICTURE);

					}

				});

		((Button) findViewById(R.id.mExitButton))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		
		((Button) findViewById(R.id.mSettingsButton))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startSettingsActivity();
			}
		});
		
		((Button) findViewById(R.id.mEditButton))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//startLoadActivity();
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_PICK);
				startActivityForResult(
						Intent.createChooser(intent, getString(R.string.select_picture)),
						AppConstants.SELECT_PICTURE_DIRECT_LOAD);
			}

		});		
		
		// ((Button) findViewById(R.id.mEditButton))
		// .setOnClickListener(new View.OnClickListener(){
		// @Override
		// public void onClick(View arg0) {
		// Intent i = new Intent(BioMedActivity.this, EditPix.class);
		// startActivity(i);
		//
		// }
		//
		// });

	}

	public void startSettingsActivity()
	{
		startActivity(new Intent(this, SettingsActivity.class));
		//ColorPickerDialog dlg = new ColorPickerDialog(this);
		//dlg.show();
	}
	
	/*private void startLoadActivity() {
		
		startActivityForResult(new Intent(this, SelectFileActivity.class), 123);
	}*/
	
	public static String getSelectedImagePath() {
		return selectedImagePath;
	}

	public static String getImageName() {
		File file = new File(selectedImagePath);
		// return file.getName();
		int index = file.getName().lastIndexOf('.');
		if (index > 0 && index <= file.getName().length() - 2) {
			return file.getName().substring(0, index);
		}
		return "";
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == AppConstants.SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				Intent i = new Intent(BioMedActivity.this, DrawActivity.class);
				startActivity(i);
			}
			else if (requestCode == AppConstants.SELECT_PICTURE_DIRECT_LOAD)
			{
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				Intent i = new Intent(BioMedActivity.this, SelectFileActivity.class);
				i.putExtra(AppConstants.EXTRA_FILTER, getImageName());
				i.putExtra(AppConstants.EXTRA_FOLDERS, AppConstants.DOWNLOADS_FOLDER + ";" + AppConstants.APP_FOLDER);
				i.putExtra(AppConstants.EXTRA_PATTERN, ".xml");
				startActivityForResult(i, AppConstants.SELECT_FILE);
			}
		}
		else if (resultCode == AppConstants.SELECT_FILE_OK)
		{
			String xmlPath = data.getStringExtra(AppConstants.EXTRA_FILE_PATH);
			Intent i = new Intent(BioMedActivity.this, DrawActivity.class);
			i.putExtra(AppConstants.EXTRA_FILE_PATH, xmlPath);
			startActivity(i);
		}
	}

	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

}