package com.hpe.carnet;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.util.Log;

public class ScriptExecution {

	public static void RunScript_local_asyn()
	{
		List<Parameter> params = ParseXML.getParameters();
		if(params !=  null)
    	{
    		
    		for(int i = 0; i < params.size(); i ++)
    		{	
    			Parameter param = params.get(i);
    			String pcapname = ProgramAutoRun.KernelConfigAndStartDump(param);
    			String[]taskparam = {param.getFilename(), param.getServerIP(),param.getPORT(), pcapname};
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
	public static void RunScript_local_thread()
	{
		List<Parameter> params = ParseXML.getParameters();
		if(params !=  null)
    	{
    		
    		for(int i = 0; i < params.size(); i ++)
    		{	
    			Parameter param = params.get(i);
    			String pcapname = ProgramAutoRun.KernelConfigAndStartDump(param);
    			
    			String[]taskparam = {param.getFilename(), param.getServerIP(),param.getPORT()};
    			DownloadThread downthread = new DownloadThread(param.getFilename(), pcapname, param.getServerIP(),
						Integer.valueOf(param.getPORT()));
    			downthread.start();
    			try {
					downthread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			Log.d(Common.TAG, taskparam[0] + " "+taskparam[1]+" "+taskparam[2]);

    			//StartDownload(param.getFilename(), param.getServerIP(),Integer.valueOf(param.getPORT()));
    			ProgramAutoRun.StopTcpdump();
    			
    		}
    	}
	}
	public static void RunScript_Background()
	{
		List<Parameter> params = ParseXML.getParameters();
		if(params !=  null)
    	{
    		
    		for(int i = 0; i < params.size(); i ++)
    		{	
    			Parameter param = params.get(i);
    			String pcapname = ProgramAutoRun.KernelConfigAndStartDump(param);
    			String[]taskparam = {param.getFilename(), param.getServerIP(),param.getPORT()};
    			DownloadThread backthread = new DownloadThread(param.getBackgroundFilename(),pcapname, param.getServerIP(),
						Integer.valueOf(param.getBackgroundPORT()));
    			backthread.start();
				
    			try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			DownloadThread downthread = new DownloadThread(param.getFilename() ,pcapname, param.getServerIP(),
						Integer.valueOf(param.getPORT()));
    			downthread.start();
    			/*
    			try {
					downthread.join();
					//backthread.InterruptDownload();
					backthread.interrupt();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			*/
    			while(downthread.isAlive())
    			{
    				try {
    					Thread.sleep(500);
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    		
    			if(backthread.isAlive())
    			{
    				backthread.interrupt();
    			}
    			
    			Log.d(Common.TAG, taskparam[0] + " "+taskparam[1]+" "+taskparam[2]);
    			//StartDownload(param.getFilename(), param.getServerIP(),Integer.valueOf(param.getPORT()));
    			ProgramAutoRun.StopTcpdump();
    			
    		}
    	}
	}
	public static void RunScript_GCM(String msg)
	{
		List<Parameter> params = ParseXML.getParameters(msg);
		if(params !=  null)
    	{
    		
    		for(int i = 0; i < params.size(); i ++)
    		{	
    			Parameter param = params.get(i);
    			String pcapfile = ProgramAutoRun.KernelConfigAndStartDump(param);
    			String[]taskparam = {param.getFilename(), param.getServerIP(),param.getPORT(), pcapfile};
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
