package com.pachakutech.geofencingdemo.services;
import android.app.IntentService;
import android.content.*;
import android.app.NotificationManager;
import com.pachakutech.geofencingdemo.*;
import com.pachakutech.geofencingdemo.activities.*;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.*;
import android.location.*;
import com.pachakutech.geofencingdemo.utils.*;
import java.util.List;
import java.util.*;
import android.util.*;

public class FenceMonitor extends IntentService implements LocationListener
{

	//loc manager
	LocationManager locationManager;
	//location
	Location location;
	Map<Location, String> geoAnchors = new HashMap<Location, String>( );
	Map anchors;
	
	public FenceMonitor(){
		super("FenceMonitor");
	}
	
	@Override
	protected void onHandleIntent( Intent p1 ) 
	{
		locationManager = (LocationManager) this.getSystemService( Context.LOCATION_SERVICE );
		location = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
		locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, this );
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this );
		
		anchors = (HashMap<String, String>) getSharedPreferences( getString( R.string.anchor_hash ), MODE_PRIVATE ).getAll();
		String[] handlingArray = new String[3];
		for( String k : anchors.keySet() ){
			handlingArray = Utils.convertStringToArray(anchors.get(k).toString());
			geoAnchors.put(Utils.locationFromStrings(handlingArray[0], handlingArray[1]), handlingArray[2]+","+k );
			Log.i("TAG", "k= "+k+" and website= "+handlingArray[2]);
		}
		if(geoAnchors != null)
		new SeeIfNear().execute();
	}
	
	private class SeeIfNear extends AsyncTask<Void, Void, Boolean> {

		private static final long LOCATION_SLEEPY_TIME = 20000;
		private static final int FIVE_MILES_IN_METERS = 5 * 1609;

		private float distance;
		private float bestDistance = 2*5*1609;
		private Location winningLocation = null;


		@Override
		protected Boolean doInBackground( Void[] p1 ) {
			try {
				Thread.sleep( LOCATION_SLEEPY_TIME );
			} catch(InterruptedException e) {}
			return true;
		}

		@Override
		protected void onPostExecute( Boolean b ) {
			super.onPostExecute( b );
		    final Location currentLocation = FenceMonitor.this.getCurrentLocation();
			for(Location loc : geoAnchors.keySet()){
				distance = currentLocation.distanceTo(loc);
				if( distance < FIVE_MILES_IN_METERS ){
					if( distance < bestDistance ) {
						winningLocation = loc;
						bestDistance = distance;	
					}
				}
			}
			if( winningLocation != null) 
				NotifyUser( winningLocation );
			endThis();
		}
	}
	
	@Override
	Location getCurrentLocation(){
		return location;
	}
	
	public void endThis(){
		locationManager.removeUpdates( this );
		locationManager = null;
	}
	
	@Override
	public void onLocationChanged( Location location ) {
		if( Utils.isBetterLocation( location, this.location ) ) {
			this.location = location;
			}
	}

	private void NotifyUser( Location location ) {
		String[] handlingUrl = new String[2];
//		Log.i("TAG LOcation : ", location.toString());
//		Log.i("TAG number : ", "number: "+anchors.keySet().size());
//		Log.i("TAG keys : ", anchors.keySet().toArray()[0].toString());
		handlingUrl = Utils.convertStringToArray(geoAnchors.get(location).toString());
		NotificationManager mgr = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = handlingUrl[1];
		long when = System.currentTimeMillis();

		//make notification details
		Notification notification = new Notification(icon, tickerText, when);
		Intent notificationIntent = new Intent(this, ViewLink.class);
		notificationIntent.putExtra("targetUrl", handlingUrl[0]);
		CharSequence contentTitle = this.getString(R.string.app_name);
		CharSequence contentText = this.getString(R.string.view_link_at ) + handlingUrl[0];

		PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.link_to_geofence, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		
		notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
		notification.vibrate = GeoFencingIn48Hrs.VIBRATE;

		mgr.notify(R.string.you_are_near, notification);

	}
	
	@Override
	public void onStatusChanged( String p1, int p2, Bundle p3 ) {
		// TODO: Implement this method
	}

	@Override
	public void onProviderEnabled( String p1 ) {
		// TODO: Implement this method
	}

	@Override
	public void onProviderDisabled( String p1 ) {
		// TODO: Implement this method
	}
	
}
