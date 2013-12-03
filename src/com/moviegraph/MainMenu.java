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

import java.io.File;


public class MainMenu extends Activity {


    String email;
    SharedPreferences prefs;
    boolean logout=false;

    //    TODO: LISTID constants

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState != null) {
           email=savedInstanceState.getString("email");
        }
        else{
            prefs = this.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
            email = prefs.getString("email", email);
        }




        Log.d("Session", "SharedPreferences exist, email= "+ email);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", email);
    }

    // create the Activity's menu from a menu resource XML file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_menu, menu);
        return true;
    }

    // handle choice from options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

                        //The only item is Lougout so it will delete preferences
        logout=true;
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

//Used to check if logout clears the shared preferences when logout.
                String email = prefs.getString("email", null);
                Log.d("Session", "SharedPreferences exist, email= "+ email);

        Intent addNewContact;
        // create a new Intent to launch

            addNewContact =
                    new Intent(this, WelcomeScreen.class);


        startActivity(addNewContact);
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();

        if(!logout){
            SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", email);
            editor.putString("lastActivity", getClass().getName());

            String lastactivity= prefs.getString("lastActivity", null);
            Log.d("Session", "lastActivity = "+ lastactivity);
            editor.commit();
        }
        logout=false;


    }
    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first

        if(!logout){
            SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", email);
            editor.putString("lastActivity", getClass().getName());

            String lastactivity= prefs.getString("lastActivity", null);
            Log.d("Session", "lastActivity = "+ lastactivity);
            editor.commit();
        }
        logout=false;
        // Save the note's current draft, because the activity is stopping
        // and we want to be sure the current note progress isn't lost.

    }


}

