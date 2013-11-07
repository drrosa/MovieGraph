package com.moviegraph;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class SignUp extends Activity {

    private static final String TAG= "On Signup saving profile";
    private long rowID;

    private EditText email;

    private EditText password;
    private EditText confirm_password;
    private EditText tag1;
    private EditText tag2;


    // called when the Activity is first started
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.signup);

		email = (EditText) findViewById(R.id.new_email);
		password = (EditText) findViewById(R.id.password);
		confirm_password = (EditText) findViewById(R.id.confirm_password);



        Bundle extras = getIntent().getExtras();

        // if there are extras, use them to populate the EditTexts
        if (extras != null) {
            rowID = extras.getLong("row_id");
            email.setText(extras.getString("email"));
            password.setText(extras.getString("password"));

        } // end if

        // set event listener for the Save Rating Button
		Button SubmitButton =
				(Button) findViewById(R.id.accept);
		SubmitButton.setOnClickListener(saveRatingButtonClicked);
	}


    // responds to event generated when user clicks the Done Button
    OnClickListener saveRatingButtonClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (email.getText().length() != 0) {

                AsyncTask<Object, Object, Object> saveRatingTask =
                        new AsyncTask<Object, Object, Object>() {

                            @Override
                            protected Object doInBackground(Object... params) {
                               saveProfile();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object result) {
                                finish();
                            }
                        };

                // save the rating to the database using a separate thread
                saveRatingTask.execute((Object[]) null);
            }
            else {
                // create a new AlertDialog Builder
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(SignUp.this);

                // set dialog title & message, and provide Button to dismiss
                builder.setTitle(R.string.errorEmail);
                builder.setMessage(R.string.errorMessage_email);
                builder.setPositiveButton(R.string.errorButton, null);
                builder.show();
            }
        }
    };

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


    private void saveProfile() {

        Log.d(TAG, "profile inserted");
//		// get DatabaseConnector to interact with the SQLite database
		UserDatabase userdatabase = new UserDatabase(this);
//
//
//		Log.d(TAG, "profile inserted into DB: " + (rating.getRating() * 2));
//
		if (getIntent().getExtras() == null) {
//			// insert the rating information into the database
            userdatabase.insertProfile(email.getText().toString(), password.getText().toString());
		}
		else {
			userdatabase.updateRating(rowID,
                    email.getText().toString(), password.getText().toString());
		}
    }

} 

