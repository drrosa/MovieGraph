package scottm.examples.movierater;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class SignUp extends Activity {



	// called when the Activity is first started
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.signup);


	}

    public void onAcceptClick(View view){

        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
//		 Do something in response to button

    }


    public void onCancelClick(View view){

        Intent intent = new Intent(this, WelcomeScreen.class);
        startActivity(intent);
//		 Do something in response to button

    }


 
} 

