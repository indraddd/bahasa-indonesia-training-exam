package com.indraddd.ujianbin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQueryBuilder;

public class MyDatabase extends SQLiteAssetHelper{
	
	/**
	 * SQLiteAssetHelper is intended as drop in alternative for the framwork's SQLiteOpenHelper
	 * An Android helper class to manage database creation and version management using an application's raw asset files.
	 * 
	 * This class provides developers with a simple way to ship their Android app with an existing SQLite 
	 * database (which may be pre-populated with data) 
	 * and to manage its initial creation and any upgrades required with subsequent version releases.
	 * 
	 * The database will be extracted from the assets and copied into place within application's private data directory.
	 * 
	 * if you prefer to store database file somewhere else (such as external storage) 
	 * you can use alternate constructor to specify a storage path.
	 * super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
	**/
	
	//providing the constructor with database name and version number
	private static final String DATABASE_NAME = "db_phone";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_SOAL = "soal";
	
	public static final String COL_SOAL_ID = "_id";
	public static final String COL_SOAL_PERTANYAAN = "pertanyaan";
	public static final String COL_SOAL_JAWABAN_A = "jawaban_a";
	public static final String COL_SOAL_JAWABAN_B = "jawaban_b";
	public static final String COL_SOAL_JAWABAN_C = "jawaban_c";
	public static final String COL_SOAL_JAWABAN_D = "jawaban_d";
	public static final String COL_SOAL_JAWABAN_E = "jawaban_e";
	public static final String COL_SOAL_JAWABAN_BNR = "jawaban_bnr";
	
	
	
	public MyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	// show all soal
	public Cursor getPertanyaan() {
		
		//The database is made available for use the first time either 
		SQLiteDatabase db = getReadableDatabase(); // <-- this 
		// or getWritableDatabase() is called.
		
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		String[] sqlSelect = {"0 _id", "pertanyaan", "jawaban_bnr"};
		String sqlTables = "soal";
		
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
		
		c.moveToFirst();
		return c;
	}
	
	public List<Quiz> getAllQuiz(){
		List<Quiz> quizList = new ArrayList<Quiz>();
		//Select all query
		String selectQuery = "SELECT * FROM " + TABLE_SOAL;
		
		//The database is made available for use the first time either 
		SQLiteDatabase db = getReadableDatabase(); // <-- this 
		// or getWritableDatabase() is called.
		
		/** The class will throw a SQLiteAssetHelperException if you do not provide the appropriate named file
		 * The SQLiteOpenHelper methods onConfigure, onCreate and OnDowngrade are not supported by this implementation and have been declared final.
		 * **/
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Quiz quiz = new Quiz();
				quiz.setId(Integer.parseInt(cursor.getString(0)));
				quiz.setPertanyaan(cursor.getString(1));
				quiz.setJawaban_a(cursor.getString(2));
				quiz.setJawaban_b(cursor.getString(3));
				quiz.setJawaban_c(cursor.getString(4));
				quiz.setJawaban_d(cursor.getString(5));
				quiz.setJawaban_e(cursor.getString(6));
				quiz.setJawaban_bnr(cursor.getString(7));
				
				//add quiz to list
				quizList.add(quiz);
			}while(cursor.moveToNext());
		}
		return quizList;	
	}
	
}
