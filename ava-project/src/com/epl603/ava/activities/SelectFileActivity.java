package com.epl603.ava.activities;

import java.io.File;
import java.util.ArrayList;

import com.epl603.ava.R;
import com.epl603.ava.classes.AppConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SelectFileActivity extends Activity {

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

		filename = getIntent().getStringExtra(AppConstants.EXTRA_FILTER);
		folders = getIntent().getStringExtra(AppConstants.EXTRA_FOLDERS);
		pattern = getIntent().getStringExtra(AppConstants.EXTRA_PATTERN);

		fileListView = (ListView) findViewById(R.id.fileList);

		/*
		 * TextView emptyView = new TextView(this);
		 * emptyView.setText(getString(R.string.no_items));
		 * fileListView.setEmptyView(emptyView);
		 */

		TextView emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		emptyView.setText(R.string.no_items);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) fileListView.getParent()).addView(emptyView);
		fileListView.setEmptyView(emptyView);

		fileListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View v,
							int position, long id) {

						Intent i = new Intent();
						i.putExtra(AppConstants.EXTRA_FILE_PATH,
								listPaths.get(position));
						setResult(AppConstants.SELECT_FILE_OK, i);
						finish();
					}
				});
		
		registerForContextMenu(this.findViewById(R.id.fileList));

		LoadFiles();

		// fileListView.setAdapter(new
		// ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,
		// lv_arr));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		
		 AdapterView.AdapterContextMenuInfo info; 
	        try { 
	             info = (AdapterView.AdapterContextMenuInfo) menuInfo; 
	        } catch (ClassCastException e) { 
	            return; 
	        }  
		ShowDeleteMessage(info.position);
	}
	
	private void ShowDeleteMessage(int index) {
		
		final String filename = listContents.get(index);
		final String path = listPaths.get(index);
		
		new AlertDialog.Builder(this)
		.setTitle("Delete entry?")
		.setMessage(filename)
		.setPositiveButton("Delete", new OnClickListener() {
		    public void onClick(DialogInterface arg0, int arg1) {
		    	File file = new File(path);
		    	file.delete();
		    	LoadFiles();
		    }
		})
		.setNegativeButton("Cancel", new OnClickListener() {
		    public void onClick(DialogInterface arg0, int arg1) {
		        // Some stuff to do when cancel got clicked
		    }
		})
		.show();
	}
	
	private void LoadFiles() {

		listPaths.clear();
		listContents.clear();
		
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
