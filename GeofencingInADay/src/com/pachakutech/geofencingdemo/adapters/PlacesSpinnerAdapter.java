package com.pachakutech.geofencingdemo.adapters;
import android.widget.SpinnerAdapter;
import android.database.*;
import android.view.*;
import android.content.Context;
import java.util.List;
import android.widget.TextView;
import android.graphics.Color;
import com.pachakutech.geofencingdemo.structs.*;
import android.widget.ArrayAdapter;

public class PlacesSpinnerAdapter extends ArrayAdapter {

	private Context context;

	private List<Place> places;

	public PlacesSpinnerAdapter( Context context, int textViewResourceId, List<Place> places ) {
		super(context, textViewResourceId, places);
		this.context = context;
		this.places = places;
	}

	@Override
	public int getCount( ) {
		if( places != null )
		return places.size();
		else return 0;
	}

	@Override
	public Object getItem( int p1 ) {
		return places.get(p1).name;
	}

//	@Override
//	public long getItemId( int p1 ) {
//		// TODO: Implement this method
//		return 0;
//	}
//
//	@Override
//	public boolean hasStableIds( ) {
//		// TODO: Implement this method
//		return false;
//	}
//
	@Override
	public View getView( int p1, View p2, ViewGroup p3 ) {
		TextView textView = new TextView(context);
        textView.setText(places.get(p1).name);
        return textView;
	}

//	@Override
//	public int getItemViewType( int p1 ) {
//		// TODO: Implement this method
//		return 0;
//	}
//
//	@Override
//	public int getViewTypeCount( ) {
//		// TODO: Implement this method
//		return 0;
//	}
//
	@Override
	public boolean isEmpty( ) {
		return places.size()==0;
	}

//	@Override
//	public View getDropDownView( int p1, View p2, ViewGroup p3 ) {
//		TextView textView = new TextView(context);
//        textView.setText(places.get(p1).name);
//        return textView;
//	}
//
}
