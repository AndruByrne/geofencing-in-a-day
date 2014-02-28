package com.pachakutech.geofencingdemo.broadcastRecievers;
import android.content.BroadcastReceiver;
import android.content.*;
import android.app.NotificationManager;
import android.app.Notification;
import android.os.Bundle;
import android.app.PendingIntent;
import com.pachakutech.geofencingdemo.*;
import com.pachakutech.geofencingdemo.activities.*;
import android.app.AlarmManager;
import java.util.Calendar;
import com.pachakutech.geofencingdemo.services.*;

public class TrippedFenceReciever extends BroadcastReceiver
{

	private Context context;
	
	@Override
	public void onReceive( Context context, Intent intent ) {
		this.context = context;
		
		//set up next instance
		if(GeoFencingIn48Hrs.repeat){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, GeoFencingIn48Hrs.TIME_TO_CHECK_SMALL);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, R.string.removable_geofences, new Intent(context, TrippedFenceReciever.class), PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
		}
		
		Intent newIntent = new Intent(context, FenceMonitor.class);
		context.startService(newIntent);
		
	}


}
