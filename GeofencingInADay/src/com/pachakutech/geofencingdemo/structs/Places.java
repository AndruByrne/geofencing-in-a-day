package com.pachakutech.geofencingdemo.structs;
import java.util.List;
import com.google.api.client.util.Key;
public class Places {
    @Key
    public String status;

    @Key
    public List<Place> results;

    @Override
    public String toString() {
        return status + "\n" + results.toString();
    }
}
