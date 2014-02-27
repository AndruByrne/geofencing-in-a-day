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

public class AnchorList extends ListActivity
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
    private Map<String, String> anchors;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
//		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);		
		anchorHash = getSharedPreferences(getString(R.string.anchor_hash), MODE_PRIVATE);
		anchors = (HashMap<String, String>) anchorHash.getAll();
		arrayAdapter = new AnchorListAdapter(this, anchors.keySet());
		setListAdapter(arrayAdapter);
    }
	
	@Override
	public void onResume(){
		anchors = (Map<String, String>) anchorHash.getAll();
		arrayAdapter.notifyDataSetChanged();
		super.onResume();
	
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
				anchors.remove(listView.getItemAtPosition(position));
				arrayAdapter.notifyDataSetChanged();
			}
		} );
		builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
				public void onClick( DialogInterface di, int i ){
				}
			} );
		
	}
	
	public void addAnchor(){
		//toast instruction to share webpage with app
		startActivity(new Intent(Intent.ACTION_VIEW, 
		                         Uri.parse(getString(R.string.google_url))));	}
	
	@Override
	public void onPause(){
		SharedPreferences.Editor editor = anchorHash.edit();
		for( String s: anchors.keySet()){
			editor.putString(s, anchors.get(s));
			editor.commit();
		}
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
