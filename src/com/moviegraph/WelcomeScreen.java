package com.moviegraph;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class WelcomeScreen extends Activity {
    SharedPreferences prefs;


    private EditText email;
    private EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);

        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        TextView loginButton = (TextView) findViewById(R.id.button_login);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/dijkstra.ttf");

        ((TextView) findViewById(R.id.screen_title)).setTypeface(font);
        ((TextView) findViewById(R.id.button_signup)).setTypeface(font);
        loginButton.setTypeface(font);
        email.setTypeface(font);
        password.setTypeface(font);

        email.setText("DrrT");
        password.setText("asd");

        loginButton.setOnClickListener(submitLogin);
    }


    // responds to event generated when user clicks the Done Button
    View.OnClickListener submitLogin = new View.OnClickListener() {
        boolean loginResult=false;

        @Override
        public void onClick(View v) {

            if (email.getText().length() != 0 && password.getText().length() !=0 ) {

                AsyncTask<Object, Object, Object> saveRatingTask =
                        new AsyncTask<Object, Object, Object>() {

                            @Override
                            protected Object doInBackground(Object... params) {
                                if(loginUser()==true){
                                    Log.d("Android", "Login Username Exist");
                                    loginResult=true;
                                }


                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object result) {
//                                finish();

                                if(loginResult==true){
                                    loginResult=false;
                                    onLoginClick();

                                }
                                else{
                                    setInvalidLoginAlert();
                                }
                            }
                        };

                // save the rating to the database using a separate thread
                saveRatingTask.execute((Object[]) null);
//                if(loginResult==true){

//                }

            }
            else {
                if(email.getText().length() == 0){
                    // create a new AlertDialog Builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(WelcomeScreen.this);

                    // set dialog title & message, and provide Button to dismiss
                    builder.setTitle(R.string.errorEmail);
                    builder.setMessage(R.string.errorMessage_email);
                    builder.setPositiveButton(R.string.errorButton, null);
                    builder.show();
                }
                if(password.getText().length() == 0){
                    // create a new AlertDialog Builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(WelcomeScreen.this);

                    // set dialog title & message, and provide Button to dismiss
                    builder.setTitle("Enter Password");
                    builder.setMessage("Password cannot be empty");
                    builder.setPositiveButton(R.string.errorButton, null);
                    builder.show();
                }


            }
        }
    };

    public void onSignupClick(View view){

        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);

    }
    public void onLoginClick(){

        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);

    }



    public boolean loginUser() {

        String result = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("email",this.email.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("password",this.password.getText().toString()));


        InputStream is = null;
        //http post
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.cs.utexas.edu/~nel349/moviegraph/login.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result=sb.toString();
        }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }
        Log.d("Android", "Inside loginUser(): Result= "+result);

        if(result.equals("null\n")){
            Log.d("Android", "result.equals(\"null\")=  " + result.equals("null"));
            return false;
        }




        String server_password="";
        String server_email="";
                //parse json data
                try{
                    JSONObject jsonResponse = new JSONObject(new String(result));
                    server_email = jsonResponse.getString("email");
                    server_password = jsonResponse.getString("password");


                        Log.d("Android","email: "+email+
                                ", password: "+password
                        );

                } catch (JSONException e) {
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }

        //Check password credentials
        if(!this.password.getText().toString().equals(server_password)){
            return false;
        }else{
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", server_email);
//            editor.putString("name", "Test");
            editor.commit();
        }

        return true;
}


    public void setInvalidLoginAlert(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(WelcomeScreen.this);

        // set dialog title & message, and provide Button to dismiss
        builder.setTitle("Invalid Username/Password");
        builder.setMessage("Username/Password is incorrect or your username does not exist in our database");
        builder.setPositiveButton(R.string.errorButton, null);
        builder.show();
    }

} 

