package scottm.examples.movierater;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainMenu extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.main_menu);
	}

    public void onRecommendClick(View view){

        Intent intent = new Intent(this, RecommendMovie.class);
        startActivity(intent);
    }

    public void onPendingClick(View view){

        Intent intent = new Intent(this, Movies.class);
        intent.putExtra("buttonClicked", "pending");
        startActivity(intent);
    }

    public void onSeenClick(View view){

        Intent intent = new Intent(this, Movies.class);
        intent.putExtra("buttonClicked", "seen");
        startActivity(intent);
    }

    public void onAllMoviesClick(View view){

        Intent intent = new Intent(this, Movies.class);
        intent.putExtra("buttonClicked", "all");
        startActivity(intent);
    }
}

