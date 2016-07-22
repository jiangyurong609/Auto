package com.example.tcptest;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;

public class DownloadAsyncTask extends AsyncTask<String, Boolean, Boolean>{
	private final static int Buffer = 8 * 1024;
	private String downloadFile;
	private String serverIP;
	private int PORT;
	private BackgroundTrafficTask backgroundtraffic;
	
	private boolean background = false;
	private long waittime = 6000;
    private EditText et_waittime = null; 
    private String [] parameters = new String[3];
	@Override
	protected Boolean doInBackground(String... params) {
		/*
		if(params.length == 4)
		{
			try {
		       	 downloadFile = params[0];
		       	 serverIP = params[1];
		       	 PORT = Integer.parseInt(params[2]);
		       	
		       	 Socket socket = new Socket(serverIP, PORT);
		   	     GetMsg(params[3], socket);
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
	       	 
		}
		else
		{
		*/
		try {
	       	 downloadFile = params[0];
	       	 serverIP = params[1];
	       	 PORT = Integer.parseInt(params[2]);
	       	 //String pcapname = params[3];
	       	 Socket socket = new Socket(serverIP, PORT);
	   	     download(downloadFile,params[3], socket);
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
		
		//}
		return background;
	}
	protected void onProgressUpdate(Boolean... progress)
	{
		if(progress[0])
		{	
			//DownloadThread down = new DownloadThread(parameters[0], parameters[1], Integer.valueOf(parameters[2]));
			//down.start();
			backgroundtraffic = new BackgroundTrafficTask();
			backgroundtraffic.execute(parameters);
			Log.d(Common.TAG, parameters[0]+", "+parameters[1] +", "+parameters[2]);
			Log.d(Common.TAG, "started background traffic");
		}
	}
	@Override
	protected void onPostExecute(Boolean result) {
		if(result)
		{
			backgroundtraffic.cancel(true);
		}
	    //super.onPostExecute(result);
	}

	// download request, return file size
    private long request(String fileName, String pcapfile, Socket socket) throws IOException {
        // get socket input stream; DataInputStream
        DataInputStream in = new DataInputStream(socket.getInputStream());
        //get socket outputstream PrintWriter
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()));
        // request string
        String requestString = fileName + "@ " + pcapfile;
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
    
    // accept msg
    // accept and save file
    private void receiveMsg( Socket socket) throws Exception {
        //get socket input stream; BufferedInputStream
    	Log.d(Common.TAG, "begin down ");
        BufferedInputStream in = new BufferedInputStream(
                socket.getInputStream());
        // get local inputstream
       // Log.d("==","first location"+localFile);

        //FileOutputStream out = new FileOutputStream(localFile);
        byte[] buf = new byte[Buffer];
        int len;
       // len = in.read(buf);
        //Log.d("==", "length: "+ String.valueOf(len));
        // reading content until finished
        String content ="";
        while ((len = in.read(buf)) >= 0) {
            //Log.d("==", "length "+ String.valueOf(len));
        	content += new String(buf, 0, len); // write 
            
        }
        Log.d("==", "MSG content: "+ content);
        in.close();
    }
    // download file from server
    public void GetMsg(String size, Socket socket) throws Exception {
        try {
            //String password = "password";
           
            // String downloadFile ="imissyou.mp3";
          
            //long fileLength = request("msg", size, socket);
        	 PrintWriter out = new PrintWriter(new OutputStreamWriter(
                     socket.getOutputStream()));
             // request string
             String requestString = "msg" + "@ " + size;
             out.println(requestString); // send request
             out.flush();
             
             receiveMsg( socket); // accept file and save to local
             
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
           // socket.close(); // 
        }
    }
    // download file from server
    public void download(String downloadFile, String pcapfile, Socket socket) throws Exception {
        try {
            //String password = "password";
           
            // String downloadFile ="imissyou.mp3";
            String localpath = "/sdcard/project/download/";
            File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"project/download");
            if(!directory.isDirectory())
            {
            	directory.mkdir();
            }
            String localFile = localpath + downloadFile;
            long fileLength = request(downloadFile, pcapfile, socket);
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
