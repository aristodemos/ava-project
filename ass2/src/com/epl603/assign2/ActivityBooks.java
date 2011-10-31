package com.epl603.assign2;

import com.epl603.meta.DatabaseHelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ActivityBooks extends Activity {
		
	private DatabaseHelper dbHelper;
	private ListView booksList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        dbHelper = DatabaseHelper.getDatabaseHelper(this.getApplicationContext());
        
        booksList = (ListView)findViewById(R.id.listViewBooks);
        
    }
}