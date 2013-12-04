package com.moviegraph;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Movies extends ListActivity {

	public static final String ROW_ID = "row_id"; // Intent extra key
	private ListView ratingListView; 
	private CursorAdapter ratingAdapter;
    private int listID;

	@Override
	public void onCreate(Bundle savedInstanceState) { 

		super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        listID = extras.getInt("listID");

        ratingListView = getListView();
		ratingListView.setOnItemClickListener(viewRatingListener);

		// map each ratings's name to a TextView
		// in the ListView layout
		String[] from = new String[] { "name" };
		int[] to = new int[] { R.id.movieTextView};

		ratingAdapter = new SimpleCursorAdapter(
				Movies.this,
				R.layout.movie_list_item, null,
				from, to, 0);

		setListAdapter(ratingAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume(); 

		// create new GetRatingsTask and execute it 
		new GetRatingsTask().execute((Object[]) null);
	} 


	@Override
	protected void onStop() {
		Cursor cursor = ratingAdapter.getCursor(); 

		if (cursor != null)
			cursor.close(); // deactivate it

		ratingAdapter.changeCursor(null); // adapter now has no Cursor
		super.onStop();
	}

	// performs database query outside GUI thread
	private class GetRatingsTask extends AsyncTask<Object, Object, Cursor> {
		DatabasePending databasePending = new DatabasePending(Movies.this);
        DatabaseSeen databaseSeen = new DatabaseSeen(Movies.this);
        DatabaseAllMovies databaseAllMovies = new DatabaseAllMovies(Movies.this);


        // perform the database access
		@Override
		protected Cursor doInBackground(Object... params) {
			databasePending.open();
            databaseSeen.open();
            databaseAllMovies.open();

            Cursor movieList = null;

            if(listID == 0)
                movieList = databasePending.getAllMovies();
            else if(listID == 1)
                movieList = databaseSeen.getAllMovies();
            else if(listID == 2)
                movieList = databaseAllMovies.getAllMovies();

			return movieList;
		} 

		// use the Cursor returned from the doInBackground method
		@Override
		protected void onPostExecute(Cursor result) {
			ratingAdapter.changeCursor(result); 
			databasePending.close();
            databaseSeen.close();
            databaseAllMovies.close();
		} 
	} // end class GetContactsTask


	// create the Activity's menu from a menu resource XML file
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_options_menu, menu);
		return true;
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        Intent screen =new Intent();
		// create a new Intent to launch
       if(item.getItemId()==R.id.LogoutItem){
           screen =
new Intent(this, WelcomeScreen.class);
       }else {
            screen =
                   new Intent(this, RecommendMovie.class);
       }

		startActivity(screen);
		return super.onOptionsItemSelected(item); 
	}

	// event listener that responds to the user touching a contact's name
	// in the ListView
	OnItemClickListener viewRatingListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Log.d("******MoiveRater", "postion: " + position + ", id: " + id);
			// create an Intent to launch the ViewRating Activity
			Intent viewMovieInfo = new Intent(Movies.this, ViewMovieInfo.class);

			// pass the selected movie's row ID as an extra with the Intent
			viewMovieInfo.putExtra("_id", id);
            // pass the the id of the movie list: pending, seen, or all
            viewMovieInfo.putExtra("listID", listID);
			startActivity(viewMovieInfo);
		}
	};
}