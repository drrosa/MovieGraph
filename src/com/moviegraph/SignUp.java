package com.moviegraph;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;


public class SignUp extends Activity {

    private static final String TAG= "On Signup saving profile";
    private long rowID;

    private EditText email;

    private EditText password;
    private EditText confirm_password;
    private EditText tag1;
    private EditText tag2;


    // called when the Activity is first started
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		email = (EditText) findViewById(R.id.new_email);
		password = (EditText) findViewById(R.id.password);
		confirm_password = (EditText) findViewById(R.id.confirm_password);



        Bundle extras = getIntent().getExtras();

        // if there are extras, use them to populate the EditTexts
        if (extras != null) {
            rowID = extras.getLong("row_id");
            email.setText(extras.getString("email"));
            password.setText(extras.getString("password"));

        } // end if

        // set event listener for the Save Rating Button
		Button SubmitButton =
				(Button) findViewById(R.id.accept);
		SubmitButton.setOnClickListener(submitSignup);
	}




    // responds to event generated when user clicks the Done Button
    OnClickListener submitSignup = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (email.getText().length() != 0 && confirm_password.getText().toString().equals(password.getText().toString())) {

                AsyncTask<Object, Object, Object> saveRatingTask =
                        new AsyncTask<Object, Object, Object>() {

                            @Override
                            protected Object doInBackground(Object... params) {

                                Log.d("Android", "About to enter singUPNewUser");

//                               saveProfile();
                                signUpNewUser();
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
                if(email.getText().length() == 0){
                    // create a new AlertDialog Builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(SignUp.this);

                    // set dialog title & message, and provide Button to dismiss
                    builder.setTitle(R.string.errorEmail);
                    builder.setMessage(R.string.errorMessage_email);
                    builder.setPositiveButton(R.string.errorButton, null);
                    builder.show();
                }

                if(!confirm_password.getText().toString().equals(password.getText().toString())){
                    // create a new AlertDialog Builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(SignUp.this);

                    // set dialog title & message, and provide Button to dismiss
                    builder.setTitle(R.string.errorConfirmation);
                    builder.setMessage(R.string.errorMessage_password_confirmation);
                    builder.setPositiveButton(R.string.errorButton, null);
                    builder.show();
                }

            }
        }
    };

    public void onAcceptClick(View view){

        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
//		 Do something in response to button

    }


    public void onCancelClick(View view){

        Intent intent = new Intent(this, WelcomeScreen.class);
        startActivity(intent);
//		 Do something in response to button

    }


    private void saveProfile() {

        Log.d(TAG, "profile inserted");
//		// get DatabaseConnector to interact with the SQLite database
//
//
//		Log.d(TAG, "profile inserted into DB: " + (rating.getRating() * 2));
        Statement stmt = null;
        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            /* System.out.println("Database connection success"); */

            Log.d("Android", "Database connection success");


            stmt = con.createStatement();

//            String sql = "use flipbook;";
//            stmt.executeUpdate(sql);

           String sql = "insert into profiles(email, password)values ('" +this.email.getText().toString()+"','"+ this.password.getText().toString()+"');";
            stmt.executeUpdate(sql);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static final String url = "jdbc:mysql://173.194.107.132:3306/flipbook";
    private static final String user = "root";
    private static final String pass = "root";

    public void signUpNewUser() {

        String result = "";
//the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("email",this.email.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("password",this.password.getText().toString()));


        InputStream is = null;
//http post
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.cs.utexas.edu/~nel349/getAllPeopleBornAfter.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            }catch(Exception e){
                Log.e("log_tag", "Error in http connection "+e.toString());
            }
//convert response to string
//        try{
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            is.close();
//
//            result=sb.toString();
//        }catch(Exception e){
//            Log.e("log_tag", "Error converting result "+e.toString());
//        }

//parse json data
//        try{
//            JSONArray jArray = new JSONArray(result);
//            for(int i=0;i<jArray.length();i++){
//                JSONObject json_data = jArray.getJSONObject(i);
//                Log.i("log_tag","id: "+json_data.getInt("id")+
//                        ", name: "+json_data.getString("name")+
//                        ", sex: "+json_data.getInt("sex")+
//                        ", birthyear: "+json_data.getInt("birthyear")
//                );
//            }
//        } catch (JSONException e) {
//            Log.e("log_tag", "Error parsing data " + e.toString());
//        }




    }


} 

