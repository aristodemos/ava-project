package com.epl603.assign2;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import com.epl603.classes.Publisher;
import com.epl603.meta.DatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddBookActivity extends Activity{
	
	private DatabaseHelper databaseHelper;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addbook);
        getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        
        final EditText titleText = (EditText)findViewById(R.id.titleText);
        final EditText authorText = (EditText)findViewById(R.id.authorText);
        final EditText isbnText = (EditText)findViewById(R.id.isbnText);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        
        this.databaseHelper = DatabaseHelper.getDatabaseHelper(this.getApplicationContext());
        fillSpinner();
        
        ((Button) findViewById(R.id.cancelbutton2))
        	.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	             finish();
				}
			});
        ((Button) findViewById(R.id.addbutton1))
        	.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String nameQuery=spinner.getSelectedItem().toString();
					
					int pub_id=databaseHelper.getPublisherByIdName(nameQuery);
					
					//array = (String[]) (Vector)spinner.getSelectedItem().firstElement();
					databaseHelper.insertBook(titleText.getText().toString()
							, authorText.getText().toString()
							, isbnText.getText().toString()
							, pub_id);
				}
			});
    }
    private void fillSpinner(){
    	Spinner spinner = (Spinner) findViewById(R.id.spinner);
    	   	
    	ArrayList<String> publishers = databaseHelper.getAllPublishersNames();
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, publishers);
    	spinner.setAdapter(adapter);
    	}
}
