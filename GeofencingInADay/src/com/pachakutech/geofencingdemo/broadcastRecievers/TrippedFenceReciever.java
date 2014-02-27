package com.pachakutech.geofencingdemo.broadcastRecievers;
import android.content.BroadcastReceiver;
import android.content.*;
import android.app.NotificationManager;
import android.app.Notification;
import android.os.Bundle;
import android.app.PendingIntent;
import com.pachakutech.geofencingdemo.*;
import com.pachakutech.geofencingdemo.activities.*;

public class TrippedFenceReciever extends BroadcastReceiver
{

	private Context context;
	
	private Intent intent;

	@Override
	public void onReceive( Context context, Intent intent ) {
		this.context = context;
		this.intent = intent;
		NotifyUser();
	}

	private void NotifyUser( ) {
		NotificationManager mgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "String from places.xml here";
		long when = System.currentTimeMillis();

		//make notification details
		Notification notification = new Notification(icon, tickerText, when);
		Intent notificationIntent = new Intent(context, ViewLink.class); 
		CharSequence contentTitle = context.getString(R.string.app_name);
		CharSequence contentText = context.getString(R.string.view_link_at);

		notificationIntent.putExtras(intent.getExtras());

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.vibrate = GeoFencingInADay.VIBRATE;

		//calling R.string gives an int
		mgr.notify(R.string.you_are_near, notification);
		
	}

}
