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

public class AddAnchor extends Activity implements LocationListener {

	//link to store with coordinates
	EditText editLink;
	//Filter to apply to places API
	EditText editFilter;
	//label to reference anchor by
	EditText editLabel;
	//coordinates of phone or place selected
	TextView latView;
	TextView lngView;
	//places service
	PlacesService placesService; 
	//parameters for place query
	Map<String, String> placeParameters;
	//button for place query
	ImageButton queryPlace;
	//button for adding geofence
	Button addGeofence;
	//array of places filtered by keyword
	List<Place> places;
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
		Intent intent = getIntent( );

		//layout
		setContentView( R.layout.add_anchor );
		editLink = (EditText)findViewById( R.id.edit_link );
		editLabel = (EditText)findViewById( R.id.edit_label );
		editFilter = (EditText)findViewById( R.id.edit_filter );
		queryPlace = (ImageButton)findViewById( R.id.placeQuery );
		addGeofence = (Button)findViewById( R.id.addGeofence );
		placesSpinner = (Spinner)findViewById( R.id.placesSpinner );
		latView = (TextView)findViewById( R.id.lat_view );
		lngView = (TextView)findViewById( R.id.lng_view );

		editLink.setHint( intent.getStringExtra( Intent.EXTRA_TEXT ) );
		editLabel.setHint( intent.getStringExtra( Intent.EXTRA_SUBJECT ) );
		queryPlace.setClickable( false );
		addGeofence.setClickable( false );

		anchorHash = getSharedPreferences( getString( R.string.anchor_hash ), MODE_PRIVATE );
		locationManager = (LocationManager) this.getSystemService( Context.LOCATION_SERVICE );	
		location = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );

		//rest adapter
		RestAdapter restAdapter = new RestAdapter.Builder( )
		    .setEndpoint( getString( R.string.google_api_endpoint ) )
		    .build( );
		placesService = restAdapter.create( PlacesService.class );
		placeParameters = new HashMap<String, String>();
		placeParameters.clear( );
		placeParameters.put( getString( R.string.sensor ), getString( R.string.places_api_sensor ) );
		placeParameters.put( getString( R.string.radius ), getString( R.string.places_api_radius ) );
		placeParameters.put( getString( R.string.key ), getString( R.string.places_api_key ) );
        placeParameters.put( getString( R.string.keyword ), "" );
		placeParameters.put( getString( R.string.location ), 
		                         Utils.latStringFromLocation(location)+
		                     ","+Utils.lngStringFromLocation(location));

		//spinner adapter
		placesSpinnerAdapter = new PlacesSpinnerAdapter( this, places );
		placesSpinner.setAdapter( placesSpinnerAdapter );
		placesSpinner.setOnItemSelectedListener( new OnItemSelectedListener( ){

				@Override
		        public void onItemSelected( AdapterView adapterView, View view, int i, long l ) {
					Place place = places.get( i );
					editLabel.setText( place.name );
    				latView.setText( Double.toString( place.geometry.location.lat ) );
				    lngView.setText( Double.toString( place.geometry.location.lng ) );
				}

				@Override
				public void onNothingSelected( AdapterView<?> p1 ) {
				}
			} );

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



	public void applyFilter( View v ) {
		Toast.makeText(this, "boo", Toast.LENGTH_SHORT).show();
		
		if( editFilter.getText( ).toString( ).trim( ).length( ) != 0 ) {
			placeParameters.put( getString( R.string.keyword ), editFilter.getText( ).toString( ) );
	        new GetPlaces( ).execute( );
		}
	}

	public void addGeofence( View v ) {
		locationManager.removeUpdates( this );
		SharedPreferences.Editor editor = anchorHash.edit( );
		editor.putString(editLabel.getText().toString(), Utils.catOnGeofence( latView.getText().toString(), lngView.getText().toString(),editLink.getText().toString() ) );
		editor.commit( );
		Toast.makeText(this, editLabel.getText().toString()+"boo", Toast.LENGTH_SHORT).show();
	}

	private class GetPlaces extends AsyncTask<Void, Void, List<Place>> {

		@Override
		protected List<Place> doInBackground( Void[] p1 ) {
			return placesService.listPlaces( placeParameters );
		}

		@Override
		protected void onPostExecute( List<Place> result ) {
			places.clear( );
			places.addAll( result );
			placesSpinnerAdapter.notifyDataSetChanged( );
    		super.onPostExecute( result );
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
									Utils.latStringFromLocation(location)+
									","+Utils.lngStringFromLocation(location));
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
