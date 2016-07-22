package com.hpe.carnet;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;

public class DownloadService extends Service{


	private final static int Buffer = 8 * 1024;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		RunScript();
		
		int res = super.onStartCommand(intent, flags, startId);
		return res;
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	private void RunScript()
	{
		List<Parameter> params = ParseXML.getParameters();
		if(params !=  null)
    	{
    		
    		for(int i = 0; i < params.size(); i ++)
    		{	
    			Parameter param = params.get(i);
    			ProgramAutoRun.KernelConfigAndStartDump(param);
    			String[]taskparam = {param.getFilename(), param.getServerIP(),param.getPORT()};
    		    DownloadAsyncTask downtask = new DownloadAsyncTask();
    		    try {
					boolean success = downtask.execute(taskparam).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

    			Log.d(Common.TAG, taskparam[0] + " "+taskparam[1]+" "+taskparam[2]);
    			//StartDownload(param.getFilename(), param.getServerIP(),Integer.valueOf(param.getPORT()));
    			ProgramAutoRun.StopTcpdump();
    			
    		}
    	}
	}


}
