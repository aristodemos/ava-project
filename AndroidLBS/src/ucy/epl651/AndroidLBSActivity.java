package ucy.epl651;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import ucy.epl651.GeoCodeResult;
import ucy.epl651.GeoCoder;

public class AndroidLBSActivity extends Activity {
    
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; //in meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; //in milliseconds
	
	//private GeoCoder geoCoder = new GeoCoder();
	private MyGeoCoder mygeoCoder = new MyGeoCoder();
	
	protected LocationManager locationManager;
	protected Location currentLocation;
	
	protected Button retrieveLocationButton;
	protected Button reverseGeocodingButton;


	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location);
        reverseGeocodingButton = (Button) findViewById(R.id.reverse_geocoding);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locationManager.requestLocationUpdates(
        		LocationManager.GPS_PROVIDER,
        		MINIMUM_TIME_BETWEEN_UPDATES,
        		MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
        		new MyLocationListener()
        		);
        retrieveLocationButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showCurrentLocation();
			}
		});     
        reverseGeocodingButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				performReverseGeocodingInBackground();
			}
		});
    }
    
    private void performReverseGeocodingInBackground() {
		// TODO Auto-generated method stub
		showCurrentLocation();
		new ReverseGeocodeLookupTask().execute((Void[])null);
	}
    
    protected void showCurrentLocation(){
    	currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	
    	if (currentLocation!=null){
    		String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s  ", 
    				currentLocation.getLongitude(), currentLocation.getLatitude());
    		Toast.makeText(AndroidLBSActivity.this, message, 
    				Toast.LENGTH_LONG).show();
    	}
    }
    
    private class MyLocationListener implements LocationListener{
    	
    	public void onLocationChanged(Location location) {
    		String message = String.format(
    				"New Location \n Longitude: %1$s \n Latitude: %2$s",
    				location.getLongitude(), location.getLatitude());
    		Toast.makeText(AndroidLBSActivity.this, message, 
    				Toast.LENGTH_LONG).show();
    	}
    	public void onStatusChanged(String s, int i, Bundle b){
    		Toast.makeText(AndroidLBSActivity.this, "Provider status changed", Toast.LENGTH_LONG).show();
    	}
    	public void onProviderDisabled(String s) {
    		Toast.makeText(AndroidLBSActivity.this,
    		      "Provider disabled by the user. GPS turned off",
    		       Toast.LENGTH_LONG).show();
    	}
    	public void onProviderEnabled(String s) {
    	    Toast.makeText(AndroidLBSActivity.this,
    	          "Provider enabled by the user. GPS turned on",
    	           Toast.LENGTH_LONG).show();
        }

    }
    public class ReverseGeocodeLookupTask extends AsyncTask <Void, Void, GeoCodeResult>{
    	private ProgressDialog progressDialog;
    	
    	protected void onPreExecute(){
    		this.progressDialog = ProgressDialog.show(
    				AndroidLBSActivity.this,
    				"Please Wait... connecting with Yahoo!!!", 
    				"Requesting Reverse Geocode LookUp",
    				true);
    	}
    	protected GeoCodeResult doInBackground(Void... params) {
    		
				return mygeoCoder.reverseGeoCode(currentLocation.getLatitude(), currentLocation.getLongitude());
			
    	}
    	protected void onPostExecute(GeoCodeResult result) {
    		this.progressDialog.cancel();
    		if (result!=null){
    		Toast.makeText(AndroidLBSActivity.this, result.toString(), 
    				Toast.LENGTH_LONG).show();
    		}
    		else {
    			Toast.makeText(AndroidLBSActivity.this, "Cannot Find address!", 
        				Toast.LENGTH_LONG).show();
    		}
    	}
    }
    
    
}