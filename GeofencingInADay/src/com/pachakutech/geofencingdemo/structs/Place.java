package com.pachakutech.geofencingdemo.structs;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.Key;
import android.location.Address;

public class Place {
    @Key
    public String id;

    @Key
    public String name;

    @Key
    public String reference;

    @Key
    public List<Address> address_components;

    @Key
    public Geometry geometry;

    @Key
    public String vicinity;

    @Override
    public String toString() {
        if(address_components == null ) {
            return name + "\n" + geometry.toString() + "\n" + vicinity;
        } else {
            return name + "\n" + geometry.toString() + "\n" + vicinity + "\n" + address_components.toString();
        }
    }
	
}
