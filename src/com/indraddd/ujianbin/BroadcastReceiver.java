package com.indraddd.ujianbin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BroadcastReceiver {
	public class ConnectivityReceiver extends BroadcastReceiver {

	    @Override
	    public void onReceive(Context context, Intent intent) {
	        Log.d(ConnectivityReceiver.class.getSimpleName(), "action: "
	                + intent.getAction());
	    }

	}


	private static final String LOG_TAG = "BroadcastReceiver";
	
	public static boolean hasActiveInternetConnection(Context context) {
	    if (isNetworkAvailable(context)) {
	        try {
	            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
	            urlc.setRequestProperty("User-Agent", "Test");
	            urlc.setRequestProperty("Connection", "close");
	            urlc.setConnectTimeout(1500); 
	            urlc.connect();
	            return (urlc.getResponseCode() == 200);
	        } catch (IOException e) {
	        Log.e(LOG_TAG, "Error checking internet connection", e);
	        Toast.makeText(context, "Error checking internet connection", Toast.LENGTH_LONG).show();
	        }
	    } else {
	    Log.d(LOG_TAG, "No network available!");
	    Toast.makeText(context, "no network available", Toast.LENGTH_LONG).show();
	    }
	    return false;
	}


	private static boolean isNetworkAvailable(Context context) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
	}
}
