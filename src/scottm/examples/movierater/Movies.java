package scottm.examples.movierater;

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
    private String buttonClicked;

	@Override
	public void onCreate(Bundle savedInstanceState) { 

		super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        buttonClicked = intent.getStringExtra("buttonClicked");

        ratingListView = getListView();
		ratingListView.setOnItemClickListener(viewRatingListener);      

		// map each ratings's name to a TextView
		// in the ListView layout
		String[] from = new String[] { "name" };
		int[] to = new int[] { R.id.ratingTextView };

		ratingAdapter = new SimpleCursorAdapter(
				Movies.this,
				R.layout.rating_list_item, null, 
				from, to);

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
			cursor.deactivate(); // deactivate it

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

            if(buttonClicked.equals("pending"))
                movieList = databasePending.getAllMovies();
            else if(buttonClicked.equals("seen"))
                movieList = databaseSeen.getAllMovies();
            else if(buttonClicked.equals("all"))
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
		inflater.inflate(R.menu.movie_rating_menu, menu);
		return true;
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// create a new Intent to launch
		Intent addNewContact = 
				new Intent(this, RecommendMovie.class);
		startActivity(addNewContact); 
		return super.onOptionsItemSelected(item); 
	}

	// event listener that responds to the user touching a contact's name
	// in the ListView
	OnItemClickListener viewRatingListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Log.d("MoiveRater", "postion: " + position + ", id: " + id);
			// create an Intent to launch the ViewRating Activity
			Intent viewContact =
					new Intent(Movies.this, ViewRating.class);

			// pass the selected contact's row ID as an extra with the Intent
			viewContact.putExtra(ROW_ID, id);
			startActivity(viewContact);
		}
	};
}