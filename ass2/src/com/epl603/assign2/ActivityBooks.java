package com.epl603.assign2;

import java.util.Vector;

import com.epl603.classes.Book;
import com.epl603.meta.DatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Button;

public class ActivityBooks extends Activity {

	private static final String TAG = "ZXing exception";
	private DatabaseHelper dbHelper;
	private ListView booksList;
	private Vector<Book> books;

	private static final int ADDRESULT = 2;
	private static final int BARCODERESULT = 5;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		registerForContextMenu(this.findViewById(R.id.listViewBooks));

		((Button) findViewById(R.id.btnAdd))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent myIntent = new Intent(v.getContext(),
								AddBookActivity.class);
						startActivityForResult(myIntent, ADDRESULT);
					}
				});

		this.dbHelper = DatabaseHelper.getDatabaseHelper(this
				.getApplicationContext());

		((Button) findViewById(R.id.btnScan))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								"com.google.zxing.client.android.SCAN");
						intent.setPackage("com.google.zxing.client.android");
						intent.putExtra("SCAN_MODE", "PRODUCT_MODE");

						try {
							startActivityForResult(intent, BARCODERESULT);

						} catch (ActivityNotFoundException e) {
							showDownloadDialog();
						}
					}
				});

		booksList = (ListView) findViewById(R.id.listViewBooks);
		
		refresh();

	}
	
	private void ShowDeleteMessage(final Book book) {
		new AlertDialog.Builder(this)
		.setTitle("Delete entry?")
		.setMessage(book.getTitle())
		.setPositiveButton("Delete", new OnClickListener() {
		    public void onClick(DialogInterface arg0, int arg1) {
		    	dbHelper.deleteBookByISBN(book.getISBN());
				refresh();
		    }
		})
		.setNegativeButton("Cancel", new OnClickListener() {
		    public void onClick(DialogInterface arg0, int arg1) {
		        // Some stuff to do when cancel got clicked
		    }
		})
		.show();
	}

	public void refresh() {
		books = dbHelper.getAllBooks();
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		
		 AdapterView.AdapterContextMenuInfo info; 
	        try { 
	             info = (AdapterView.AdapterContextMenuInfo) menuInfo; 
	        } catch (ClassCastException e) { 
	            return; 
	        }  
		ShowDeleteMessage(books.get(info.position));
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == BARCODERESULT) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				//String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Handle successful scan
				Intent myIntent = new Intent();

				myIntent.setAction("epl603.assignment2.ACTION_BOOK_LOOKUP");
				myIntent.putExtra("epl603.assignment2.EXTRA_BOOK_ISBN",
						contents);
				sendBroadcast(myIntent);

			} else 
			{ 
				// Handle cancel
			}
		}
	}

	private AlertDialog showDownloadDialog() {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);
		downloadDialog.setTitle("Install Barcode Scanner?");
		downloadDialog
				.setMessage("This application requires Barcode Scanner. Would you like to install it?");
		downloadDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						Uri uri = Uri.parse("market://search?q=pname:"
								+ "com.google.zxing.client.android");
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						try {
							startActivity(intent);
						} catch (ActivityNotFoundException anfe) {
							// Hmm, market is not installed
							Log.w(TAG,
									"Android Market is not installed; cannot install Barcode Scanner");
						}
					}
				});
		downloadDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
					}
				});
		return downloadDialog.show();
	}
}