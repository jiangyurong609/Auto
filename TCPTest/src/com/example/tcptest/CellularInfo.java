package com.example.tcptest;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class CellularInfo {

	private Context context;
	private TelephonyManager mTelephonyMgr;

	public CellularInfo(Context ctx)
	{
		
		context = ctx;
		mTelephonyMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		
		// Register the listener with the telephony manager
		mTelephonyMgr.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		String carrierName = mTelephonyMgr.getNetworkOperatorName();

		Log.d(Common.TAG, carrierName);
		Log.d(Common.TAG, get_network());
	}

	public String get_network()
    {
		 Activity act=(Activity)context;
		 String network_type="UNKNOWN";//maybe usb reverse tethering
		 NetworkInfo active_network=((ConnectivityManager)act.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		 if (active_network!=null && active_network.isConnectedOrConnecting())
		    {if (active_network.getType()==ConnectivityManager.TYPE_WIFI)
		        {network_type="WIFI";
		        }
		     else if (active_network.getType()==ConnectivityManager.TYPE_MOBILE)
		          {network_type=((ConnectivityManager)act.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().getSubtypeName();
		          }
		    }
		 return network_type;
    }
	 // Listener for signal strength.
    final PhoneStateListener mListener = new PhoneStateListener()
    {

    	@Override
    	public void onSignalStrengthsChanged(SignalStrength sStrength)
    	{
    		String ssignal = sStrength.toString();
    		

    		String[] parts = ssignal.split(" ");
    		
    		int dbm = 0;

    		if ( mTelephonyMgr.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE){

    		    //dbm = Integer.parseInt(parts[8])*2-113;
    			dbm = Integer.parseInt(parts[9]); //Rsrp
    			//Log.d(Common.TAG, parts[13]);

    		}
    		else{

    		    if (sStrength.getGsmSignalStrength() != 99) {
    		                    int intdbm = -113 + 2
    		                            * sStrength.getGsmSignalStrength();
    		                    dbm = (intdbm);
    		                }
    		}
    		//Log.d(Common.TAG, Integer.toString(dbm));

    	}
    	@Override
    	public void onCellInfoChanged(List<CellInfo> cellInfos)
    	{
    		//for(int i = 0; i < cellInfo.size(); i++)
    		for(CellInfo cellInfo : cellInfos)
    		{
    			StringBuilder sb = new StringBuilder("");
    			if(cellInfo instanceof CellInfoGsm)
    			{
	    			CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
	
	    		    CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
	    		    CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();
	    		    
	    		    sb.append(System.currentTimeMillis())
                    .append(",GSM,bootts=")
        			.append(SystemClock.elapsedRealtimeNanos())
        			.append(",registered=")
        			.append(cellInfoGsm.isRegistered())
        			.append(",cellIdentity=")
        			.append(cellIdentity.toString())
        			.append(",cellSignalStrengthGsm=")
        			.append(cellSignalStrengthGsm.toString())
	    		    .append(",timestamp=")
        	    	.append(cellInfoGsm.getTimeStamp())
        	    	.append("\n");
	    		    //Log.d("cell", "registered: "+cellInfoGsm.isRegistered());
	    		    //Log.d("cell", cellIdentity.toString());         
	    		    //Log.d("cell", cellSignalStrengthGsm.toString());
    			}
    			else if (cellInfo instanceof CellInfoCdma)
    			{
    				CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;

        		    CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
        		    CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
        		    sb.append(System.currentTimeMillis())
                    .append(",CDMA,bootts=")
        			.append(SystemClock.elapsedRealtimeNanos())
        			.append(",registered=")
        			.append(cellInfoCdma.isRegistered())
        			.append(",cellIdentity=")
        			.append(cellIdentity.toString())
        			.append(",cellSignalStrengthGsm=")
        			.append(cellSignalStrengthCdma.toString())
	    		    .append(",timestamp=")
        	    	.append(cellInfoCdma.getTimeStamp())
        	    	.append("\n");
        		    //Log.d("cell", "registered: "+cellInfoCdma.isRegistered());
        		    //Log.d("cell", cellIdentity.toString());         
        		    //Log.d("cell", cellSignalStrengthCdma.toString());
    			}else if (cellInfo instanceof CellInfoWcdma)
    			{
    				CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;

        		    CellIdentityWcdma cellIdentity = cellInfoWcdma.getCellIdentity();
        		    CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
        		    sb.append(System.currentTimeMillis())
                    .append(",WCDMA,bootts=")
        			.append(SystemClock.elapsedRealtimeNanos())
        			.append(",registered=")
        			.append(cellInfoWcdma.isRegistered())
        			.append(",cellIdentity=")
        			.append(cellIdentity.toString())
        			.append(",cellSignalStrengthGsm=")
        			.append(cellSignalStrengthWcdma.toString())
	    		    .append(",timestamp=")
        	    	.append(cellInfoWcdma.getTimeStamp())
        	    	.append("\n");
    			}else if(cellInfo instanceof CellInfoLte)
    			{
    				CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;

        		    CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
        		    CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
        		    sb.append(System.currentTimeMillis())
                    .append(",LTE,bootts=")
        			.append(SystemClock.elapsedRealtimeNanos())
        			.append(",registered=")
        			.append(cellInfoLte.isRegistered())
        			.append(",cellIdentity=")
        			.append(cellIdentity.toString())
        			.append(",cellSignalStrengthGsm=")
        			.append(cellSignalStrengthLte.toString())
	    		    .append(",timestamp=")
        	    	.append(cellInfoLte.getTimeStamp())
        	    	.append("\n");
    			}
    			
    		}
    	}
    	@Override
    	public void onCellLocationChanged(CellLocation location)
    	{
    		super.onCellLocationChanged(location);

            int cid = 0;
            int lac = 0;
            
            if (location != null) {
                if (location instanceof GsmCellLocation) {
                    cid = ((GsmCellLocation) location).getCid();
                    lac = ((GsmCellLocation) location).getLac();
                }
                else if (location instanceof CdmaCellLocation) {
                    cid = ((CdmaCellLocation) location).getBaseStationId();
                    lac = ((CdmaCellLocation) location).getSystemId();
                }
            }
            String cellInfo = Integer.toString(lac)+"-"+Integer.toString(cid);
            Log.v("logg","CELL CHANGED:"+cellInfo);

    	}
    	
    	@Override
    	public void onDataConnectionStateChanged(int state, int networkType)
    	{
    		
    	}
    	
    	@Override
    	public void onDataActivity(int direction)
    	//Callback invoked when data activity state changes.
    	{
    		
    	}
    	@Override
    	public void onServiceStateChanged(ServiceState serviceState)
    	//Callback invoked when device service state changes.
    	{
    		
    	}

    };
	
    /**
     * 
     * part[0] = "Signalstrength:"  _ignore this, it's just the title_

		parts[1] = GsmSignalStrength
		
		parts[2] = GsmBitErrorRate
		
		parts[3] = CdmaDbm
		
		parts[4] = CdmaEcio
		
		parts[5] = EvdoDbm
		
		parts[6] = EvdoEcio
		
		parts[7] = EvdoSnr
		
		parts[8] = LteSignalStrength
		
		parts[9] = LteRsrp
		
		parts[10] = LteRsrq
		
		parts[11] = LteRssnr
		
		parts[12] = LteCqi
		
		parts[13] = gsm|lte
		
		parts[14] = _not reall sure what this number is_
     */
	
}
