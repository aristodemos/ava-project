package com.epl603.assign2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class AddBookActivity extends Activity{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addbook);
        getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
       
        ((Button) findViewById(R.id.cancelbutton2))
        	.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	             finish();
				}
			});
    }
}
