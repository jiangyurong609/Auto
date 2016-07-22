package com.hpe.carnet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.os.Environment;
import android.util.Log;

public class ProgramAutoRun {

	public static final String TAG = "==";

	public static String KernelConfigAndStartDump(Parameter param)
	//public static void KernelConfigure()
	{
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File (sdcard.getAbsolutePath() + "/project");
		if(!dir.exists())
			dir.mkdirs();
		String pcapname ="";
		File file = new File(dir, "startdump.sh");
		try {
			
			pcapname += "File_"+param.getFilename().replace(".", "_")+"_PORT_"+param.getPORT()+"_";
			file.createNewFile();
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append("#!/bin/sh\n");
			//rmem setting echo 鈥渕in default max鈥� > /proc/sys/net/ipv4/tcp_rmem
			if(param.getrmem_min()!=null && param.getrmem_default() != null && param.getrmem_max() != null)
			{	
				String rmem_cmd = "echo \"" + param.getrmem_min()+" "
						+ param.getrmem_default() + " " + param.getrmem_max()+"\" > /proc/sys/net/ipv4/tcp_rmem";
				myOutWriter.append("su -c \""+rmem_cmd+"\"\n");
				pcapname += "rmem_"+param.getrmem_min()+"_"+param.getrmem_default()+"_"+param.getrmem_max()+"_";
			}
			// ECR echo yourvalue > /proc/sys/net/ipv4/ipfrag_secret_interval
			if(param.getECR()!=null)
			{
				String ecr_cmd = "su -c \"echo " + param.getECR() + " > /proc/sys/net/ipv4/ipfrag_secret_interval\"\n";
				myOutWriter.append(ecr_cmd);
				pcapname += "ECR_"+param.getECR()+"_";
			}
			//ECN echo yourvalue > /proc/sys/net/ipv4/tcp_ecn
			if(param.getECN()!=null)
			{
				String ecn_cmd = "su -c \"echo " + param.getECN() + " > /proc/sys/net/ipv4/tcp_ecn\"\n";
				myOutWriter.append(ecn_cmd);
				pcapname += "ECN_"+param.getECN()+"_";
			}
			//timestamp echo yourvalue > /proc/sys/net/ipv4/tcp_timestamps
			if(param.getTimestamp()!=null)
			{
				String ts_cmd = "su -c \"echo " + param.getTimestamp() + " > /proc/sys/net/ipv4/tcp_timestamps\"\n";
				myOutWriter.append(ts_cmd);
				pcapname += "TS_"+param.getTimestamp()+"_";
			}
			// mtu ifconfig $Device mtu $mtu_value 
			/**
			 * Be careful, change the interface to the desired one, to check: netcfg 
			 */
			if(param.getMTU()!=null)
			{
				String ts_cmd = "su -c \"ifconfig wlan0 mtu " + param.getMTU() + "\"\n";
				myOutWriter.append(ts_cmd);
				pcapname += "MTU_"+param.getMTU();
			}
			
			//start tcpdump
			//String tcpdump = "su -c \"tcpdump -s0 -v -w /storage/sdcard0/project/" + pcapname+".pcap\"\n";
			String tcpdump = "su -c \"tcpdump -s0 -i wlan0 -v -w /sdcard/project/" + pcapname+".pcap\"\n";
			myOutWriter.append(tcpdump);
			myOutWriter.flush();
			myOutWriter.close();
			
			fOut.close();
			
			//Process proc =
			Runtime.getRuntime().exec("sh /sdcard/project/startdump.sh");
			
			Thread.sleep( Common.sleeptime); //sleep for while after started tcpdump
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pcapname;
		// 
	}
	
	public static boolean ModifyDNS_cell(String dns_serverip)
	//public static void KernelConfigure()
	{
		boolean sucess = true;
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File (sdcard.getAbsolutePath() + "/project");
		if(!dir.exists())
			dir.mkdirs();
		//String pcapname ="";
		File file = new File(dir, "modifydns.sh");
		try {
			
			file.createNewFile();
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append("#!/bin/sh\n");
			//rmem setting echo 鈥渕in default max鈥� > /proc/sys/net/ipv4/tcp_rmem
			myOutWriter.append("su -c \"setprop net.rmnet0.dns1 " +dns_serverip +"\"\n");
			myOutWriter.append("su -c \"setprop net.rmnet0.dns2 " +dns_serverip +"\"\n");
			myOutWriter.append("su -c \"setprop net.dns1 " +dns_serverip +"\"\n");
			myOutWriter.append("su -c \"setprop net.dns2 " +dns_serverip +"\"\n");
			
			myOutWriter.flush();
			myOutWriter.close();
			
			fOut.close();
			
			Process proc = Runtime.getRuntime().exec("sh /storage/sdcard0/project/modifydns.sh");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			sucess = false;
			e.printStackTrace();
		}
		return sucess;
		// 
	}
	public static boolean ModifyDNS_wifi(String dns_serverip)
	//public static void KernelConfigure()
	{
		boolean sucess = true;
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File (sdcard.getAbsolutePath() + "/project");
		if(!dir.exists())
			dir.mkdirs();
		//String pcapname ="";
		File file = new File(dir, "modifydns.sh");
		try {
			
			file.createNewFile();
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append("#!/bin/sh\n");
			//rmem setting echo 鈥渕in default max鈥� > /proc/sys/net/ipv4/tcp_rmem
			myOutWriter.append("su -c \"setprop dhcp.wlan0.dns1 " +dns_serverip +"\"\n");
			myOutWriter.append("su -c \"setprop dhcp.wlan0.dns2 " +dns_serverip +"\"\n");
			myOutWriter.append("su -c \"setprop net.dns1 " +dns_serverip +"\"\n");
			myOutWriter.append("su -c \"setprop net.dns2 " +dns_serverip +"\"\n");
			
			myOutWriter.flush();
			myOutWriter.close();
			
			fOut.close();
			
			Process proc = Runtime.getRuntime().exec("sh /storage/sdcard0/project/modifydns.sh");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			sucess = false;
			e.printStackTrace();
		}
		return sucess;
		// 
	}
	public static void StopTcpdump()
	//public static void KernelConfigure()
	{
		int pid = GetTcpdumpPID();
		if(pid != -1)
		{
			
			File sdcard = Environment.getExternalStorageDirectory();
			File dir = new File (sdcard.getAbsolutePath() + "/project");
			if(!dir.exists())
			{
				dir.mkdirs();
			}
			
			File file = new File(dir, "stopdump.sh");
			try {
				file.createNewFile();
				FileOutputStream fOut = new FileOutputStream(file);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				myOutWriter.append("#!/bin/sh\n");
				
				//find tcpdump
				String tcpdump = "su -c \"kill " + pid+"\"\n";
				
				myOutWriter.append(tcpdump);
				myOutWriter.flush();
				myOutWriter.close();	
				fOut.close();
				
				Thread.sleep(Common.sleeptime); //sleep for a while before kill
				
				Runtime rt = Runtime.getRuntime();
				Process proc = rt.exec("sh /storage/sdcard0/project/stopdump.sh");
				proc.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 
	}


	
	
	public static int GetTcpdumpPID()
	{
		/* get the pid of the process in which we exec tcpdump  
		* to do that we use the ps command, so we need to launch another process to achieve that  
		*/  
		Process process2;
		int pid =-1;
		try {
			/*
			Process process = Runtime.getRuntime().exec("ps tcpdump");
			
			BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            // Waits for the command to finish.
            process.waitFor();

            Log.d(Common.TAG, output.toString());
            */
            process2 = Runtime.getRuntime().exec("ps tcpdump");
			DataInputStream in = new DataInputStream(process2.getInputStream());
			//DataOutputStream in = new DataOutputStream(process2.getOutputStream());
			String temp= in.readLine();  
			//Log.d(TAG,"first " + temp);
			temp = in.readLine();
			if(temp != null)
			{
				Log.d(TAG,"second" +temp);
				//We apply a regexp to the second line of the ps output to get the pid  
				//temp = temp.replaceAll("^root *([0-9]*).*","$1");
				String[] cmds = temp.split(" ");
				for(int i = 0; i < cmds.length; i ++)
				{
					if (cmds[i].matches("[0-9]+"))
					{	
						pid = Integer.valueOf(cmds[i]);
						Log.d(TAG, Integer.toString(i)+": "+cmds[i]);
						break;
					}
				}
			}
			//pid = Integer.parseInt(temp);  
			//the ps process is no more needed  
			process2.destroy();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}

		//read the output of ps  
		
		
		return pid;
	}
	

	public static void printLog(InputStream is)
	{
		//InputStream is = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        try {
			while ((line = br.readLine()) != null)
			{
				Log.d(TAG, "currentline: "+ line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
