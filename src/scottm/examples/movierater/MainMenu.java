package scottm.examples.movierater;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class MainMenu extends Activity {



	// called when the Activity is first started
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.main_menu);


	}

    public void onRecommendClick(View view){

        Intent intent = new Intent(this, RecommendMovie.class);
        startActivity(intent);
//		 Do something in response to button

    }

    public void onPendingClick(View view){

        Intent intent = new Intent(this, MovieRaterActivity.class);
        startActivity(intent);
//		 Do something in response to button

    }

} 

