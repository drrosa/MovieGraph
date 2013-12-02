package com.moviegraph;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RecommendMovie extends Activity {

    private static final String TAG = "RecommendMovie";

    private long rowID;

    private EditText title;
    private EditText mood;
    private TextView dateSeen;
    private EditText tag1;
    private EditText tag2;
    private int buttonID;


//    TODO: BUTTONID constants

    // called when the Activity is first started
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend);

        title = (EditText) findViewById(R.id.titleEditText);
        mood = (EditText) findViewById(R.id.moodEditText);
        dateSeen = (TextView) findViewById(R.id.dateSeenTextView);
        tag1 = (EditText) findViewById(R.id.tag1EditText);
        tag2 = (EditText) findViewById(R.id.tag2EditText);

        Bundle extras = getIntent().getExtras();

        // if there are extras, use them to populate the EditTexts
        if (extras != null) {
            rowID = extras.getLong("_id");
            title.setText(extras.getString("name"));
            dateSeen.setText(extras.getString("dateSeen"));
            mood.setText(extras.getString("mood"));
            tag1.setText(extras.getString("tag1"));
            tag2.setText(extras.getString("tag2"));
        } // end if

        // set event listener for the Save Rating Button
        Button submitButton =
                (Button) findViewById(R.id.send);
        submitButton.setOnClickListener(saveRatingButtonClicked);
    }

    // responds to event generated when user clicks the Done Button
    OnClickListener saveRatingButtonClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (title.getText().length() != 0) {

                AsyncTask<Object, Object, Object> saveRatingTask =
                        new AsyncTask<Object, Object, Object>() {

                            @Override
                            protected Object doInBackground(Object... params) {
                                saveMovie();
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
                        new AlertDialog.Builder(RecommendMovie.this);

                // set dialog title & message, and provide Button to dismiss
                builder.setTitle(R.string.errorTitle);
                builder.setMessage(R.string.errorMessage);
                builder.setPositiveButton(R.string.errorButton, null);
                builder.show();
            }
        }
    };

    public void onFirstMustSee(View view){
        buttonID = 0;
    }

    public void onFirstRecommended(View view){
        buttonID = 1;
    }

    public void onFirstWorthWatching(View view){
        buttonID = 2;
    }

    private void saveMovie() {
        // get DatabaseConnector to interact with the SQLite database
        DatabaseAllMovies databaseAllMovies = new DatabaseAllMovies(this);

        DatabaseSeen databaseSeen = new DatabaseSeen(this);


        if (getIntent().getExtras() == null) {
            // insert the rating information into the database

            //If user types "populate" the database will get populated with test data.
            if(title.getText().toString().equals("Populate")){
                for(Long rowID: databaseAllMovies.populateMoviesDB())
                    databaseSeen.insertMovie(rowID, buttonID);
            }

            rowID = databaseAllMovies.insertMovie(
                    title.getText().toString(),
                    mood.getText().toString(),
                    tag1.getText().toString(),
                    tag2.getText().toString());

            databaseSeen.insertMovie(rowID, buttonID);

        }
        else {
            databaseAllMovies.updateMovie(rowID,
                    title.getText().toString(),
                    mood.getText().toString(),
                    dateSeen.getText().toString(),
                    tag1.getText().toString(),
                    tag2.getText().toString());
        }
    }
}