package com.pachakutech.geofencingdemo.activities;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.pachakutech.geofencingdemo.*;
import com.pachakutech.geofencingdemo.adapters.*;
import android.content.*;
import java.util.Map;
import java.util.Set;
import android.view.inputmethod.*;
import android.net.Uri;
import android.location.LocationManager;
import com.pachakutech.geofencingdemo.utils.*;
import java.util.HashMap;
import android.util.Log;
import com.pachakutech.geofencingdemo.interfaces.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import com.pachakutech.geofencingdemo.broadcastRecievers.*;

public class AnchorList extends ListActivity implements RemoveableGeofences
{

    //list of anchors at onClick
	private ListView listView;
	//position at onClick
	private int position;
	//cursor for anchor list
	private AnchorListAdapter arrayAdapter;
	//serialized anchors
	private SharedPreferences anchorHash;
	//anchor names and LatLngs
    private Map<String, String> anchors = new HashMap<String, String>();
	//list of anchor names
	ArrayList<String> anchorList = new ArrayList<String>();
	
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
//		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);		
		anchorHash = getSharedPreferences(getString(R.string.anchor_hash), MODE_PRIVATE);
		arrayAdapter = new AnchorListAdapter(this, android.R.layout.simple_list_item_1, anchorList);
		setListAdapter(arrayAdapter);
    }
	
	@Override
	public void onResume(){
		super.onResume();
		anchors = (HashMap<String, String>) anchorHash.getAll();
		anchorList.clear();
		anchorList.addAll(anchors.keySet());
		arrayAdapter.notifyDataSetChanged();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, GeoFencingIn48Hrs.TIME_TO_CHECK_BIG);
	        
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), R.string.removable_geofences, new Intent(this, TrippedFenceReciever.class), PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
		
	}

	@Override
	protected void onListItemClick( ListView l, View v, int p, long id ) {
		super.onListItemClick( l, v, position, id );
		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		listView = l;
		position = p;
		builder.setMessage(R.string.confirm_anchor_delete);
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
			public void onClick( DialogInterface di, int i ){
				AnchorList.this.removeGeofence(listView.getItemAtPosition(position).toString());
			}
		} );
		builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
				public void onClick( DialogInterface di, int i ){
				}
			} );
		builder.show();
	}
	
	@Override
	public void removeGeofence( String string ) {
		anchors.remove(string);
		anchorList.clear();
		anchorList.addAll(anchors.keySet());
		arrayAdapter.notifyDataSetChanged();
	}
	
	public void addAnchor(){
		Toast.makeText(this, getString( R.string.adding_geofence ), Toast.LENGTH_LONG).show();
		//toast instruction to share webpage with app
		startActivity(new Intent(Intent.ACTION_VIEW, 
		                         Uri.parse(getString(R.string.google_url))));	}
	
	@Override
	public void onPause(){
		SharedPreferences.Editor editor = anchorHash.edit();
		editor.clear();
		for( String s: anchors.keySet()){
			editor.putString(s, anchors.get(s));
		}
		editor.commit();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu( menu );
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch(item.getItemId()){
			case R.id.action_add:
				addAnchor();
			return true;
			default:
				return super.onOptionsItemSelected( item );
				
		}		
	}
	
}
