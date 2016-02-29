package com.indraddd.ujianbin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends Activity implements RecognitionListener, TextToSpeech.OnInitListener{
		
	private Button btnQuiz;
	private Button btn2;
	private String LOG_TAG = "SpeechRecognizer";
	private TextView hasil;
	private TextView pandu;
	private TextToSpeech tts;

	private SpeechRecognizer speech;
		
	private Intent recognizerIntent;
		
	private Handler handler = new Handler();
		
	boolean killCommanded = false;
	
	VoiceCommandService voiceCommandService;
	static Context activityContext;
	private Messenger serverMessenger;
	
	private static final String[] VALID_COMMANDS ={
		"jam berapa",
		"hari apa",
		"ujian",
		"mulai",
		"kembali"
	};
		
		
	private static final int VALID_COMMANDS_SIZE = VALID_COMMANDS.length;
	private static final int MY_DATA_CHECK_CODE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		btnQuiz = (Button) findViewById(R.id.btnQuiz);
		btn2 = (Button) findViewById(R.id.btn2);
		hasil = (TextView)findViewById(R.id.txtHsl);
		pandu = (TextView)findViewById(R.id.txt1);
		
		tts = new TextToSpeech(this, this);
		
		pandu.setText("Untuk memulai aplikasi gunakan menu ujian, atau ucapkan perintah 'Mulai' atau 'Ujian'. ");
		
		btnQuiz.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent i = new Intent(Menu.this, QuizActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
				startActivity(i, bndlanimation);
			}
		});	
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent i = new Intent(Menu.this, TrafficMan.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
				startActivity(i, bndlanimation);
			}
		});	
		
	}
	
	@Override
	protected void onStart(){
			
		speech=SpeechRecognizer.createSpeechRecognizer(this);
		speech.setRecognitionListener(this);
		
		recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "id-ID");
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
		
		speech.startListening(recognizerIntent);
		super.onStart();
		
	}
	
	private boolean doubleBackToExitPressedOnce;
	@Override
	public void onBackPressed(){
		if(doubleBackToExitPressedOnce){
			super.onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Tekan Kembali Lagi untuk Keluar", Toast.LENGTH_SHORT).show();
		
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				doubleBackToExitPressedOnce=false;
			}
		}, 2000);
	}

	@Override
	public void onDestroy(){
		if(tts != null){
			tts.stop();
			tts.shutdown();
		}
		ComponentName component = new ComponentName(this, NetworkStateReceiver.class);
		getPackageManager().setComponentEnabledSetting(component, 
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
		super.onDestroy();
	}
	
	public void onInit(int status){
		if(status == TextToSpeech.SUCCESS){
			int result = tts.setLanguage(new Locale("IND"));
			
			if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
				Log.i("TTS", "Bahasa Tidak Didukung");
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				ArrayList<String> languages = new ArrayList<String>();
				languages.add("in-IN");
				installIntent.putStringArrayListExtra(TextToSpeech.Engine.EXTRA_CHECK_VOICE_DATA_FOR, languages);				
				startActivity(installIntent);
				Toast.makeText(getApplicationContext(), "TTS: installing", Toast.LENGTH_SHORT).show();
			}else{
				speakOut();
				Log.i("TTS", "TTS Ready");
			}
		}else{
			Log.e("TTS", "Inisialisasi Gagal!");
		}
	}
	
	private void speakOut(){
		String pnd = pandu.getText().toString();
		tts.speak(pnd, TextToSpeech.QUEUE_FLUSH, null);
		
	}
	
	private void processCommand(ArrayList<String> text){
		String response = "Maaf, tidak mengerti. coba lagi..";
		int maxStrings = text.size();
		boolean resultFound = false;
		for(int i=0; i<VALID_COMMANDS_SIZE && !resultFound; i++){
			for(int j=0; j< maxStrings && !resultFound; j++){
				if(StringUtils.getLevenshteinDistance(text.get(j), VALID_COMMANDS[i])< (VALID_COMMANDS[i].length() / 3)){
					response = getResponse(i); 
				}				
			}
		}
		
		final String finalResponse = response;
		handler.post(new Runnable() {
			public void run() {
				hasil.setText(finalResponse);
			}
		});
	}
	
	private String getResponse(int command){
		Calendar c = Calendar.getInstance();
		
		String retString =  "Maaf tidak mengerti, coba lagi";
		SimpleDateFormat dfDate_day;
		switch (command) {
		case 0:
			dfDate_day= new SimpleDateFormat("HH:mm:ss");
			retString = "Sekarang jam " + dfDate_day.format(c.getTime());
			break;
		case 1:
			dfDate_day = new SimpleDateFormat("dd/MM/yyyy");
			retString= " Hari ini " + dfDate_day.format(c.getTime());
			break;
		case 2:
			retString =  "Ujian";
			Intent i = new Intent(Menu.this, QuizActivity.class);
			Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
			startActivity(i, bndlanimation);
			break;

		case 3:
			retString =  "Mulai";
			Intent e = new Intent(Menu.this, QuizActivity.class);
			Bundle bndlanimation1 = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
			startActivity(e, bndlanimation1);
			break;
			
		case 4:
			killCommanded = true;
			break;

		default:
			break;
		}
		return retString;
		
	}
	
	@Override
	public void onResume(){
		ComponentName component = new ComponentName(this, NetworkStateReceiver.class);
		getPackageManager().setComponentEnabledSetting(component, 
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
		super.onResume();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(speech != null){
			speech.destroy();
			speech = null;
			Log.i(LOG_TAG, "destroy");
		}
		super.onPause();
	}

	@Override
	public void onRmsChanged(float rmsdB) {
		
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
		Log.i(LOG_TAG, "onBufferRecived" +buffer);
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
		Log.i(LOG_TAG, "onReadyForSpeech");
		
	}

	@Override
	public void onBeginningOfSpeech() {
		Log.i(LOG_TAG, "onBeginningOfSpeech");
	}

	
	@Override
	public void onEndOfSpeech() {
		Log.i(LOG_TAG, "onEndOfSpeech");
	}

	@Override
	public void onResults(Bundle results) {
		Log.i(LOG_TAG, "onResults");
		ArrayList<String> matches = null;
		if(results != null){
			matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			if(matches != null){
				Log.i(LOG_TAG, "Suara Dikenali: " + matches.toString());
				final ArrayList<String> text = matches;
				processCommand(text);
				if(!killCommanded){
					speech.startListening(recognizerIntent);
				}else{
					finish();
				}
			}
		}
	}

	@Override
	public void onPartialResults(Bundle partialResults) {
		Log.i(LOG_TAG, "onPartialResults");
		
	}

	@Override
	public void onEvent(int eventType, Bundle params) {
		Log.i(LOG_TAG, "onEvent");
		
	}
	
	@Override
	public void onError(int error) {
		if(error == SpeechRecognizer.ERROR_CLIENT || error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS){
			Log.d(LOG_TAG, "Client error / insufficient permissions");
		}else if(error == SpeechRecognizer.ERROR_NETWORK || error == SpeechRecognizer.ERROR_NETWORK_TIMEOUT){
			Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
			Log.e(LOG_TAG, "Network Error");
			
		}else if(error == SpeechRecognizer.ERROR_SERVER){
			Toast.makeText(this, "can't reach SpeechRecognizer server! ...disabling SpeechRecognizer", Toast.LENGTH_LONG).show();
			speech.stopListening();
			Log.e(LOG_TAG, "Server Error");
			Log.i(LOG_TAG, "Disabling Speech Recognizer");
		}else{
			speech.startListening(recognizerIntent);
			
		}
	}
	
}
