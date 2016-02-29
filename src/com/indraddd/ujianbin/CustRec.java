package com.indraddd.ujianbin;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;



public class CustRec extends Activity implements RecognitionListener{
	
	private TextView txtHasil;
	private ToggleButton toggleButton;
	private ProgressBar progressBar;
	
	private SpeechRecognizer speech = null;
	private Intent recognizerIntent;
	private String LOG_TAG = "CustRecActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custrec);
		txtHasil = (TextView)findViewById(R.id.textView1);
		toggleButton = (ToggleButton)findViewById(R.id.toggleButton1);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.INVISIBLE);
		
		speech = SpeechRecognizer.createSpeechRecognizer(this);
		speech.setRecognitionListener(this);
		
		recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "id-ID");
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
		
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				if(isChecked){
					progressBar.setVisibility(View.VISIBLE);
					progressBar.setIndeterminate(true);
					speech.startListening(recognizerIntent);
				}else{
					progressBar.setIndeterminate(false);
					progressBar.setVisibility(View.INVISIBLE);
					speech.stopListening();
				}
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(speech != null){
			speech.destroy();
			Log.i(LOG_TAG, "destroy");
		}
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onReadyForSpeech");
		
	}

	@Override
	public void onBeginningOfSpeech() {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onBeginningOfSpeech");
		progressBar.setIndeterminate(false);
		progressBar.setMax(10);
	}

	@Override
	public void onRmsChanged(float rmsdB) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onRmsChanged" +rmsdB);
		progressBar.setProgress((int) rmsdB);
		
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onBufferRecived" +buffer);
	}

	@Override
	public void onEndOfSpeech() {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onEndOfSpeech");
		progressBar.setIndeterminate(true);
		toggleButton.setChecked(false);
	}

	@Override
	public void onError(int error) {
		int errorCode = 0;
		// TODO Auto-generated method stub
		String errorMessage = getErrorText(errorCode);
		Log.d(LOG_TAG, " FAILED " +errorMessage);
		txtHasil.setText(errorMessage);
		toggleButton.setChecked(false);
	}

	@Override
	public void onResults(Bundle results) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onResults");
		ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		/**String text="";
		for(String result : matches)
			text += result;**/
		txtHasil.setText(matches.get(0));
		
	}

	@Override
	public void onPartialResults(Bundle partialResults) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onPartialResults");
		
	}

	@Override
	public void onEvent(int eventType, Bundle params) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onEvent");
		
	}
	
	public static String getErrorText(int errorCode){
		String message;
		
		switch(errorCode){
		case SpeechRecognizer.ERROR_AUDIO:
		message = "Audio Recording Error";
		break;
		
		case SpeechRecognizer.ERROR_CLIENT:
		message = "Client Side Error";
		break;
		
		case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
		message = "Insufficient Permissions";
		break;
		
		case SpeechRecognizer.ERROR_NETWORK:
		message = "Network Error";
		break;
		
		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
		message = "Network Timeout Error";
		break;
		
		case SpeechRecognizer.ERROR_NO_MATCH:
		message = "No match";
		break;
		
		case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
		message = "Recognizer Busy";
		break;
		
		case SpeechRecognizer.ERROR_SERVER:
		message = "Error from Server ";
		break;
		
		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
		message = "No Speech Input";
		break;
		
		default:
		message = "Didn't Understand, Please Try Again";
		break;
		
		}
		return message;
	}
	

}
