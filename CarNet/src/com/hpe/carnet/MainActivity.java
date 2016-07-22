package com.hpe.carnet;

//import com.download_client.R;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.hpe.carnet.R;
import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity { 
    //public static String TAG = "==";

    //private VideoView myVideoView =null;
    private long waittime = 3000;   
    //private String current_destination; 
    private GCMReceiver mGCMReceiver;
    private IntentFilter mOnRegisteredFilter;	
    private String network_type;
    private int webprogress = 0;
    private WebView webview;
    private ProgressBar progBar;
    MyFTPClient ftpclient = null;
    List<Parameter> param = null;
    // wifi scan
    WifiManager wifi;       
    ListView lv;
    TextView textStatus;
    Button buttonScan;
    int size = 0;
    List<ScanResult> results;

    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;

    
    
    //private static String serverIP = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CellularInfo cell = new CellularInfo(this);
		network_type = cell.get_network();
		RegisterGCM();//register gcm;
		initialView();        
		param = ParseXML.getParameters();
		
		// wifi scanner code
		/*
        textStatus = (TextView) findViewById(R.id.textStatus);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener((OnClickListener) MainActivity.this);
        lv = (ListView)findViewById(R.id.list);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }   
        this.adapter = new SimpleAdapter(MainActivity.this, arraylist, R.layout.row, new String[] { ITEM_KEY }, new int[] { R.id.list_value });
        lv.setAdapter(this.adapter);

        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent) 
            {
               results = wifi.getScanResults();
               size = results.size();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));  
        */
		//ftpclient = new MyFTPClient();
		
	}
	
	
    private class GCMReceiver extends BroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	      String regId = intent.getStringExtra(Common.FIELD_REGISTRATION_ID);
	     // sendIdToServer(regId);
	    }
	  }

	@Override
	public void onResume() {
	  super.onResume();
	  registerReceiver(mGCMReceiver, mOnRegisteredFilter);
	}
	
	@Override
	public void onPause() {
	  super.onPause();
	  unregisterReceiver(mGCMReceiver);
	}

	private void initialView()
	{
		setContentView(R.layout.init);
	    Button start = null;
	    Button video = null;
	    Button web = null;
	    Button back = null;
	    Button wifiScanActivity = null;
		webview = (WebView)findViewById(R.id.webview);
     	progBar = (ProgressBar) findViewById(R.id.progWeb);
     	//videoview = (VideoView)findViewById(R.id.myvideoview);
     	
     	progBar.setVisibility(ProgressBar.INVISIBLE);    	
        
		start = (Button)findViewById(R.id.start);
		back = (Button)findViewById(R.id.back);
		video = (Button)findViewById(R.id.video);
		web = (Button)findViewById(R.id.web);
		wifiScanActivity =  (Button)findViewById(R.id.ftp);
		start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//startService(new Intent(MainActivity.this, DownloadService.class));
            	ScriptExecution.RunScript_local_thread();
            	//ScriptExecution.RunScript_Background();
            }
        });
     
		back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//startService(new Intent(MainActivity.this, DownloadService.class));
            	//ScriptExecution.RunScript_local_thread();
            	ScriptExecution.RunScript_Background();
            }
        });
		video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	 
            	viewVideo("http://128.125.121.205/10M.mp4");
            	
            }
        });
        
     
		web.setOnClickListener(new View.OnClickListener() {
            //Handler handler = new Handler();
            @Override
            public void onClick(View v) {      	
            	
            	//loadWebpage("http://www.latimes.com/", webview, progBar);
            	new BackgroundTask().execute("webload");
            }
       });
		wifiScanActivity.setOnClickListener(new View.OnClickListener() {
            //Handler handler = new Handler();
            @Override
            public void onClick(View v) {      	
            	//new BackgroundTask().execute("ftp");
            	Intent nextScreen = new Intent(getApplicationContext(), WiFiDemo.class);
                //Sending data to another Activity
                //nextScreen.putExtra("name", inputName.getText().toString());
                //nextScreen.putExtra("email", inputEmail.getText().toString()); 
                //Log.e("n", inputName.getText()+"."+ inputEmail.getText());
                startActivity(nextScreen);
            }
       });
	}
	//private void loadWebpage(String url, WebView webview, final ProgressBar progBar)
	private void loadWebpage(String url)
	{
		//setContentView(R.layout.web);
		//final WebView webview =  (WebView)findViewById(R.id.webview);
     	//final ProgressBar progBar = (ProgressBar) findViewById(R.id.progWeb);
     	//videoview = (VideoView)findViewById(R.id.myvideoview);
     	
     	progBar.setVisibility(ProgressBar.INVISIBLE);   
		if(webview != null)
		{
			webview.clearCache(true);
			webview.clearFormData();
		}
		webview.getSettings().setJavaScriptEnabled(true);
    	webview.setWebChromeClient(new WebChromeClient() {
    		   public void onProgressChanged(WebView view, int progress) {
    		     // Activities and WebViews measure progress with different scales.
    		     // The progress meter will automatically disappear when we reach 100%
    			  //activity.setProgress(progress * 1000);
    			   progBar.setProgress(progress);
    			   webprogress = progress;
                   if (progress == 100) {
                       progBar.setVisibility(ProgressBar.INVISIBLE);
                       progBar.setProgress(0);
                   } else {
                       progBar.setVisibility(ProgressBar.VISIBLE);
                   }
    		   }
    		 });
		 webview.setWebViewClient(new WebViewClient() {
		   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		     Toast.makeText(MainActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			 //  Log.d(Common.TAG, "Oh no! " + description);
		   }
		 });

    	//webview.loadUrl("http://www.latimes.com/");
		 webview.loadUrl(url);
	}
	
	private void viewVideo(String url)
	{
		setContentView(R.layout.video);
		final VideoView videoview = (VideoView)findViewById(R.id.myvideoview);
		videoview.setVideoURI(Uri.parse(url));
		videoview.setMediaController(new MediaController(this));
		videoview.requestFocus();
		videoview.start();
		Button sw = (Button)findViewById(R.id.change);
			sw.setOnClickListener(new View.OnClickListener() {
	            //Handler handler = new Handler();
	            @Override
	            public void onClick(View v) {      	
	            	//videoview.pause();
	            	videoview.stopPlayback();
	            	//setContentView(R.layout.init);
	            	initialView();
	            }
	       });
	}
	private void ModifyDNS(String networktype)
	{
		if(networktype.equals("WIFI"))
		{
			ProgramAutoRun.ModifyDNS_wifi(Common.SERVER_IP);
		}else
		{
			ProgramAutoRun.ModifyDNS_cell(Common.SERVER_IP);
		}
	}
	
	/**
	 * GCM registeration
	 */
	private void RegisterGCM()
	{

        mGCMReceiver = new GCMReceiver();
        mOnRegisteredFilter = new IntentFilter();
        mOnRegisteredFilter.addAction(Common.ACTION_ON_REGISTERED);
        registerReceiver(mGCMReceiver, mOnRegisteredFilter);
        
        GCMRegistrar.checkDevice(MainActivity.this);
        GCMRegistrar.checkManifest(this);
        String regId = GCMRegistrar.getRegistrationId(MainActivity.this);
        Log.d(Common.TAG, "id here: "+ regId);
        if (!regId.equals("")) {
            //sendIdToServer(regId);
        	new BackgroundTask().execute(regId);
            Log.d(Common.TAG,"first time get" +regId);
          } else {
            GCMRegistrar.register(MainActivity.this, Common.SENDER_ID);
            //Log.d("==", regId);
            
            //regId = GCMRegistrar.getRegistrationId(this);
          }
	}
    private class BackgroundTask extends AsyncTask<String, String, Void>{
    	 
        // Generates dummy data in a non-ui thread
    	//private Context context;
        @Override
        protected Void doInBackground(String... params) {
        	if(params[0].equals("webload"))
        	{
        		//ModifyDNS(network_type); //commented temporily, for change dns
        		
        		if(params !=  null)
            	{
            		for(int i = 0; i < param.size(); i ++)
            		{	
            			Parameter parameter = param.get(i);
            			ProgramAutoRun.KernelConfigAndStartDump(parameter);	
            			//loadWebpage("http://www.latimes.com/", webview, progBar);
            			loadWebpage("http://www.latimes.com/");
            			while(webprogress < 100)
            			{
            				//Log.d(Common.TAG, "progress: "+ webprogress);
            				try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
            				
            			}
            			//Log.d(Common.TAG, "final progress: "+ webprogress);
            			webprogress = 0;
            			//StartDownload(param.getFilename(), param.getServerIP(),Integer.valueOf(param.getPORT()));
            			ProgramAutoRun.StopTcpdump();
            			
            		}
            	}
        	}
        	else if(params[0].equals("ftp"))
        	{
        		//ModifyDNS(network_type); //commented temporily, for change dns
        		//List<Parameter> param = ParseXML.getParameters();
        		if(params !=  null)
            	{
        			boolean status = false;
					//connect ftp
					status = ftpclient.ftpConnect("128.125.121.204", "ftp-test", "ftp-test", 21);
					if (status == true) {
						Log.d(Common.TAG, "Connection Success");
						//Log.d(Common.TAG, ftpclient.ftpGetCurrentWorkingDirectory());
						//ftpclient.ftpPrintFilesList(ftpclient.ftpGetCurrentWorkingDirectory());
	            		for(int i = 0; i < param.size(); i ++)
	            		{	
	            			Parameter parameter = param.get(i);
	            			ProgramAutoRun.KernelConfigAndStartDump(parameter);	
	            		
	            			//status = ftpclient.ftpUpload(TEMP_FILENAME, TEMP_FILENAME, "/", cntx);
	    						
	    					ftpclient.ftpPrintFilesList(ftpclient.ftpGetCurrentWorkingDirectory());
    						File sdcard = Environment.getExternalStorageDirectory();
    						boolean st = ftpclient.ftpDownload(ftpclient.ftpGetCurrentWorkingDirectory() + "/"+parameter.getFTP(),
    						//boolean st = ftpclient.ftpDownload(ftpclient.ftpGetCurrentWorkingDirectory() + "/Wildlife.wmv",
    								sdcard.getAbsolutePath()+"/project/" + parameter.getFTP());
    						
    						if(st == true)
    							Log.d(Common.TAG , "downfinished");
	            			//StartDownload(param.getFilename(), param.getServerIP(),Integer.valueOf(param.getPORT()));
	            			ProgramAutoRun.StopTcpdump();

	            		}
	            		ftpclient.ftpDisconnect();
					} else {
						//Toast.makeText(getApplicationContext(), "Connection failed", 2000).show();
						Log.d(Common.TAG, "Connection failed");
					}
            	}
        	}
        	else
        	{
        		ServerUtilities.register(MainActivity.this, Common.APPNAME, Common.EMAIL, params[0]);
        	}
        	
        	return null;
        }
        	@Override
            protected void onProgressUpdate(String... values) {

            }
        }



}
