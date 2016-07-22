package com.example.tcptest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
 
public class VideoPlay extends Activity {
 
//String SrcPath = "rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp";
	private String SrcPath = "http://128.125.121.205/10M.mp4";
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       final Context context = this;
       setContentView(R.layout.video);
       VideoView myVideoView = (VideoView)findViewById(R.id.myvideoview);
       myVideoView.setVideoURI(Uri.parse(SrcPath));
       myVideoView.setMediaController(new MediaController(this));
       myVideoView.requestFocus();
       myVideoView.start();
       
       Button sw  = (Button)findViewById(R.id.change);
       sw.setOnClickListener(new View.OnClickListener() {
           //Handler handler = new Handler();
           @Override
           public void onClick(View v) {
           	 Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent); 
           }
       });
       
   }
}
