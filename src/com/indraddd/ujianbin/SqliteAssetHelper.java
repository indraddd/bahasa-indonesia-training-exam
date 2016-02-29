package com.indraddd.ujianbin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SqliteAssetHelper extends ListActivity{
	private Cursor soal;
	private MyDatabase db;
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		db = new MyDatabase(this);
		
		soal = db.getPertanyaan();
		//List<Quiz> quiz = db.getAllQuiz();
		
		ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
				soal, 
				new String[]{"pertanyaan","jawaban_bnr"},//table values
				new int[]{android.R.id.text1, android.R.id.text2});
		
		getListView().setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		soal.close();
		db.close();
	}
}
