package com.hpe.carnet;

import java.util.Iterator;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
//import edu.usc.enl.carma.conf.Common;
//import edu.usc.enl.carma.conf.Config;


/**
 * Implementation of a GPS connection with the Android-provided interfaces for
 * accessing the internal GPS sensor of the phone (if available). Communication
 * with the GPS adapter is implemented via getter methods which return
 * null/0-results if the location information is too old.
 * 
 * @author Yurong Jiang
 */
public final class GPSLocationListener {

	/**
	 * Constant Class Variables:
	 */
	private static final long DEFAULT_LOCATION_TIMEOUT = 5000L;
	private static final boolean UPDATE_SATELLITES = false;
	private static final boolean UPDATE_SHARED_PREF = false;

	/**
	 * Final Instance Variables:
	 */
	/**
	 * Number of milliseconds which can elapse until the current GPS data is
	 * considered old and will not be returned by the getter functions (timer
	 * resetted if data is updated)
	 */
	//private final Config setting;
	private final long location_timeout;

	/**
	 * Android-provided managing service for accessing GPS-related data
	 */
	private final LocationManager manager;
	/**
	 * Listener which provides methods called once the location of the phone
	 * changes
	 */
	private LocationListener listener;

	/**
	 * Instance Variables:
	 */
	private boolean first_time_gps = true;
	/**
	 * Current number of available satellites (set when LocationChanged event
	 * was raised)
	 */
	private int num_satellites;
	
	/**
	 * Timestamp of the last GPS data update
	 */
	private long last_update_time;
	private long updatetimer;

	/**
	 * Current location (set when LocationChanged event is raised)
	 */
	public Location current_location = new Location("-1,-1");
	public Location previous_location = new Location("-1,-1");

	//private  GeomagneticField mGeomagneticField;
	private enum Status {
		OK, NO_PROVIDER, NO_KNOWN_LOCATION, NULL
	};

	/**
	 * Current status of the GPS device (set when LocationChanged event was
	 * raised)
	 */
	@SuppressWarnings("unused")
	private Status status;

	/**
	 * Listener implementation used for reacting on events like the change of
	 * the location
	 * 
	 * @author Yurong
	 */
	private class MyLocationListener implements LocationListener,GpsStatus.Listener,GpsStatus.NmeaListener {

		private final boolean writeout;
		
		@SuppressWarnings("unused")
		public MyLocationListener(boolean writeout) {
			this.writeout = writeout;
		}
		
		public void onLocationChanged(Location location) {
			current_location = location;
		//	if (this.writeout) {
				this.writeOutGPS(location);
		//	}
		}
		
		private void writeOutGPS(Location location) {
			float decl = Common.updateGeomagneticField(location) ;
			
			StringBuilder sb = new StringBuilder("");
			
			sb.append(System.currentTimeMillis())
	    	.append(",lat=") 
	    	.append(location.getLatitude())
	    	.append(",lon=") 
	    	.append(location.getLongitude())
	    	.append(",alt=") 
	    	.append(location.getAltitude())
	    	.append(",bear=") 
	    	.append(location.getBearing())
	    	.append(",decl=") 
	    	.append(decl)
	    	.append(",speed=") 
	    	//.append(location.getProvider())
	    	//.append(",")
	    	.append(location.getSpeed())
	    	.append(",accuracy=")
	    	.append(location.getAccuracy())
	    	.append(",gpstime=") 
	    	.append(location.getTime())
	    	.append("\n");
		   // Log.d(Common.TAG, sb.toString());	
	    	Common.writeToFile(sb.toString(), "GPSLog");
			//StoreGPS_kalman();
	    	//Log.d(Common.TAG,"Original: "+System.currentTimeMillis()+", "+ sb.toString());
	    	//Log.d(Common.TAG,"mGeomagneticField: "+mGeomagneticField.getDeclination()+", "+ sb.toString());
	    	
		}

		public void onProviderDisabled(String provider) {
			status = Status.NO_PROVIDER;
			Log.e(Common.TAG, "GPS provider has been disabled: " + provider);
		}

		public void onProviderEnabled(String provider) {
			updateGPSData();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {}

		@Override
		public void onNmeaReceived(long timestamp, String nmea) {
			// TODO Auto-generated method stub
			Common.writeToFile(timestamp+","+nmea+'\n', "GPSLogNMEA");
			//Log.d(Common.TAG,nmea );
		}

		@Override
		public void onGpsStatusChanged(int event) {
			// TODO Auto-generated method stub
			
		}
	
	}

	public GPSLocationListener(Context context, boolean writeout) {

		manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			throw new RuntimeException(
					"GPS is not enabled.\n"
							+ "Either activate GPS or deselect phone PIDs requiring GPS access");
		} else {
			listener = new MyLocationListener(writeout);
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100,
					0, listener);
			location_timeout = DEFAULT_LOCATION_TIMEOUT;

			//updatetimer = System.currentTimeMillis();
			last_update_time = System.currentTimeMillis();

			// initialize data fields
			updateGPSData();
		}
		//manager.addGpsStatusListener((GpsStatus.Listener)listener);
		//manager.addNmeaListener((GpsStatus.NmeaListener) listener);
	}
	
	public boolean disconnect() {
		if (listener != null) {
			manager.removeUpdates(listener);
		}
		return true;
	}
	
	public boolean connect() {
		if (listener != null) {
			listener = new MyLocationListener(true);
		}
		manager.addGpsStatusListener((GpsStatus.Listener)listener);
		manager.addNmeaListener((GpsStatus.NmeaListener) listener);
		
		
		return true;
	}
	
	
	/**
	 * Updates the current number of available satellites
	 */
	private void updateSatellites() {
		if (UPDATE_SATELLITES) {
			GpsStatus status = manager.getGpsStatus(null);
			Iterator<GpsSatellite> it = status.getSatellites().iterator();
	
			num_satellites = 0;
			while (it.hasNext()) {
				it.next();
				num_satellites++;
			}
		}
	}
	
	/**
	 * 
	 * @return Number of available satellites (if data is too old, i.e.
	 *         freshness time interval has been exceeded 0 will be returned)
	 */
	@SuppressWarnings("unused")
	private int getNumSatellites() {
		int n = 0;
		if (UPDATE_SATELLITES) {			
			long now = System.currentTimeMillis();
			long dif = last_update_time + location_timeout;
			if (dif > now) {
				n = num_satellites;
			}
		}
		return n;
	}
	
	/**
	 * @return Current location information (if data is too old, i.e. freshness
	 *         time interval has been exceeded null will be returned)
	 */
	public Location getLocation() {
		Location l = null;
		long now = System.currentTimeMillis();
		long dif = last_update_time + location_timeout;
		if (dif > now) {
			l = current_location;
		}
		return l;
	}
	
	/**
	 * @return Current location information ignoring freshness
	 */
	public Location getLocationBestEffort() {
		if (current_location == null) {
			return new Location("-1,-1"); 
		}
		return current_location;
	}

	/**
	 * Updates the GPS data stored in the local variables
	 */
	private void updateGPSData() {

		// check if provider is enabled
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			status = Status.NO_PROVIDER;
			Log.e(Common.TAG, "GPS is not eneabled but it should be.");
			return;
		}

		// get last known location
		current_location = manager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (current_location == null) {
			status = Status.NO_KNOWN_LOCATION;
			return;
		}
		
		if (UPDATE_SATELLITES) {
			updateSatellites();			
		}

		// set latest timestamp
		last_update_time = System.currentTimeMillis();
	}
	

	
}
