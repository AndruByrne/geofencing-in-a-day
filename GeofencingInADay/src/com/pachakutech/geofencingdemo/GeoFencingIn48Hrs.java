package com.pachakutech.geofencingdemo;
import android.app.Application;
import android.content.*;

public class GeoFencingIn48Hrs extends Application
{
	public final static boolean repeat = true;
	public final static long[] VIBRATE = {0, 100, 100, 100, 100, 100, 100, 100, 350, 100, 100, 100, 100, 100, 100, 100};
	public final static int TIME_TO_CHECK_SMALL = 10;
	public final static int TIME_TO_CHECK_BIG = 30;
}
