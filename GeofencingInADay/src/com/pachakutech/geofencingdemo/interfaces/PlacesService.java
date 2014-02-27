package com.pachakutech.geofencingdemo.interfaces;
import retrofit.http.GET;
import retrofit.http.QueryMap;
import java.util.List;
import java.util.Map;
import com.pachakutech.geofencingdemo.structs.*;

public interface PlacesService{
	@GET("/maps/api/place/nearbysearch/json")
	List<Place> listPlaces(@QueryMap Map<String, String> options);
}
