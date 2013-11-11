package com.moviegraph;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAllMovies {

    private static final String TAG = "DatabaseConnector";

    private static final String DATABASE_NAME = "MovieGraph";
    public static final String TABLE_NAME = "all_movies";
    private SQLiteDatabase database;
    private DatabaseOpenHelper databaseOpenHelper;


    public DatabaseAllMovies(Context context) {
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



    public Long[] populateMoviesDB(){
        Long[] dbs = new Long[9];
        dbs[0] = insertMovie("Batman: The Dark Knight", "Action", "A", "B");
        dbs[1] = insertMovie("The Lord of The Rings: The Return of the King", "Fantasy", "A", "B");
        dbs[2] = insertMovie("Teletubies", "Horror", "A", "B");
        dbs[3] = insertMovie("David esta Perdido", "Suspense", "A", "B");
        dbs[4] = insertMovie("Norman the Great", "Documentary", "A", "B");
        dbs[5] = insertMovie("Falling Skies", "Action", "A", "B");
        dbs[6] = insertMovie("The Walking Dead: The Movie", "Action", "A", "B");
        dbs[7] = insertMovie("Dragon Ball Z", "Action", "A", "B");
        dbs[8] = insertMovie("WAAAzup!!!", "Comedy", "A", "B");

        return dbs;
    }

    // get a Cursor containing information about the movie specified
    // by the given name
    public Cursor getMovieId(String name) {
        return database.query(
                "all_movies", new String[] {"_id"}, "name=" + name, null, null, null, null);

    }

    // inserts a new rating into the database
    public long insertMovie(String title,
                            String mood, String tag1, String tag2) {

        ContentValues newRating = new ContentValues();
        newRating.put("name", title);
        newRating.put("mood", mood);
        newRating.put("tag1", tag1);
        newRating.put("tag2", tag2);

        open();
        long rowID = database.insert(TABLE_NAME, null, newRating);
        close();

        return rowID;
    }


    // updates a rating in the database
    public void updateMovie(long id, String name,
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
    public void deleteMovie(long id) {
        open(); 
        database.delete(TABLE_NAME, "_id=" + id, null);
        close();
    } 






} 

