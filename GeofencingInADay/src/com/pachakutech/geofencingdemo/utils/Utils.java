package com.pachakutech.geofencingdemo.utils;
import android.location.Location;
import android.location.LocationManager;

public class Utils
{

	private static final long TWO_MINUTES = 1000 * 60 * 2;
	
	public static String latStringFromLocation(final Location location) {
		return Location.convert(location.getLatitude(), Location.FORMAT_DEGREES); 
	}
	public static String lngStringFromLocation(final Location location) {
		return Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
	}
	
	public static Location locationFromStrings(final String lat, final String lng){
		Location location = new Location(LocationManager.NETWORK_PROVIDER);
		location.setLatitude(Location.convert(lat));
		location.setLongitude(Location.convert(lng));
		return location;
		
		}
	
	public static boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
													currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private static boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
	
	public static String[] convertStringToArray(String str){
        String[] arr = str.split(",");
        return arr;
    }
	
	public static String catOnGeofence(String lat, String lng, String uri){
		return lat+","+lng+","+uri;
	}
}
