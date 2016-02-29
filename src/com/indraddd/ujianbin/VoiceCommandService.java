package com.indraddd.ujianbin;

import java.lang.ref.WeakReference;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

public class VoiceCommandService extends Service{
	
	protected static final boolean DEBUG = false;
	protected static final String TAG = null;
	private int bindFlag;
	private Messenger serviceMessenger;
	private Messenger serverMessenger;
	private Context activityContext;
	static VoiceCommandService voiceCommandService;
	protected Intent recognizerIntent;
	protected SpeechRecognizer speech;
	
	protected AudioManager audioManager;


	protected boolean isListening;
	protected volatile boolean isCountDownOn;

	static final int MSG_RECOGNIZER_START_LISTENING = 1;
	static final int MSG_RECOGNIZER_CANCEL = 2;
	
	public static void init(Context context){
		voiceCommandService = new VoiceCommandService();
		Context activityContext = context;
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate();
		Intent service = new Intent(activityContext, Menu.class);
		activityContext.startService(service);
		bindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;
		
		speech = SpeechRecognizer.createSpeechRecognizer(this);
		speech.setRecognitionListener((RecognitionListener) this);
		
		recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "id-ID");
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
		
	}
	
	
	protected void onStart(){
		super.onStart(null, bindFlag);
		bindService(new Intent(this, Menu.class), serviceConnection, bindFlag);
	}
	
	protected void onStop(){
		if(serviceMessenger != null){	
			unbindService(serviceConnection);
			serviceMessenger = null;
		}
		
	}
	
	private final ServiceConnection serviceConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			if(DEBUG){
				Log.d(TAG, "on service connected");
			}
			
			serviceMessenger = new Messenger(service);
			Message msg = new Message();
			msg.what = VoiceCommandService.MSG_RECOGNIZER_START_LISTENING;
			
			try{
				serverMessenger.send(msg);
			}catch(RemoteException e){
				e.printStackTrace();
			}
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			if(DEBUG){
				Log.d(TAG, " on service disconnected");
			}
			serviceMessenger = null;
		}
	};
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "on bind");
		return serverMessenger.getBinder();
	}

}
