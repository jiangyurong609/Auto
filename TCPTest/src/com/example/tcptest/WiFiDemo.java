package com.example.tcptest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
//http://68.181.99.195/tdat.zip
public class WiFiDemo extends Activity{// implements OnClickListener {
    private WifiManager wifi;
    private ListView lv;
    private TextView textStatus;
    private Button buttonScan, buttonCancel, taskstart, taskend;
    private List<ScanResult> results;

    private String ITEM_KEY = "key";
    private List<HashMap<String, String>> networkList = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter adapter;
    private GPSLocationListener gpsloclistener; 
    private GLocListener googleloclistener;
    private TelephonyManager tManager;
    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_demo);

        textStatus = (TextView) findViewById(R.id.textStatus);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        taskstart = (Button) findViewById(R.id.taskstart);
        taskend = (Button) findViewById(R.id.taskend);
        
        //buttonCancel.setOnClickListener(this);
        //googleloclistener = new GLocListener(this);
        gpsloclistener = new GPSLocationListener(this, true);
        lv = (ListView) findViewById(R.id.list);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
        this.adapter = new SimpleAdapter(WiFiDemo.this, networkList, R.layout.row,
                new String[] { ITEM_KEY }, new int[] { R.id.list_value });
        lv.setAdapter(this.adapter);
        
        initView();
        initTelephony();
        //registerReceiver(
        
        
    }
    private void initView()
    {
        		//, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	networkList.clear();
            	registerReceiver(br,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                wifi.startScan();
                //googleloclistener.connect();
                gpsloclistener.connect();
                textStatus.setText("Scanning....");
                //ScriptExecution.RunScript_local_thread();
                //startService(new Intent(WiFiDemo.this, DownloadService.class));
                Toast toast = Toast.makeText(WiFiDemo.this, "Start Process", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
            }
        });
        
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	unregisterReceiver(br);	
            	//googleloclistener.disconnect();
            	gpsloclistener.disconnect();
            	//ProgramAutoRun.StopTcpdump();
            	Toast toast = Toast.makeText(WiFiDemo.this, "End Process", Toast.LENGTH_SHORT);
            	toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
            }
        });
        taskstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	String taskstart = "taskstart="+System.currentTimeMillis()+"\n";
            	Common.writeToFile(taskstart, Common.TaskLogger);
            	Toast toast = Toast.makeText(WiFiDemo.this, taskstart, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
				
            }
        });
        taskend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//Common.writeToFile("end="+System.currentTimeMillis()+"\n", "TaskLogger");	
            	String taskend = "taskend="+System.currentTimeMillis()+"\n";
            	Common.writeToFile(taskend, Common.TaskLogger);
            	Toast toast = Toast.makeText(WiFiDemo.this, taskend, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
            }
        });
    }
    final BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
        	networkList.clear();
        	//List<String> tostore = new ArrayList<String>();
        	StringBuilder tostore = new StringBuilder("Scan:\n");
            results = wifi.getScanResults();
            textStatus.setText(results.size() + " networks found");
            for (ScanResult result : results) {
                HashMap<String, String> items = new HashMap<String, String>();
                
                //String itenName = "BSSID=" + result.BSSID + ", SSID=" + result.SSID
                //        + ", capabilities=" + result.capabilities;
                StringBuilder sb = new StringBuilder("");
    			
    			sb.append(System.currentTimeMillis())
                .append(",bootts=")
    			.append(SystemClock.elapsedRealtimeNanos())
    	    	.append(",BSSID=") 
    	    	.append(result.BSSID)
    	    	.append(",SSID=") 
    	    	.append(result.SSID)
    	    	.append(",capabilities=") 
    	    	.append(result.capabilities)
    	    	.append(",frequency=") 
    	    	.append(result.frequency)
    	    	.append(",level=") 
    	    	.append(result.level)
    	    	.append(",describeContents=") 
    	    	//.append(location.getProvider())
    	    	//.append(",")
    	    	.append(result.describeContents())
    	    	.append(",timestamp=")
    	    	.append(result.timestamp);
    	    	//.append(",lat=")
    	    	//.append(gpsloclistener.current_location.getLatitude())
    	    	//.append(",lon=")
    	    	//.append(gpsloclistener.current_location.getLongitude())
    	    	//.append(",speed=")
    	    	//.append(gpsloclistener.current_location.getSpeed())
    	    	//.append("\n");
    	    	if(gpsloclistener.current_location != null && gpsloclistener.current_location.getLatitude()!= -1)
    	    	{
    	    		sb.append(",lat=")
        	    	.append(gpsloclistener.current_location.getLatitude())
        	    	.append(",lon=")
        	    	.append(gpsloclistener.current_location.getLongitude())
        	    	.append(",speed=")
        	    	.append(gpsloclistener.current_location.getSpeed())
        	    	.append("\n");
    	    	}else    	    	{
    	    		sb.append("\n");
    	    	}
    			tostore.append(sb.toString());
    		   // Log.d(Common.TAG, sb.toString());	
    	    	
    	    	
                items.put(ITEM_KEY, sb.toString());
                networkList.add(items);
                adapter.notifyDataSetChanged();
            }
            Common.writeToFile(tostore.toString(), "WIFILog");
            
            wifi.startScan();
            //textStatus.setText("Scanning....");

        }
    };

    private void initTelephony()
    {
    	 tManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    	 tManager.listen(new CustomPhoneStateListener(this, tManager),
    	 PhoneStateListener.LISTEN_CALL_STATE
    	 | PhoneStateListener.LISTEN_CELL_INFO // Requires API 17
    	 | PhoneStateListener.LISTEN_CELL_LOCATION
    	 | PhoneStateListener.LISTEN_DATA_ACTIVITY
    	 | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
    	 | PhoneStateListener.LISTEN_SERVICE_STATE
    	 | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
    	 | PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR
    	 | PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR);
    	// updateNeighborCells() ;
    }
    /*
	  private final void updateNeighborCells() 
	  { 
	    List< NeighboringCellInfo > listNeigborCells = 
	    		tManager.getNeighboringCellInfo(); 
	     
	    if ( listNeigborCells != null &&  listNeigborCells.size() >0 ) 
	    { 
	    	StringBuffer buf = new StringBuffer();
	    	String tempResult = "";
	    	
	    	for ( NeighboringCellInfo cellinfo : listNeigborCells ) 
	    	{ 
	    		//Log.i(LOG_TAG, "NeighboringCellInfo: " + cellinfo);
	    		tempResult =  cellinfo.getLac() + "," 
                      + cellinfo.getCid() + "," + cellinfo.getRssi() + ";";
	    		buf.append(tempResult);	

	   
	    	} 
	    	buf.deleteCharAt(buf.length() - 1);
	        //return buf.toString();
	    	Log.i(CustomPhoneStateListener.LOG_TAG, "NeighboringCellInfo: " + buf.toString());

	    }
	    else
	    {
	    	Log.i(CustomPhoneStateListener.LOG_TAG, "NeighboringCellInfo: None found"  );
	    }
	  }
     */
}
