package scottm.examples.movierater;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RatingBar;


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

