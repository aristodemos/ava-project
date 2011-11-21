package com.epl603.ava.activities;

import java.io.File;

import com.epl603.ava.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BioMedActivity extends Activity {
    
	private static final int SELECT_PICTURE = 1;

	private static String selectedImagePath;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((Button) findViewById(R.id.mloadButton))
        	.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent, 
							"Select Picture"), SELECT_PICTURE);

				}
        	
        });
        //((Button) findViewById(R.id.mEditButton))
        //	.setOnClickListener(new View.OnClickListener(){
		//		@Override
		//		public void onClick(View arg0) {
		//			Intent i = new Intent(BioMedActivity.this, EditPix.class);
		//			startActivity(i);					
		//			
		//		}
        //		
        //	});

    
    }
    public static String getSelectedImagePath(){
    	return selectedImagePath;
    }
    
    public static String getImageName(){
    	File  file = new File(selectedImagePath);
    	//return file.getName();
    	  int index = file.getName().lastIndexOf('.');
          if (index>0&& index <= file.getName().length() - 2 ) {
          return file.getName().substring(0, index);
          }  
        return "";
        }
    
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);      
                Intent i = new Intent(BioMedActivity.this, DrawActivity.class);
                startActivity(i);
            }
        }     
    }
    
    
    
	private String getPath(Uri uri) {
		String[] projection = {MediaStore.Images.Media.DATA};
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
    
    
}