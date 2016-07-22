package com.hpe.carnet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class GLocListener implements ConnectionCallbacks,
OnConnectionFailedListener, LocationListener{

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

	private Location mLastLocation;

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	// boolean flag to toggle periodic location updates
	private boolean mRequestingLocationUpdates = false;

	
    private static final long MILLISECONDS_PER_SECOND = 1000L;
    // Update frequency in seconds
    private static final long UPDATE_INTERVAL_IN_SECONDS = 5L;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final long FASTEST_INTERVAL_IN_SECONDS = 1L;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    	
    // Define an object that holds accuracy and frequency parameters
    private  LocationRequest mLocationRequest;
    //private final LocationClient mLocationClient;
    private static final String TAG = Common.TAG;

    private Location currentLocation;
    
	private static int DISPLACEMENT = 10; // 10 meters
	private static Context ctx;
	public  GLocListener (Context c)
	{
		ctx =c;
		currentLocation = new Location("currentLocation");
		currentLocation.setLatitude(0.0);
		currentLocation.setLongitude(0.0);
	
	    if (checkPlayServices()) {
	
			// Building the GoogleApi client
			buildGoogleApiClient();
	
			createLocationRequest();
		}
	    if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	    Log.d(TAG, "GPSLocationAdapter initialized.");
	}

/*
 * Called by Location Services when the request to connect the
 * client finishes successfully. At this point, you can
 * request the current location or start periodic updates
 */
@Override
public void onConnected(Bundle dataBundle) {
	Log.d(TAG, "GPSLocationAdapter connected, starting periodic updates.");
	// Once connected with google api, get the location
			//displayLocation();
	if (mRequestingLocationUpdates) {
		startLocationUpdates();
	}
    // start periodic updates
    //mLocationClient.requestLocationUpdates(mLocationRequest, this);
}



/*
 * Called by Location Services if the connection to the
 * location client drops because of an error.
 */
public void onDisconnected() {
	Log.d(TAG, "GPSLocationAdapter disconnected.");
}

/*
 * Called by Location Services if the attempt to
 * Location Services fails.
 */
@Override
public void onConnectionFailed(ConnectionResult connectionResult) {    	
    /*
     * Google Play services can resolve some errors it detects.
     * If the error has a resolution, try sending an Intent to
     * start a Google Play services activity that can resolve
     * error.
     */
    if (connectionResult.hasResolution()) {
    	Log.e(TAG, "GPSLocationAdapter connection failed (has resolution) " + connectionResult.toString());
    } else {
        /*
         * If no resolution is available, display a dialog to the
         * user with the error.
         */
    	Log.e(TAG, "GPSLocationAdapter connection failed (no resolution) " + connectionResult.toString());
    }
}
@Override
public void onConnectionSuspended(int cause) {
	// TODO Auto-generated method stub
	mGoogleApiClient.connect();
}

// Define the callback method that receives location updates
@SuppressLint("NewApi")
@Override
public void onLocationChanged(Location location) {
	//Log.e(TAG, "Current location: " + location.getLatitude() + ", " + location.getLongitude());
	currentLocation = location;
	float decl = Common.updateGeomagneticField(location) ;
	StringBuilder sb = new StringBuilder("");
	sb.append(System.currentTimeMillis())
	.append(",") 
	.append(location.getLatitude())
	.append(",") 
	.append(location.getLongitude())
	.append(",") 
	.append(location.getAltitude())
	.append(",") 
	.append(location.getBearing())
	.append(",") 
	//.append(location.getSpeed())
	.append(decl)
	.append(",") 
	//.append(location.getProvider())
	//.append(",") 
	.append(location.getAccuracy())
	.append(",") 
	.append(location.getTime())
	.append("\n");
	Common.writeToFile(sb.toString(), "GPSLog_g");
	//Log.d(Common.TAG,"Google: "+System.currentTimeMillis()+", "+ sb.toString());
}

/**
 * Creating google api client object
 * */
protected synchronized void buildGoogleApiClient() {
	mGoogleApiClient = new GoogleApiClient.Builder(ctx)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this)
			.addApi(LocationServices.API).build();
}

/**
 * Creating location request object
 * */
protected void createLocationRequest() {
	mLocationRequest = new LocationRequest();
	mLocationRequest.setInterval(UPDATE_INTERVAL);
	mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
	mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
}

/**
 * Method to verify google play services on the device
 * */
private boolean checkPlayServices() {
	int resultCode = GooglePlayServicesUtil
			.isGooglePlayServicesAvailable(ctx);
	if (resultCode != ConnectionResult.SUCCESS) {
		if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
			GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) ctx,
					PLAY_SERVICES_RESOLUTION_REQUEST).show();
		} else {
			Toast.makeText(ctx.getApplicationContext(),
					"This device is not supported.", Toast.LENGTH_LONG)
					.show();
			
		}
		return false;
	}
	return true;
}

/**
 * Starting the location updates
 * */
protected void startLocationUpdates() {

	LocationServices.FusedLocationApi.requestLocationUpdates(
			mGoogleApiClient, mLocationRequest, this);

}

/**
 * Stopping location updates
 */
protected void stopLocationUpdates() {
	LocationServices.FusedLocationApi.removeLocationUpdates(
			mGoogleApiClient, this);
	
	
}


public Location getLocation() {
	return currentLocation;
}

public void connect()
{	
	if (!mRequestingLocationUpdates) {
		// Changing the button text
		if (mGoogleApiClient==null || !mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		}
		
		mRequestingLocationUpdates = true;

		// Starting the location updates
		startLocationUpdates();

		Log.d(TAG, "Periodic location updates started!");

	} 
}

public void disconnect() {
	Log.d(TAG, "GPSLocationAdapter is being disconnected.");
	mRequestingLocationUpdates = false;
	stopLocationUpdates();
	if (mGoogleApiClient.isConnected()) {
		mGoogleApiClient.disconnect();
	}
	// If the client is connected
    //if (mLocationClient.isConnected()) {
        /*
         * Remove location updates for a listener.
         * The current Activity is the listener, so
         * the argument is "this".
         * https://code.google.com/p/android/issues/detail?id=62173
         */
   // 	mLocationClient.removeLocationUpdates(this);
   // }
    /*
     * After disconnect() is called, the client is
     * considered "dead".
     */
    //mLocationClient.disconnect();
}


}
