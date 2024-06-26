package edu.ucsd.cse110.successorator;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;

/**
 * The main activity of the app
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;



    /**
     * Method that runs when MainActivity is created
     *
     * @param savedInstanceState - state of the application
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        // Sets the content view to the view root of MainActivity
        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        // Initializes MainViewModel to just update application time
    }
    @Override
    protected void onResume() {
        super.onResume();
        SuccessoratorApplication app = (SuccessoratorApplication) getApplicationContext();
        app.setTodayTime(LocalDateTime.now());
        System.out.println("ran in Main Activity");
    }

}
