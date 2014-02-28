package com.pachakutech.geofencingdemo.activities;

import com.pachakutech.geofencingdemo.*;
import com.pachakutech.geofencingdemo.structs.*;
import com.pachakutech.geofencingdemo.interfaces.*;
import com.pachakutech.geofencingdemo.adapters.*;
import com.pachakutech.geofencingdemo.utils.*;
import android.app.Activity;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.view.View;
import java.util.Map;
import java.util.List;
import retrofit.RestAdapter;
import android.widget.AdapterView.*;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import java.util.HashMap;
import java.util.ArrayList;
import android.util.*;
import java.net.*;
import java.io.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.*;


public class AddAnchor extends Activity implements LocationListener, SpinMaster {

	//intent from web
	Intent intent;
	//link to store with coordinates
	EditText editLink;
	//Filter to apply to places API
	EditText editFilter;
	//label to reference anchor by
	EditText editLabel;
	//coordinates of phone or place selected
	TextView latView;
	TextView lngView;
	//parameters for place query
	Map<String, String> placeParameters	= new HashMap<String, String>( );
	//array of places filtered by keyword
	List<Place> places = new ArrayList<Place>( );
	//spinner of places filtered by keyword
	Spinner placesSpinner;
	//adapter for spinner
	private PlacesSpinnerAdapter placesSpinnerAdapter;
	//serialization of Geofences
	private SharedPreferences anchorHash;
	//location
	private Location location;
	//location manager for this activity
	LocationManager locationManager;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		intent = getIntent( );

		//layout
		setContentView( R.layout.add_anchor );
		editLink = (EditText)findViewById( R.id.edit_link );
		editLabel = (EditText)findViewById( R.id.edit_label );
		editFilter = (EditText)findViewById( R.id.edit_filter );
		placesSpinner = (Spinner)findViewById( R.id.placesSpinner );
		latView = (TextView)findViewById( R.id.lat_view );
		lngView = (TextView)findViewById( R.id.lng_view );

		editLink.setHint( intent.getStringExtra( Intent.EXTRA_TEXT ) );
		editLabel.setHint( intent.getStringExtra( Intent.EXTRA_SUBJECT ) );

		anchorHash = getSharedPreferences( getString( R.string.anchor_hash ), MODE_PRIVATE );
		locationManager = (LocationManager) this.getSystemService( Context.LOCATION_SERVICE );	
		location = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
		//spinner adapter
		placesSpinnerAdapter = new PlacesSpinnerAdapter( this, android.R.layout.simple_spinner_dropdown_item, places );
		placesSpinner.setAdapter( placesSpinnerAdapter );
	}

	@Override
	protected void onResume( ) {
		locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, this );
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this );
		super.onResume( );
	}

	@Override
	protected void onPause( ) {
		locationManager.removeUpdates( this );
		super.onPause( );
	}

	@Override
	public void spinnerItemSelected( Place place ) {
		Toast.makeText( this, "Item " + place.name + " selected", Toast.LENGTH_LONG ).show( );
		editLabel.setText( place.name );
		latView.setText( Double.toString( place.geometry.location.lat ) );
		latView.refreshDrawableState( );
		lngView.setText( Double.toString( place.geometry.location.lng ) );	
	}

	public void applyFilter( View v ) {
		if( editFilter.getText( ).toString( ).trim( ).length( ) != 0 ) {
			new GetPlaces( ).execute( );			
		}
	}

	public void addGeofence( View v ) {
		locationManager.removeUpdates( this );
		SharedPreferences.Editor editor = anchorHash.edit( );
		
		editor.putString( editLabel.getText().toString().trim().length() == 0 ? 
		                  intent.getStringExtra( Intent.EXTRA_SUBJECT ) : editLabel.getText( ).toString( ), 
						  Utils.catOnGeofence( latView.getText( ).toString( ), 
						      lngView.getText( ).toString( ), 
					          editLink.getText().toString().trim().length()==0 ?
							  intent.getStringExtra( Intent.EXTRA_TEXT ) : editLink.getText().toString() ) );
		editor.commit( );
		Toast.makeText( this, getString( R.string.added_geofence ) + editLabel.getText( ).toString( ), Toast.LENGTH_SHORT ).show( );
	}

	private class GetPlaces extends AsyncTask<Void, Void, Places> {

		@Override
		protected Places doInBackground( Void[] p1 ) {
			GoogleClient client = new GoogleClient( );
			try {
				return client.executePlacesSearchGet(
					URLEncoder.encode( editFilter.getText( ).toString( ), "UTF-8" ), 
					getString( R.string.places_api_key ),
					Utils.latStringFromLocation( location ) + "," + Utils.lngStringFromLocation( location ), 
					50 );
			} catch(UnsupportedEncodingException e) {return null;}
		}

		@Override
		protected void onPostExecute( Places result ) {
			if( result != null ) {
				super.onPostExecute( result );
				places.clear( );
				places.addAll( result.results );
				placesSpinnerAdapter.notifyDataSetChanged( );
				placesSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener( ){

						public void onItemSelected( AdapterView<?> adapterView, View view, int position, long id ) {
							AddAnchor.this.spinnerItemSelected( places.get( position ) );
						}

						public void onNothingSelected( AdapterView<?> p1 ) {
						}
					} );
				placesSpinner.performClick( );

			} else Log.e( "TAG", "empty response!" );
		}

	}

	@Override
	public void onLocationChanged( Location location ) {
		if( Utils.isBetterLocation( location, this.location ) ) {
			this.location = location;
			if( editFilter.getText( ).toString( ).trim( ).length( ) == 0 ) {
				latView.setText( Utils.latStringFromLocation( this.location ) );
				lngView.setText( Utils.lngStringFromLocation( this.location ) );
				placeParameters.put( getString( R.string.location ), 
									Utils.latStringFromLocation( location ) +
									"," + Utils.lngStringFromLocation( location ) );
			}
		}
	}

	public void onProviderDisabled( String string ) {}

	public void onStatusChanged( String string, int i, Bundle bundle ) {}

	public void onProviderEnabled( String string ) {}

	//need keywords and search button
	//should be a spinner list of places
	//Label for notification (and list key) editable, 
	//but based on place selection
	//location also based on places and located at bottom

	//Locationwork -- to be moved into a util class



}
