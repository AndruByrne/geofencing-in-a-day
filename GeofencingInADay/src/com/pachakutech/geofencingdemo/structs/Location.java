package com.pachakutech.geofencingdemo.structs;
import com.google.api.client.util.*;

public class Location {
	@Key
	public double lat;

	@Key
	public double lng;

	@Override
	public String toString() {
		return Double.toString(lat) + ", " + Double.toString(lng);
	}
}

