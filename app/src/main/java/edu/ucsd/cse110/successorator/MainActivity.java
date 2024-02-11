package edu.ucsd.cse110.successorator;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.GoalEntry;
import edu.ucsd.cse110.successorator.lib.domain.GoalList;

public class MainActivity extends AppCompatActivity {
    ListView listgoals;
    String[] goals = {"b", "c"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);

        /*Code got from:
        https://www.geeksforgeeks.org/how-to-get-current-time-and-date-in-android/
        How to Get Current Time and Date in Android?
        Captured at 2/09/2024
        Used for copying code to capture the date, and changed the format to fit our format
        Copied code start-
        SimpleDateFormat sdf = new SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z");
        String currentDateAndTime = sdf.format(new Date());
        -end
        */
        SimpleDateFormat date = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
        String currentDate = date.format(new Date());
        view.dateText.setText(currentDate);

        //To test the empty goal text
        view.emptyGoals.setText(R.string.emptyGoalsText);

        setContentView(R.layout.activity_main);
        listgoals = (ListView)findViewById(R.id.listGoals);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_main, goals);
        listgoals.setAdapter(arrayAdapter);
        view.listGoals.setVisibility(View.VISIBLE);


        var addButton = view.imageButton;
        // This triggers the popup for keyboard
        addButton.setOnClickListener(v -> {
            // Functionality for keyboard and input popup
        });

        /*
        Temporary object goalList with instance variable List<Goals>
        if (goalList.size() == 0){
            view.emptyGoals.setVisibility(View.VISIBLE);
            view.listGoals.setVisibility(View.INVISIBLE);
        } else {
            view.emptyGoals.setVisibility(View.INVISIBLE);
            view.listGoals.setVisibility(View.VISIBLE);

            // code for displaying goals in ListView
            // use ArrayAdapter

        }
         */

        //current placeholder idea for showing the goal list and empty goal list situation
        /* subject to changes
        ArrayList<String> glist;
        glist = new ArrayList<String>();
        glist.add("Goal 1");
        glist.add("Goal 2");
        if (glist.size() == 0){
            view.emptyGoals.setText(R.string.emptyGoalsText);
        }
        else{
            view.listGoals.setText(glist);
        }
        */
        setContentView(view.getRoot());
    }
}
