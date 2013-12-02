package com.moviegraph;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


public class MainMenu extends Activity {

    //    TODO: LISTID constants
    SharedPreferences prefs;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
//        String email = prefs.getString("email", null);
//        Log.d("Session", "SharedPreferences exist, email= "+ email);
		setContentView(R.layout.main_menu);
	}

    public void onRecommendClick(View view){

        Intent intent = new Intent(this, RecommendMovie.class);
        startActivity(intent);
    }

    public void onPendingClick(View view){

        Intent intent = new Intent(this, Movies.class);
        intent.putExtra("listID", 0);
        startActivity(intent);
    }

    public void onSeenClick(View view){

        Intent intent = new Intent(this, Movies.class);
        intent.putExtra("listID", 1);
        startActivity(intent);
    }

    public void onAllMoviesClick(View view){

        Intent intent = new Intent(this, Movies.class);
        intent.putExtra("listID", 2);
        startActivity(intent);
    }



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

        //The only item is Lougout so it will delete preferences

        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

        String email = prefs.getString("email", null);
        Log.d("Session", "SharedPreferences exist, email= "+ email);

        startActivity(new Intent(this, WelcomeScreen.class));
        return super.onOptionsItemSelected(item);
    }

}

