package aris.epl603;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Askisi01Activity extends Activity {
    /** Called when the activity is first created. */
	
    private static final  String TTT= "TTT";	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        

        
        final Button myButton = (Button) this.findViewById(R.id.my_button);
        myButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			openMyDialog(v);
			Log.d(TTT, "on click!!");
		}
	});    
    }
    
	public void openMyDialog(View view) {
		showDialog(10);
	}

	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case 10:
            // Create out AlterDialog
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
        return super.onCreateDialog(id);
    }
	
	
	
}