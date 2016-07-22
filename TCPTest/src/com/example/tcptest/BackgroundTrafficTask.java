package com.example.tcptest;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class BackgroundTrafficTask extends AsyncTask<String, Void, Void>{
	private final static int Buffer = 8 * 1024;
	private String downloadFile;
	private String serverIP;
	private int PORT;
	//private DownloadAsynTask downtask;
	@Override
	protected Void doInBackground(String... params) {
		try {
	       	 downloadFile = params[0];
	       	 serverIP = params[1];
	       	 PORT = Integer.parseInt(params[2]);
	      // 	Log.d("==", "backgound here: " + downloadFile+", "+ serverIP +", "+params[2]);
	       	 Socket socket = new Socket(serverIP, PORT);
	   	     download(downloadFile, socket);
	   	     socket.close();
	   		} catch (UnknownHostException e) {
	   	        // TODO Auto-generated catch block
	   	        e.printStackTrace();
	   		} catch (IOException e) {
	   	        // TODO Auto-generated catch block
	   	        e.printStackTrace();
	   		} catch (Exception e) {
	           e.printStackTrace();
	       }		
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
	    //super.onPostExecute(result);
	}
	// download request, return file size
    private long request(String fileName, String password, Socket socket) throws IOException {
        // get socket input stream; DataInputStream
        DataInputStream in = new DataInputStream(socket.getInputStream());
        //get socket outputstream PrintWriter
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()));
        // request string
        String requestString = fileName + "@ " + password;
        out.println(requestString); // send request
        out.flush();
        return in.readLong(); // accept returns
        
    }
    // accept and save file
    private void receiveFile(String localFile, Socket socket) throws Exception {
        //get socket input stream; BufferedInputStream
    	Log.d(Common.TAG, "begin down ");
        BufferedInputStream in = new BufferedInputStream(
                socket.getInputStream());
        // get local inputstream
        Log.d(Common.TAG,"first location"+localFile);
        
        FileOutputStream out = new FileOutputStream(localFile);
        byte[] buf = new byte[Buffer];
        int len;
       // len = in.read(buf);
        //Log.d("==", "length: "+ String.valueOf(len));
        // reading content until finished
        while ((len = in.read(buf)) >= 0) {
            //Log.d("==", "length "+ String.valueOf(len));
        	out.write(buf, 0, len); // write 
            out.flush();
            if(isCancelled())
            {
            	Log.d(Common.TAG, "canceled the task");
            	break;
            }
        }
        out.close();
        in.close();
    }
    // download file from server
    public void download(String downloadFile, Socket socket) throws Exception {
        try {
            String password = "password";
           
            // String downloadFile ="imissyou.mp3";
            String localpath = "/sdcard/project/download/";
            File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"project/download");
            if(!directory.isDirectory())
            {
            	directory.mkdir();
            }
            String localFile = localpath + downloadFile;
            long fileLength = request(downloadFile, password, socket);
           // Log.d("==","fileLength: " + fileLength + " B");
            // if file len > 0, allow download
            if (fileLength >= 0) {
            	Log.d(Common.TAG,"fileLength: " + fileLength + " B");
            //	Log.d("==","downing to ..."+localFile);
                receiveFile(localFile, socket); // accept file and save to local
                Log.d(Common.TAG,"file: " + downloadFile + " had save to "
                        + localFile);
            } else {
            	Log.d(Common.TAG,"download " + downloadFile + " error! ");
            	
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
           // socket.close(); // 
        }
    }
}
