package com.indraddd.ujianbin;

import java.util.Locale;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity implements TextToSpeech.OnInitListener{
	
	private TextToSpeech tts;
	TextView t;
	TextView sc;
	Button pemb;
	private String text;
	int percentage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		TextView sc = (TextView)findViewById(R.id.textView1);
		TextView t = (TextView)findViewById(R.id.textResult);
		TextView big = (TextView)findViewById(R.id.textView2);
		pemb = (Button)findViewById(R.id.btnBhs);
		
		Bundle b = getIntent().getExtras();
		int score = b.getInt("score");
		
		tts = new TextToSpeech(this, this);
		
		int nilai = 0;
		int btsLulus = 70;
		nilai = score * 5;
		
		big.setText(" "+nilai );
		
		if(nilai >= btsLulus){
			t.setText("Selamat, Nilai Anda Adalah"+nilai+", Lulus");
			
		}else{
			t.setText("Maaf, Nilai Anda Adalah"+nilai+", Tidak Lulus");
		}
		
		percentage = (100/20)*score;
		
		sc.setText(percentage+" % Jawaban benar dari 20 soal.");
		
		text = t.getText().toString(); 
		
		pemb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent i = new Intent(ResultActivity.this, PembahasanActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
				startActivity(i, bndlanimation);
			}
		});	
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
		
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
	

}

