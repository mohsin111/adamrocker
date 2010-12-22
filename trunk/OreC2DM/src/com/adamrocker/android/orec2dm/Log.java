package com.adamrocker.android.orec2dm;

public class Log {
	private static final String TAG = "OreC2DM";
	private static final boolean DEBUG = false;
	public static void LogI(String msg) {
		if (DEBUG)
			android.util.Log.i(TAG, msg);
	}
	
	public static void LogD(String msg) {
		if (DEBUG)
			android.util.Log.d(TAG, msg);
	}
}
