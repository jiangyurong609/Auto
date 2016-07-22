package com.hpe.carnet;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class GCMIntentService extends GCMBaseIntentService{
	  //private String TAG = "MedusaGCM";
	   public GCMIntentService() {
	        super( Common.SENDER_ID );
	        // TODO Auto-generated constructor stub
	        //Log.i( Common.TAG, "GCMIntentService constructor called" );
	    }

	  @Override
	  protected void onRegistered(Context context, String regId) {
		  Log.d(Common.TAG, "registered: "+ regId);
		  //GCMRegistrar.setRegisteredOnServer(context, true);
	      ServerUtilities.register(context, Common.APPNAME, Common.EMAIL, regId);

		  Intent intent = new Intent(Common.ACTION_ON_REGISTERED);
		  intent.putExtra(Common.FIELD_REGISTRATION_ID, regId);
	    
		  context.sendBroadcast(intent);
	  }

	  @Override
	  protected void onUnregistered(Context context, String regId) {
	    Log.i(Common.TAG, "onUnregistered: "+ regId);
	  }

	  @Override
	  protected void onMessage(Context context, Intent intent) {
	    String msg = intent.getStringExtra(Common.FIELD_MESSAGE);
	    //String message = intent.getExtras().getString("price");
        
	    //Log.d("==", msg);
	    Log.d(Common.TAG, "* GCM-Msg:" + msg);
	    if(msg != null)
	    {
	    	ScriptExecution.RunScript_GCM(msg);
	    }
	    
	   // MedusaUtil.invokeMedusalet(msg);
	  }

	  @Override
	  protected void onError(Context context, String errorId) {
	    Toast.makeText(context, errorId, Toast.LENGTH_LONG).show();
	  }
	  
	
}
