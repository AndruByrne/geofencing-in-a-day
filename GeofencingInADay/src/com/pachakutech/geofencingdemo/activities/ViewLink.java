package com.pachakutech.geofencingdemo.activities;
import android.app.*;
import android.webkit.*;
import com.pachakutech.geofencingdemo.*;
import android.os.*;
import android.content.*;

public class ViewLink extends Activity
{

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView(R.layout.view_link);
		Intent notificationIntent = new Intent(this, ViewLink.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.link_to_geofence, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);		
		WebView webView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.loadUrl(this.getIntent().getExtras().getString("targetUrl"));
	}
	
}
