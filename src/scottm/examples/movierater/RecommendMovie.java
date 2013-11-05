package scottm.examples.movierater;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RecommendMovie extends Activity {

    private static final String TAG = "RecommendMovie";

    private long rowID;

    private EditText title;
    private EditText genre;
    private EditText dateSeen;
    private EditText tag1;
    private EditText tag2;

    // called when the Activity is first started
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend);

        title = (EditText) findViewById(R.id.titleEditText);
        dateSeen = (EditText) findViewById(R.id.dateSeenEditText);
        genre = (EditText) findViewById(R.id.genreEditText);
        tag1 = (EditText) findViewById(R.id.tag1EditText);
        tag2 = (EditText) findViewById(R.id.tag2EditText);

        Bundle extras = getIntent().getExtras();

        // if there are extras, use them to populate the EditTexts
        if (extras != null) {
            rowID = extras.getLong("row_id");
            title.setText(extras.getString("name"));
            dateSeen.setText(extras.getString("dateSeen"));
            genre.setText(extras.getString("genre"));
            tag1.setText(extras.getString("tag1"));
            tag2.setText(extras.getString("tag2"));
        } // end if

        // set event listener for the Save Rating Button
        Button submitButton =
                (Button) findViewById(R.id.submit);
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
                                saveRating();
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


    private void saveRating() {
        // get DatabaseConnector to interact with the SQLite database
        DatabaseConnector databaseConnector = new DatabaseConnector(this);


        if (getIntent().getExtras() == null) {
            // insert the rating information into the database

            //If user types "populate" the database will get populated with test data.
            if(title.getText().toString().equals("populate")){
                databaseConnector.populatePending();
            }

            databaseConnector.insertRating(
                    title.getText().toString(),
                    genre.getText().toString(),
                    dateSeen.getText().toString(),
                    tag1.getText().toString(),
                    tag2.getText().toString());
        }
        else {
            databaseConnector.updateRating(rowID,
                    title.getText().toString(),
                    genre.getText().toString(),
                    dateSeen.getText().toString(),
                    tag1.getText().toString(),
                    tag2.getText().toString());
        }
    }
}