package com.moviegraph;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabasePending {

    private static final String TAG = "DatabaseConnector";

    private static final String DATABASE_NAME = "MovieGraph";
    public static final String TABLE_NAME = "pending_movies";
    private SQLiteDatabase database; 
    private DatabaseOpenHelper databaseOpenHelper; 


    public DatabasePending(Context context) {
        databaseOpenHelper = 
                new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    }


    public void open() throws SQLException {
        // create or open a database for reading/writing
        database = databaseOpenHelper.getWritableDatabase();
    } 


    public void close() {
        if (database != null)
            database.close();
    }

    // inserts a new rating into the database
    public void insertMovie(long movieID) {

        ContentValues newRating = new ContentValues();
        newRating.put("movie_id", movieID);

        open();
        database.insert(TABLE_NAME, null, newRating);
        close();
    }


    // updates a rating in the database
    public void updateMovie(long id) {

        ContentValues editRating = new ContentValues();
//        editRating.put();

        open(); 
        database.update(TABLE_NAME, editRating, "_id=" + id, null);
        close(); 
    }

    public String[] getAllMovieIds(){

        Cursor result = database.query(TABLE_NAME, new String[] {"movie_id"}, null, null, null, null, "movie_id");
        String [] movieIdList = new String  [result.getCount()];

        int colID = result.getColumnIndex("movie_id");

        for(int i=0; result.moveToNext(); i++){
            movieIdList[i] = result.getString(colID);
        }

        return movieIdList;
    }

    public Cursor getAllMovies() {
        String[] selectionArgs =  getAllMovieIds();
        String selection = "_id IN(?";

        for(int i=0; i < selectionArgs.length - 1; i++)
            selection += ",?";

        selection += ")";

        return database.query(DatabaseAllMovies.TABLE_NAME, new String[] {"_id", "name"},
                selection, selectionArgs, null, null, "name");
        // query(String table, 
        // String[] columns, String selection, String[] selectionArgs, 
        // String groupBy, String having, String orderBy)
    }


    // get a Cursor containing all information about the movie specified
    // by the given id
    public Cursor getOneRating(long id) {
        return database.query(
                TABLE_NAME, null, "_id=" + id, null, null, null, null);

        // public Cursor query (String table, String[] columns, 
        // String selection, String[] selectionArgs, String groupBy, 
        // String having, String orderBy, String limit)
    }


    // delete the rating specified by the given id
    public void deleteMovie(long id) {
        open(); 
        database.delete(TABLE_NAME, "movie_id=" + id, null);
        close();
    }
} 

