package com.epl603.ava.activities;

import com.epl603.ava.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epl603.ava.classes.*;
import com.epl603.ava.views.ColorPickerDialog;

public class SettingsActivity extends Activity {

	public class ColorChangedListener implements
			ColorPickerDialog.OnColorChangedListener {
		public ColorChangedListener() {
		}

		@Override
		public void colorChanged(int color) {
			SetColor(color);
		}
	}

	int mColor;
	Button btnSave;
	Button btnCancel;
	Button btnColor;
	EditText edtPPM;
	ColorPickerDialog colorPicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		btnSave = (Button) findViewById(R.id.btnSaveSet);
		btnCancel = (Button) findViewById(R.id.btnCancelSet);
		btnColor = (Button) findViewById(R.id.colorChooser);
		edtPPM = (EditText) findViewById(R.id.edtPPM);

		btnColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorPicker.show();
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SaveSettings();
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Close();
			}
		});

		edtPPM.setText(Float.toString(DrawSettings.getPPM(this)));
		int color = DrawSettings.getDrawColor(this);
		colorPicker = new ColorPickerDialog(this, new ColorChangedListener(),
				color);
		SetColor(color);
	}

	private void SetColor(int color) {
		btnColor.setBackgroundColor(color);
		mColor = color;
	}

	private void Close() {
		finish();
	}

	private void SaveSettings() {
		DrawSettings.saveDrawColor(this, mColor);
		try {
			float ppm = Float.valueOf(edtPPM.getText().toString());
			if (ppm > 0)
				DrawSettings.savePPM(this, ppm);
			else {
				throw new Exception();
			}
			finish();
			Toast.makeText(this, "Settings saved.", Toast.LENGTH_LONG).show(); 
			
		} catch (Exception ex) {
			CreateAlert("Pixel per milimeter should be more than 0.");
		}		
	}

	private void CreateAlert(String msg) {
		new AlertDialog.Builder(this).setTitle("Warning").setMessage(msg)
				.setNeutralButton("Ok", null).show();
	}

}
