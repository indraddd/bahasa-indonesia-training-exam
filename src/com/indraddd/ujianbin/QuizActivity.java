/**
 * Code Written By Indra Dwi Utama © 2015
 * March, 30th, 2015
 * Informatics Engineering 
 * Joint Program VEDC Malang Ñ Sekolah Tinggi Teknik Atlas Nusantara
 * TI 3.1 ¥ 1111014
 * this was my very first Android native application 
 * made for Final Project in the 8th Semester
 * use this project for our learning purpose
 * **/
package com.indraddd.ujianbin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings.Global;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class QuizActivity extends Activity implements TextToSpeech.OnInitListener, RecognitionListener, OnUtteranceCompletedListener{
	List<Quiz> quizList;
	int score = 0;
	int _id = 0;
	boolean killCommanded = false;
	private Quiz currentQ;
	private TextView txtQuestion;
	private RadioButton rda, rdb, rdc, rdd, rde;
	private Button butNext;
	private Switch mutSwtch;
	private ToggleButton toggleButton;
	private ProgressBar progressBar;
	private TextToSpeech tts;
	private TextView hasil;
	private String LOG_TAG = "QuizActivityRecognizer";
	private static final String TAG = null;
	private SpeechRecognizer speech;
	private Intent recognizerIntent;
	private Handler handler = new Handler();
	private static final int VALID_COMMANDS_SIZE = VALID_COMMANDS.length;
	private static final String[] VALID_COMMANDS ={
		"jam berapa",
		"hari apa",
		"siapa anda",
		"kembali"
	};	
	protected static final int RESULT_SPEECH = 1;
	protected PowerManager.WakeLock wakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		MyDatabase db = new MyDatabase(this);
		final List<Quiz> quizList = db.getAllQuiz();
		currentQ = quizList.get(_id);
		
		tts = new TextToSpeech(this, this);
		
		hasil = (TextView)findViewById(R.id.txtText);
		
		txtQuestion = (TextView)findViewById(R.id.textView1);
		rda = (RadioButton)findViewById(R.id.radio0);
		rdb = (RadioButton)findViewById(R.id.radio1);
		rdc = (RadioButton)findViewById(R.id.radio2);
		rdd = (RadioButton)findViewById(R.id.radio3);
		rde = (RadioButton)findViewById(R.id.radio4);
		mutSwtch = (Switch)findViewById(R.id.switch1);
		butNext = (Button)findViewById(R.id.button1);
		toggleButton = (ToggleButton)findViewById(R.id.toggleButton1);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		
		speech=SpeechRecognizer.createSpeechRecognizer(this);
		speech.setRecognitionListener(this);
		
		recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "id-ID");
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
		
		final PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		this.wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, LOG_TAG);
		this.wakeLock.acquire();
		
		setQuestionView();		
		
		mutSwtch.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
				if(isChecked){
					audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
				}
			}
			
		});
		
		mutSwtch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
		butNext.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				
				
				RadioGroup grp = (RadioGroup)findViewById(R.id.radioGroup1);
				RadioButton answer = (RadioButton)findViewById(grp.getCheckedRadioButtonId());
				
				
				if(grp.getCheckedRadioButtonId() == -1){					
					Toast.makeText(getApplicationContext(), "Pilih salah satu jawaban terlebih dahulu!", Toast.LENGTH_LONG).show();
				}else{
					Log.d("your answer", currentQ.getJawaban_bnr()+ " "+answer.getText());
					if(currentQ.getJawaban_bnr().equals(answer.getText())){
						score++;
						Log.d("score", "Your Score: "+score);
					}
					if(_id<20){
						grp.clearCheck();
						currentQ = quizList.get(_id);
						setQuestionView();
						speakOut();
					}else{
						tts.stop();
						tts.shutdown();
						AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
						audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
						Intent i = new Intent(QuizActivity.this, ResultActivity.class);
						Bundle b = new Bundle();
						b.putInt("score", score);
						i.putExtras(b);
						startActivity(i);
						finish();
					}
					
					
				}
			}
		});
		
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
				
			}
		});
		
		
	}
	
	@Override
	public void onBackPressed(){
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("Mengakhiri Ujian")
		.setMessage("Anda yakin untuk mengakhiri ujian?")
		.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub				
				tts.stop();
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
				finish();
				
			}
		})
		.setNegativeButton("Tidak", null)
		.show();		
	}

	public void sayit(View v){
		speakOut();
	}
	
	@Override
	public void onDestroy(){
		if(tts != null){
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	public void onInit(int status){
		if(status == TextToSpeech.SUCCESS){
			
			
			int result = tts.setLanguage(new Locale("IND"));
			tts.setOnUtteranceCompletedListener(this);
			
			if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
				Log.e("TTS", "Bahasa Tidak Didukung");
			}else{
				speakOut();
			}
		}else{
			Log.e("TTS", "Inisialisasi Gagal!");
		}
	}
	
	private void speakOut(){
		String text = txtQuestion.getText().toString();
		String text0 = rda.getText().toString();
		String text1 = rdb.getText().toString();
		String text2 = rdc.getText().toString();
		String text3 = rdd.getText().toString();
		String text4 = rde.getText().toString();
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		tts.speak(text0, TextToSpeech.QUEUE_ADD, null);
		tts.speak(text1, TextToSpeech.QUEUE_ADD, null);
		tts.speak(text2, TextToSpeech.QUEUE_ADD, null);
		tts.speak(text3, TextToSpeech.QUEUE_ADD, null);
		tts.speak(text4, TextToSpeech.QUEUE_ADD, null);
	}
	
	@Override
	public void onUtteranceCompleted(String utteranceId) {
		Log.i(LOG_TAG, "utterance completed");
	}

	private void setQuestionView() {
		txtQuestion.setText(currentQ.getPertanyaan());
		rda.setText(currentQ.getJawaban_a());
		rdb.setText(currentQ.getJawaban_b());
		rdc.setText(currentQ.getJawaban_c());
		rdd.setText(currentQ.getJawaban_d());
		rde.setText(currentQ.getJawaban_e());
		_id++;
		
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
			speech = null;
			Log.i(LOG_TAG, "destroy");
		}
		this.wakeLock.release();
		super.onPause();
	}

	@Override
	public void onRmsChanged(float rmsdB) {
		progressBar.setProgress((int) rmsdB);
		
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
		progressBar.setIndeterminate(false);
		progressBar.setMax(10);
	}

	
	@Override
	public void onEndOfSpeech() {
		Log.i(LOG_TAG, "onEndOfSpeech");
		progressBar.setIndeterminate(true);
	}
	@Override
	public void onResults(Bundle results) {
		Log.i(LOG_TAG, "onResults");

		ArrayList<String> matches;
		if(results != null){
			matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);			
			if(results != null){
				Log.i(LOG_TAG, "Suara Dikenali: " + matches.get(0));
				String text = matches.get(0);
				
				String answA, answB, answC, answD, answE;
				answA = currentQ.getJawaban_a();
				answB = currentQ.getJawaban_b();
				answC = currentQ.getJawaban_c();
				answD = currentQ.getJawaban_d();
				answE = currentQ.getJawaban_e();
				String[] pisahA = answA.split("\\s+");
				String[] pisahB = answB.split("\\s+");
				String[] pisahC = answC.split("\\s+");
				String[] pisahD = answD.split("\\s+");
				String[] pisahE = answE.split("\\s+");
				
				int pisaSize = pisahA.length;
				int pisbSize = pisahB.length;
				int piscSize = pisahC.length;
				int pisdSize = pisahD.length;
				int piseSize = pisahE.length;
				
				String[] pilihan = text.toString().split("\\s+"); 
				int pilSize = pilihan.length;

				int nilaiBandA = 0;
				int nilaiBandB = 0;
				int nilaiBandC = 0;
				int nilaiBandD = 0;
				int nilaiBandE = 0;
				
				hasil.setText(text);
				if(rda.getText().equals(text)  ){
					rda.setChecked(true);
					handler.postDelayed(sendData, 5000);
				}else if(rdb.getText().equals(text)){
					rdb.setChecked(true);
					handler.postDelayed(sendData, 5000);
				}else if(rdc.getText().equals(text)){
					rdc.setChecked(true);
					handler.postDelayed(sendData, 5000);
				}else if(rdd.getText().equals(text)){
					rdd.setChecked(true);
					handler.postDelayed(sendData, 5000);
				}else if(rde.getText().equals(text)){
					rde.setChecked(true);
					handler.postDelayed(sendData, 5000);	
				}else{					
					for(int i=0; i<pilSize; i++){
						for(int j=0; j<pisaSize; j++){
							if(pilihan[i].equals(pisahA[j])){
								nilaiBandA++;
							}
						}
						for(int k=0; k<pisbSize; k++){
							if(pilihan[i].equals(pisahB[k])){
								nilaiBandB++;
							}
						}
						for(int l=0; l<piscSize; l++){
							if(pilihan[i].equals(pisahC[l])){
								nilaiBandC++;
							}
						}
						for(int m=0; m<pisdSize; m++){
							if(pilihan[i].equals(pisahD[m])){
								nilaiBandD++;
							}
						}
						for(int n=0; n<piseSize; n++){
							if(pilihan[i].equals(pisahE[n])){
								nilaiBandE++;
							}
						}
				
					}
					Log.i(getClass().getName(), "nilai A ="+ nilaiBandA);
					Log.i(getClass().getName(), "nilai B ="+ nilaiBandB);
					Log.i(getClass().getName(), "nilai C ="+ nilaiBandC);
					Log.i(getClass().getName(), "nilai D ="+ nilaiBandD);
					Log.i(getClass().getName(), "nilai E ="+ nilaiBandE);
					
					
					double maxVal = Math.max(nilaiBandA, Math.max(nilaiBandB, Math.max(nilaiBandC, Math.max(nilaiBandD, nilaiBandE))));
					Log.i(getClass().getName(), "biggest value ="+ maxVal);
					
					if(maxVal != 0){
						if(maxVal == nilaiBandA){
							rda.setChecked(true);
							handler.postDelayed(sendData, 5000);
						}else if(maxVal == nilaiBandB){
							rdb.setChecked(true);
							handler.postDelayed(sendData, 5000);
						}else if(maxVal == nilaiBandC){
							rdc.setChecked(true);
							handler.postDelayed(sendData, 5000);
						}else if(maxVal == nilaiBandD){
							rdd.setChecked(true);
							handler.postDelayed(sendData, 5000);
						}else if(maxVal == nilaiBandE){
							rde.setChecked(true);	
							handler.postDelayed(sendData, 5000);
						}else{
							Toast.makeText(getApplicationContext(), "Jawaban Anda tidak terdapat pada pilihan, Ulangi lagi!", Toast.LENGTH_LONG).show();
							if(toggleButton.isChecked()){
								speech.startListening(recognizerIntent);
							}
						}
					}else{
						Toast.makeText(getApplicationContext(), "Jawaban Anda tidak terdapat pada pilihan, Ulangi lagi!", Toast.LENGTH_LONG).show();
						if(toggleButton.isChecked()){
							speech.startListening(recognizerIntent);
						}
						
					}
					if(toggleButton.isChecked()){
						speech.startListening(recognizerIntent);
					}
				}
			}
			}
	}
	
	private Runnable sendData=new Runnable(){
	    public void run(){
	        try {
	            butNext.performClick();
	            if(toggleButton.isChecked()){
					speech.startListening(recognizerIntent);
				}

	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }   
	    }
	};

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
			toggleButton.setChecked(false);
		}else if(error == SpeechRecognizer.ERROR_NETWORK || error == SpeechRecognizer.ERROR_NETWORK_TIMEOUT){
			Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
		}else if(error == SpeechRecognizer.ERROR_SERVER){
			Toast.makeText(this, "can't reach SpeechRecognizer server! ...disabling SpeechRecognizer", Toast.LENGTH_LONG).show();
			speech.stopListening();
		}else{
			if(toggleButton.isChecked()){
				speech.startListening(recognizerIntent);
			}			
		}
	}
}