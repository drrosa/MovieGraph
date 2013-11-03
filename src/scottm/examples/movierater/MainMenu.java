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


public class MainMenu extends Activity {



	// called when the Activity is first started
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.menu);


	} 



	// responds to event generated when user clicks the Done Button
	OnClickListener saveRatingButtonClicked = new OnClickListener() { 
		@Override
		public void onClick(View v) {
			
		} 
	}; 


 
} 

