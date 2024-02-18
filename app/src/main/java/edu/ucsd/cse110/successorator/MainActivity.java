package edu.ucsd.cse110.successorator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;

    ImageButton imageButton2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        // Temporary
        var v = FragmentMainBinding.inflate(getLayoutInflater());
        setContentView(v.getRoot());

        SimpleDateFormat date = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
        String currentDate = date.format(new Date());
        v.dateText.setText(currentDate);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();
            @Override
            public void onClick(View view) {
                c.add(Calendar.DATE, 1);
                String currentDate = date.format(c.getTime());
                v.dateText.setText(currentDate);
                setContentView(v.getRoot());
            }
        });

        //To test the empty goal text
        v.emptyGoals.setText(R.string.emptyGoalsText);

//        this.view = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(view.getRoot());
    }
}
