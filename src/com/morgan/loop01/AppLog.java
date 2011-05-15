package com.morgan.loop01;

import android.util.Log;

public class AppLog {
	private static final String APP = "Loop01";
	
	public static int log(String message) {
		return Log.i(APP, message);
	}
}
