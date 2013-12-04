package com.moviegraph;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

import java.io.InputStream;
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
        TextView submitSignUp = (TextView) findViewById(R.id.button_submit);


        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/dijkstra.ttf");

        ((TextView) findViewById(R.id.screen_title)).setTypeface(font);
        ((TextView) findViewById(R.id.button_cancel)).setTypeface(font);
        submitSignUp.setTypeface(font);
        email.setTypeface(font);
        password.setTypeface(font);
        confirm_password.setTypeface(font);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            rowID = extras.getLong("row_id");
            email.setText(extras.getString("email"));
            password.setText(extras.getString("password"));

        }

		submitSignUp.setOnClickListener(this.submitSignup);
	}


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
                HttpPost httppost = new HttpPost("http://www.cs.utexas.edu/~nel349/moviegraph/signup.php");
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

