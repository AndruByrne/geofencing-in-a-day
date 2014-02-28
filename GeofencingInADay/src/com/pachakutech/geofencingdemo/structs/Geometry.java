package com.pachakutech.geofencingdemo.structs;

import com.google.api.client.util.Key;

public class Geometry {
    @Key
    public Location location;

    @Override
    public String toString() {
        return location.toString();
    }

}
