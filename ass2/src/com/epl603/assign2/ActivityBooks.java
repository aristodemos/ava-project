package com.epl603.assign2;

import java.util.Vector;

import com.epl603.classes.Book;
import com.epl603.meta.DatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Button;
import java.lang.Long;

public class ActivityBooks extends Activity {
		
	private static final String TAG = "ZXing exception";
	private DatabaseHelper dbHelper;
	private ListView booksList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((Button) findViewById(R.id.btnAdd))
        	.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	                Intent myIntent = new Intent(v.getContext(), AddBookActivity.class);
	                startActivityForResult(myIntent, 0);					
				}
			});
        
        this.dbHelper = DatabaseHelper.getDatabaseHelper(this.getApplicationContext());
		((Button) findViewById(R.id.btnScan))
        	.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	                intent.setPackage("com.google.zxing.client.android");
	                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	                	
	                try {
	                	startActivityForResult(intent, 1);
	                   
	                  } catch (ActivityNotFoundException e) {
	                    showDownloadDialog();
	                  }
				}
			});
        
        booksList = (ListView)findViewById(R.id.listViewBooks);
        refresh();
        
    }

    public void refresh()
    {
    	final Vector<Book> books = dbHelper.getAllBooks();
    	MyListViewAdapter listViewAdapter = new MyListViewAdapter(this, books);
    	booksList.setAdapter(listViewAdapter);
    	booksList.invalidate();
    }
    
	@Override
	protected void onDestroy() {
		dbHelper.cleanup();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }
    private  AlertDialog showDownloadDialog() {
    	AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);
    	downloadDialog.setTitle("Install Barcode Scanner?");
    	downloadDialog.setMessage("This application requires Barcode Scanner. Would you like to install it?");
    	downloadDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialogInterface, int i) {
    			Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
    			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    			try {
    				startActivity(intent);
    			} catch (ActivityNotFoundException anfe) {
// 	Hmm, market is not installed
    				Log.w(TAG, "Android Market is not installed; cannot install Barcode Scanner");
    			}
    		}
    	});
    	downloadDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialogInterface, int i) {}
    	});
    	return downloadDialog.show();
    }
}