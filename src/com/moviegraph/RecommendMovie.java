package com.moviegraph;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class RecommendMovie extends Activity
        implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "RecommendMovie";

    private long rowID;

    private EditText to;
    private EditText title;
    private TextView mood;
    private TextView dateSeen;
    private EditText tag1;
    private EditText tag2;
    private int buttonID;
    private Bundle extras;

    private static final String[] moodList ={"", "Clever", "Tense", "Witty", "Exciting", "Serious", "Gloomy"};


//    TODO: BUTTONID constants

    // called when the Activity is first started
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend);
//        SharedPreferences prefs = this.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
//        String email = prefs.getString("email", null);

        to = (EditText) findViewById(R.id.toEditText);
        title = (EditText) findViewById(R.id.titleEditText);
        mood = (TextView) findViewById(R.id.moodTextView);
        dateSeen = (TextView) findViewById(R.id.dateSeenTextView);
        tag1 = (EditText) findViewById(R.id.tag1EditText);
        tag2 = (EditText) findViewById(R.id.tag2EditText);

        TextView sendButton = (TextView) findViewById(R.id.send);

        extras = getIntent().getExtras();

        if (extras != null) {
            rowID = extras.getLong("_id");
            title.setText(extras.getString("name"));
            dateSeen.setText(extras.getString("dateSeen"));
            mood.setText(extras.getString("mood"));
            tag1.setText(extras.getString("tag1"));
            tag2.setText(extras.getString("tag2"));
        }

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/dijkstra.ttf");
        ((TextView) findViewById(R.id.screen_title)).setTypeface(font);
        to.setTypeface(font);
        title.setTypeface(font);
        mood.setTypeface(font);
        sendButton.setTypeface(font);

        sendButton.setOnClickListener(sendMovieButton);

        Spinner spin = (Spinner)findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter<String> moodDropdownList = new ArrayAdapter<String>(
                this, R.layout.spinner_mood_item, moodList );
        moodDropdownList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(moodDropdownList);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            mood.setText(moodList[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mood.setText("");
    }

    // responds to event generated when user clicks the Done Button
    OnClickListener sendMovieButton = new OnClickListener() {
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

        DatabaseAllMovies databaseAllMovies = new DatabaseAllMovies(this);
        DatabaseSeen databaseSeen = new DatabaseSeen(this);

            //If user types "populate" the database will get populated with test data.
            if(title.getText().toString().equals("Pop")){
                for(Long rowID: databaseAllMovies.populateMoviesDB())
                    databaseSeen.insertMovie(rowID, buttonID);
            }
            else{
                rowID = databaseAllMovies.insertMovie(
                        title.getText().toString(),
                        mood.getText().toString(),
                        tag1.getText().toString(),
                        tag2.getText().toString());

                databaseSeen.insertMovie(rowID, buttonID);
            }
    }
}