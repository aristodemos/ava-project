package com.epl603.ava.activities;

import java.io.File;
import java.util.ArrayList;

import com.epl603.ava.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectFileActivity extends Activity {

	private static final String EXTRA_FILTER = "extra_filter";
	private static final String EXTRA_FOLDERS = "folders";
	private static final String EXTRA_PATTERN = "pattern";
	private static final String EXTRA_FILE_PATH = "path";
	private static final int SELECT_FILE_OK = 10;

	private ListView fileListView;

	private String folders;
	private String filename;
	private String pattern;

	ArrayList<String> listContents = new ArrayList<String>();
	ArrayList<String> listPaths = new ArrayList<String>();

	// private String
	// lv_arr[]={"Android","iPhone","BlackBerry","AndroidPeople"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.file_list);
		super.onCreate(savedInstanceState);

		filename = getIntent().getStringExtra(EXTRA_FILTER);
		folders = getIntent().getStringExtra(EXTRA_FOLDERS);
		pattern = getIntent().getStringExtra(EXTRA_PATTERN);

		fileListView = (ListView) findViewById(R.id.fileList);

		fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
					
				Intent i = new Intent();
				i.putExtra(EXTRA_FILE_PATH, listPaths.get(position));
				setResult(SELECT_FILE_OK, i);
				finish();
			}
		});
		
		LoadFiles();

		// fileListView.setAdapter(new
		// ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,
		// lv_arr));
	}

	private void LoadFiles() {

		String[] foldersArr = folders.split(";");
		for (int i = 0; i < foldersArr.length; i++) {
			String f = foldersArr[i].trim();
			if (f.length() > 0) {
				File dir = new File(f);
				File[] fileList = dir.listFiles();
				if (fileList == null)
					continue;

				for (int k = 0; k < fileList.length; k++) {
					if (fileList[k].isFile()
							&& fileList[k].getName().contains(filename)
							&& fileList[k].getName().contains(pattern)) {
						listContents.add(fileList[k].getName());
						listPaths.add(fileList[k].getPath());
					}
				}
			}
		}

		fileListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listContents));
	}

}
