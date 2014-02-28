package com.pachakutech.geofencingdemo.structs;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

/**
 * Generic URL contains all the specific regarding URLS and
 * proper encoding and such.
 *
 * This uses the fields from the Places Search JSON response
 * @author dannyperez
 *
 */
public class PlacesSearchUrl extends GenericUrl {
    private static final String PLACES_API = "https://maps.googleapis.com/maps/api/place/search/json?";

    public PlacesSearchUrl() {
        super(PLACES_API);
    }

    @Key
    public String key;

    @Key
    public String location;

    @Key
    public int radius;

    @Key
    public boolean sensor;

    @Key
    public String keyword;

}
