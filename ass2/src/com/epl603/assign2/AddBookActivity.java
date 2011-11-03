package com.epl603.assign2;

import java.util.ArrayList;
import java.util.Vector;

import com.epl603.classes.Publisher;
import com.epl603.meta.DatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddBookActivity extends Activity {

	private DatabaseHelper databaseHelper;
	private Vector<Publisher> publishers;

	Spinner spinner;
	EditText isbnText;
	EditText authorText;
	EditText titleText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addbook);
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		titleText = (EditText) findViewById(R.id.titleText);
		authorText = (EditText) findViewById(R.id.authorText);
		isbnText = (EditText) findViewById(R.id.isbnText);
		spinner = (Spinner) findViewById(R.id.spinner);

		this.databaseHelper = DatabaseHelper.getDatabaseHelper(this
				.getApplicationContext());
		fillSpinner();

		String publisher = getIntent().getStringExtra("publisher");
		String authors = getIntent().getStringExtra("authors");
		String title = getIntent().getStringExtra("title");
		String isbn = getIntent().getStringExtra("ISBN");

		boolean publisherFound = false;
		if (publisher != null && publisher.length() > 0) {
			publisherFound = selectSpinnerValue(publisher);

			if (!publisherFound) {
				databaseHelper.insertPublisher(publisher, "");
				fillSpinner();
				selectSpinnerValue(publisher);
			}
		}

		titleText.setText(title);
		authorText.setText(authors);
		isbnText.setText(isbn);

		((Button) findViewById(R.id.cancelbutton2))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((Button) findViewById(R.id.addbutton1))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						if (spinner.getSelectedItemPosition() == -1
								|| titleText.getText().length() == 0
								|| isbnText.getText().length() == 0
								|| authorText.getText().length() == 0) {
							ShowAlertMessage("Warning",
									"All fields are mandatory.");
							return;
						}

						int pub_id = publishers.get(
								spinner.getSelectedItemPosition()).getId();

						long id = databaseHelper.insertBook(titleText.getText()
								.toString(), authorText.getText().toString(),
								isbnText.getText().toString(), pub_id);

						if (id == -1) {
							ShowAlertMessage("Warning", "Book allready exists.");
						} else
							finish();
					}
				});
	}

	private void ShowAlertMessage(String title, String msg) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
				.setPositiveButton("OK", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	private boolean selectSpinnerValue(String publisherName) // returns false if
																// publisher is
																// not in the
																// list
	{
		for (int i = 0; i < publishers.size(); i++) {
			if (publisherName.equals(publishers.get(i).getName())) {
				spinner.setSelection(i);
				return true;
			}
		}
		return false;
	}

	private void fillSpinner() {
		publishers = databaseHelper.getAllPublishers();

		ArrayList<String> publisherNames = new ArrayList<String>(); // =
																	// databaseHelper.getAllPublishersNames();
		for (int i = 0; i < publishers.size(); i++) {
			publisherNames.add(publishers.get(i).getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, publisherNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(adapter);
	}
}
