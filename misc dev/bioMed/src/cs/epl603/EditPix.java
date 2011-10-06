package cs.epl603;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class EditPix extends Activity {

	Canvas myCanvas;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.editview);

		myView nView = new myView(this);

		//nView.getSettings().setBuiltInZoomControls(true);
		//nView.loadUrl("file://" + BioMedActivity
			//		.getSelectedImagePath());
		
		setContentView(nView);

		// ImageView imgView = new ImageView(this);
		// Bitmap mBitMap
		// =BitmapFactory.decodeFile(BioMedActivity.getSelectedImagePath());

		// imgView.setImageBitmap(mBitMap);
		// Uri
		// imgView.setimage
	}

	private class myView extends WebView {

		public myView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		protected void onDraw(Canvas canvas) {
			Bitmap mBitMap = BitmapFactory.decodeFile(BioMedActivity
					.getSelectedImagePath());
			canvas.drawBitmap(mBitMap, 0, 0, null);
		}
	}

}
