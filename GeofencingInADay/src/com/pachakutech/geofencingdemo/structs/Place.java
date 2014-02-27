package com.pachakutech.geofencingdemo.structs;

public class Place {

	public String id;

	public String name;

	public String reference;

	public String icon;

	public String vicinity;

	public Geometry geometry;

	public String formatted_address;

	public String formatted_phone_number;

	@Override
	public String toString() {
		return name + " - " + id + " - " + reference;
	}

	public static class Geometry 
	{
		public Location location;
	}

	public static class Location 
	{
		public double lat;

		public double lng;
	}

}
