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

	private final List<String> anchorNames;
	public AnchorListAdapter(Context context, int textviewId, List<String> anchors){
		super(context, textviewId, anchors);
		anchorNames = anchors;
	}

	@Override
	public String getItem( int i ) {
		return anchorNames.get(i).toString();
	}

	
}
