package com.moviegraph;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class WelcomeScreen extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.welcome_screen);
	} 

	public void onSignupClick(View view){

		Intent intent = new Intent(this, SignUp.class);
		startActivity(intent);
//		 Do something in response to button

	}
	public void onLoginClick(View view){

		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
//		 Do something in response to button

	}
} 

