package com.moviegraph;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabasePending {

    private static final String TAG = "DatabaseConnector";

    private static final String DATABASE_NAME = "MovieGraph";
    private static final String TABLE_NAME = "pending_movies";
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
    public void insertRating(String title,
            String mood, String dateSeen, String tag1, String tag2) {

        ContentValues newRating = new ContentValues();
        newRating.put("name", title);
        newRating.put("mood", mood);
        newRating.put("dateSeen", dateSeen);
        newRating.put("tag1", tag1);
        newRating.put("tag2", tag2);

        open();
        database.insert(TABLE_NAME, null, newRating);
        close();
    }


    // updates a rating in the database
    public void updateRating(long id, String name,
            String mood, String dateSeen, String tag1, String tag2) {

        ContentValues editRating = new ContentValues();
        editRating.put("name", name);
        editRating.put("mood", mood);
        editRating.put("dateSeen", dateSeen);
        editRating.put("tag1", tag1);
        editRating.put("tag2", tag2);

        open(); 
        database.update(TABLE_NAME, editRating, "_id=" + id, null);
        close(); 
    } 

    public Cursor getAllMovies() {
        return database.query(TABLE_NAME, new String[] {"_id", "name"},
                null, null, null, null, "name");
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
    public void deleteRating(long id) {
        open(); 
        database.delete(TABLE_NAME, "_id=" + id, null);
        close();
    }
} 

