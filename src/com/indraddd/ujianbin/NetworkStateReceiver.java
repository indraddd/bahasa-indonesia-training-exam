package com.indraddd.ujianbin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkStateReceiver extends BroadcastReceiver{

	private static final String LOG_TAG = "NetworkStateReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d("app", "Network Connectivity Change");
		if(intent.getExtras()!=null){
			NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
	        if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {
	            Log.i("app","Network "+ni.getTypeName()+" connected");
	            Toast.makeText(context, "Network connected", Toast.LENGTH_LONG).show();
	        }	
		}
		if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
			/**AlertDialog.Builder alertDialog = new AlertDialog.Builder(Menu.class);
			alertDialog.setTitle("Alert");
			alertDialog.setMessage("There's no network connectivity");
			alertDialog.show();**/
			
            Log.d("app","There's no network connectivity");
            Toast.makeText(context, "There's no network connectivity", Toast.LENGTH_LONG).show();
            
     }		
	}

}
