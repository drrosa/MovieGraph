package com.moviegraph;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ViewRating extends Activity {

	private long rowID; 
	private TextView name; 
	private TextView mood;
	private TextView dateSeen; 
	private TextView tag1;
	private TextView tag2;

	// called when the activity is first created
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_info);

		name = (TextView) findViewById(R.id.nameTextView);
		mood = (TextView) findViewById(R.id.genreTextView);
		dateSeen = (TextView) findViewById(R.id.dateSeenTextView);
		tag1 = (TextView) findViewById(R.id.tag1TextView);
		tag2 = (TextView) findViewById(R.id.tag2TextView);

		// get the selected rating's row ID
		Bundle extras = getIntent().getExtras();
		rowID = extras.getLong(Movies.ROW_ID);
	}


	// called when the activity is first created
	@Override
	protected void onResume() {
		super.onResume();

		// create new LoadRatingTask and execute it 
		new LoadRatingTask().execute(rowID);
	} 


	// performs database query outside GUI thread
	private class LoadRatingTask extends AsyncTask<Long, Object, Cursor> {

		DatabaseAllMovies databaseAllMovies =
				new DatabaseAllMovies(ViewRating.this);


		// perform the database access
		@Override
		protected Cursor doInBackground(Long... params) {
			databaseAllMovies.open();

			// get a cursor containing all data on given entry
			return databaseAllMovies.getOneRating(params[0]);
		}


		// use the Cursor returned from the doInBackground method
		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);

			result.moveToFirst(); 

			// get the column index for each data item
			int nameIndex = result.getColumnIndex("name");
			int moodIndex = result.getColumnIndex("mood");
			int dateSeenIndex = result.getColumnIndex("dateSeen");
			int tag1Index = result.getColumnIndex("tag1");
			int tag2Index = result.getColumnIndex("tag2");

			// fill TextViews with the retrieved data
			name.setText(result.getString(nameIndex));
			mood.setText(result.getString(moodIndex));
			dateSeen.setText(result.getString(dateSeenIndex));
			tag1.setText(result.getString(tag1Index));
			tag2.setText(result.getString(tag2Index));

			result.close();
			databaseAllMovies.close();
		} 
	} // end class LoadRatingTask


	// create the Activity's menu from a menu resource XML file
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_rating_menu, menu);
		return true;
	}

    public void onMustSeeClick(View view){
//        asyncTask(true).execute(new Long[] { rowID });

        findViewById(R.id.recommended).setEnabled(false);
        findViewById(R.id.worth_watching).setEnabled(false);
        findViewById(R.id.haven_not_seen).setEnabled(false);
    }

    public void onRecommendedClick(View view){
//        asyncTask(true).execute(new Long[] { rowID });
        findViewById(R.id.must_see).setEnabled(false);
        findViewById(R.id.worth_watching).setEnabled(false);
        findViewById(R.id.haven_not_seen).setEnabled(false);
    }

    public void onWorthWatchingClick(View view){
//        asyncTask(true).execute(new Long[]{rowID});
        findViewById(R.id.must_see).setEnabled(false);
        findViewById(R.id.recommended).setEnabled(false);
        findViewById(R.id.haven_not_seen).setEnabled(false);
    }

    public void onNotSeenClick(View view){
//        asyncTask(false).execute(new Long[]{rowID});
        findViewById(R.id.must_see).setEnabled(false);
        findViewById(R.id.worth_watching).setEnabled(false);
        findViewById(R.id.recommended).setEnabled(false);
    }

    private final DatabaseSeen databaseSeen = new DatabaseSeen(ViewRating.this);
    private final DatabasePending databasePending = new DatabasePending(ViewRating.this);


    public AsyncTask asyncTask(final boolean seen){


        AsyncTask<Long, Object, Object> moveMovieTask =
                new AsyncTask<Long, Object, Object>() {

                    @Override
                    protected Object doInBackground(Long... params) {
                        if(seen){
                            if( findViewById(R.id.haven_not_seen).isEnabled() )
                                databasePending.deleteRating(params[0]);
//                              databaseSeen.insertRating(params[0]);
                        }
                        else{
                            databaseSeen.deleteRating(params[0]);
//                          databasePending.insertRating(params[0]);
                         }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result) {
                        finish();
                    }
                };

        // save the rating to the database using a separate thread
        return moveMovieTask;
    }

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.editItem:
			// create an Intent to launch the AddEditRating Activity
			Intent recommendMovie =
			    new Intent(this, RecommendMovie.class);

			// pass the selected rating's data as extras with the Intent
			recommendMovie.putExtra(Movies.ROW_ID, rowID);
			recommendMovie.putExtra("name", name.getText());
			recommendMovie.putExtra("mood", mood.getText());
			recommendMovie.putExtra("dateSeen", dateSeen.getText());
			recommendMovie.putExtra("tag1", tag1.getText());
			recommendMovie.putExtra("tag2", tag2.getText());
			startActivity(recommendMovie);
			return true;

		case R.id.deleteItem:
			deleteRating();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	} 


	// delete a rating
	private void deleteRating() {
		// create a new AlertDialog Builder
		AlertDialog.Builder builder = 
				new AlertDialog.Builder(ViewRating.this);

		builder.setTitle(R.string.confirmTitle); 
		builder.setMessage(R.string.confirmMessage); 

		// provide an OK button that simply dismisses the dialog
		builder.setPositiveButton(R.string.button_delete,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int button) {

				final DatabaseAllMovies databaseAllMovies =
						new DatabaseAllMovies(ViewRating.this);

				// create an AsyncTask that deletes the rating in another 
				// thread, then calls finish after the deletion
				AsyncTask<Long, Object, Object> deleteTask =
						new AsyncTask<Long, Object, Object>() {

					@Override
					protected Object doInBackground(Long... params) {
						databaseAllMovies.deleteRating(params[0]);
						return null;
					} 

					@Override
					protected void onPostExecute(Object result) {
						finish(); 
					} 
				}; 

				deleteTask.execute(new Long[] { rowID });               
			}
		}); 

		builder.setNegativeButton(R.string.button_cancel, null);
		builder.show(); 
	} 
}
