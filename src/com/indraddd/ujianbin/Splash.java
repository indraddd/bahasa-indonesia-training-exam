package com.indraddd.ujianbin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Thread splash_screen = new Thread(){
			
			public void run(){
				try{
					sleep(2000);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					startActivity(new Intent(getApplicationContext(),Menu.class));
					finish();
				}
			}
		};
		splash_screen.start();
		
	}
}
