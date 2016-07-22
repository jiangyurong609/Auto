package com.example.tcptest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.hardware.GeomagneticField;
import android.location.Location;
import android.os.Environment;




public class Common {
	//GCM
		public static final String SENDER_ID = "1093271015079";
		public static final String URL = "url";
		public static final String SENDER_URL = "http://128.125.121.205/gcm_server_php/register.php";
		public static final String ACTION_ON_REGISTERED = "com.example.tcptest.ON_REGISTERED";
		public static final String FIELD_REGISTRATION_ID = "registration_id";
		
		public static final String FIELD_MESSAGE = "msg";
		public static final String APPNAME = "TCPTest";
		public static final String EMAIL = "test@gmail.com";
		public static final String TAG = "==";
		//public static final int DEFAULT_Config_ID = R.raw.config;
		public static final String SERVER_IP = "128.125.121.204";
		public static final String TaskLogger= "TaskLogger";
		public static final long sleeptime = 3000;
		
		public static float updateGeomagneticField(Location mLastLocation) {
			GeomagneticField mGeomagneticField = new GeomagneticField((float) mLastLocation.getLatitude(),
	                (float) mLastLocation.getLongitude(), (float) mLastLocation.getAltitude(),
	                mLastLocation.getTime());
	       return mGeomagneticField.getDeclination();
	    }
		public static void writeToFile(String val, String name) {
			
			File dir = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "project");
			if(!dir.exists()) {dir.mkdirs();}
			
			//File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Delay");
			
			File file = new File(dir, name);
			
			try {
				if(!file.exists()) {file.createNewFile();}
				
				FileOutputStream fout = new FileOutputStream(file, true);
				fout.write(val.getBytes());
				
				fout.flush();
				fout.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//FileOutputStream fOut = openFileOutput(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Delay", Context.MODE_APPEND);
		}
		
}
