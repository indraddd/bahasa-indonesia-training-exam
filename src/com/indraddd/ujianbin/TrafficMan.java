package com.indraddd.ujianbin;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;
import android.widget.TextView;

public class TrafficMan extends Activity{
	
	
	
	private static final String TAG = "TrafficMan";
	private Handler mHandler = new Handler();	
	private long mStartRX = 0;
	private long mStartTX = 0;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic);
		
		mStartRX = TrafficStats.getTotalRxBytes();
		mStartTX = TrafficStats.getMobileTxBytes();
		//mStartRX = TrafficStats.getUidRxBytes();
		//mStartTX = TrafficStats.getUidTxBytes();
		if(mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED){
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("uh oh!");
			alert.setMessage("Your device does not support traffic stat monitoring!");
			alert.show();
		}else{
			mHandler.postDelayed(mRunnable, 1000);
		}
		int uid = 0;
		PackageManager pm = getPackageManager();
		ApplicationInfo pi = getApplicationInfo();
		uid = pi.uid;
		Log.d("data", "received bytes: "+ String.valueOf(TrafficStats.getUidRxBytes(uid)+ " transmitted bytes: " + TrafficStats.getUidTxBytes(uid)));
		TextView PI = (TextView)findViewById(R.id.text2);
		PI.setText(String.valueOf(uid));
		
		
	}
	
	private final Runnable mRunnable = new Runnable(){
		public void run(){
			TextView RX = (TextView)findViewById(R.id.rx);
			TextView TX = (TextView)findViewById(R.id.tx);
		
			long rxBytes = TrafficStats.getTotalRxBytes()- mStartRX;
			RX.setText(Long.toString(rxBytes));
		
			long txBytes = TrafficStats.getTotalTxBytes()- mStartTX;
			TX.setText(Long.toString(txBytes));
		
			mHandler.postDelayed(mRunnable, 10000);
		
		}
	};
	
	
	

}
