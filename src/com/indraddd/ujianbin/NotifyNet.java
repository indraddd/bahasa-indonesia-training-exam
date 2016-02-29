package com.indraddd.ujianbin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class NotifyNet extends Activity {
	
	private static final String LOG_TAG = "NotifyNet";
	static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	@Override
	protected void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		
		IntentFilter filter = new IntentFilter(ACTION);
		
		this.registerReceiver(mBroadcastReceiver, filter);
		
	}
	
	private void displayAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Alert");
		alertDialog.setMessage("There's no network connectivity");
		alertDialog.show();
	}
	
	private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
		
		@Override
		public void onReceive(Context context, Intent intent){
			String action = intent.getAction();
			if(ACTION.equals(action)){
				displayAlert();
			}
		}
	};
	

}
