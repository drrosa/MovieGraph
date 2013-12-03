package com.moviegraph;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;

public class RecommendMovie extends Activity {

    private static final String TAG = "RecommendMovie";

    private long rowID;

    private AutoCompleteTextView To;
    private EditText title;
    private EditText mood;
    private EditText dateSeen;
    private EditText tag1;
    private EditText tag2;
    private int buttonID;
//    TODO: BUTTONID constants

    // called when the Activity is first started
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend);
//        SharedPreferences prefs = this.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
//        String email = prefs.getString("email", null);

        To= (AutoCompleteTextView) findViewById(R.id.To);
        title = (EditText) findViewById(R.id.titleEditText);
        mood = (EditText) findViewById(R.id.moodEditText);
        dateSeen = (EditText) findViewById(R.id.datSeenEditText);
        tag1 = (EditText) findViewById(R.id.tag1EditText);
        tag2 = (EditText) findViewById(R.id.tag2EditText);

        Bundle extras = getIntent().getExtras();

        // if there are extras, use them to populate the EditTexts
        if (extras != null) {
            To.setText(extras.getString("name"));
            dateSeen.setText(extras.getString("dateSeen"));  ///Check
            rowID = extras.getLong("_id");
            title.setText(extras.getString("name"));
            dateSeen.setText(extras.getString("dateSeen"));
            mood.setText(extras.getString("mood"));
            tag1.setText(extras.getString("tag1"));
            tag2.setText(extras.getString("tag2"));
        } // end if


        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.To);
// Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(adapter);

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


        String result = "";
//the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("To",To.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("email",this.getSharedPreferences("myPreferences", MODE_PRIVATE).getString("email", null)));
        nameValuePairs.add(new BasicNameValuePair("title",title.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("mood",mood.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("date_seen",dateSeen.getText().toString()));


        InputStream is = null;
//http post
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.cs.utexas.edu/~nel349/moviegraph/recommend.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
    }
}