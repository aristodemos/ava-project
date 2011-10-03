package com.epl603.askisi1;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.epl603.askisi1.R;

public class Askisi01Activity extends Activity {
    /** Called when the activity is first created. */	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        

        
        Button myButton = (Button) this.findViewById(R.id.my_button);  
        myButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				openMyDialog();			
			}
		});      	
    }
    
	public void openMyDialog() {
		Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Our Team :)");
        builder.setMessage(R.string.onomata);
        
        builder.setCancelable(true);
        builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.cancel();
            }
        });
        
        AlertDialog dialog = builder.create();
        dialog.show();
	}	
	
}