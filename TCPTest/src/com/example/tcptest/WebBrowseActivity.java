package com.example.tcptest;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebBrowseActivity extends Activity {

	private WebView mWebview ;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mWebview  = new WebView(this);
        
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;

        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });
        Bundle extras = getIntent().getExtras();
        String url = extras.getString(Common.URL);
        if(url != null)
        {
        	mWebview.loadUrl(url);	
        }else
        {
        	mWebview.loadUrl("http://www.nytimes.com");	
        }
        setContentView(mWebview );

    }
    @Override
    public void onDestroy() {
    	mWebview.clearCache(true);
    	mWebview.clearFormData();
    //    GCMRegistrar.unregister(this); 
        super.onDestroy();
    }

}
