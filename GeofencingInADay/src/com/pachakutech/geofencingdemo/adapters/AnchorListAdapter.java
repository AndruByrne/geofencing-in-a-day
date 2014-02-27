package com.pachakutech.geofencingdemo.adapters;
import android.widget.ListAdapter;
import android.database.*;
import android.view.*;
import java.util.Map;
import java.util.*;
import android.content.Context;
import android.widget.*;
import com.pachakutech.geofencingdemo.*;

public class AnchorListAdapter extends ArrayAdapter<String>
{

	private final Object[] anchorNames;
	public AnchorListAdapter(Context context, Set set){
		super(context, R.layout.row, set.toArray());
		anchorNames = set.toArray();
	}

	@Override
	public String getItem( int i ) {
		return anchorNames[i].toString();
	}

	
}
