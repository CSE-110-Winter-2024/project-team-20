package edu.ucsd.cse110.successorator;
import java.util.Date;

import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;
import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;
import edu.ucsd.cse110.successorator.ui.MainFragmentAdapter;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding view;
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

        //To test the empty goal text
        v.emptyGoals.setText(R.string.emptyGoalsText);

        var datasource = DataSource.fromDefault();
        this.model = new MainViewModel(new SimpleGoalRepository(datasource));
        this.view = ActivityMainBinding.inflate(getLayoutInflater());

//        var modelOwner = this;
//        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
//        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
//        this.model = modelProvider.get(MainViewModel.class);

        model.isGoalsEmpty().observe(isGoalsEmpty -> {
            if (Boolean.TRUE.equals(isGoalsEmpty)) {
                // set viz one way
                model.getGoals().observe(text -> v.emptyGoals.setText(R.string.emptyGoalsText));
                v.emptyGoals.setVisibility(View.VISIBLE);
                v.listGoals.setVisibility(View.INVISIBLE);
            } else {
                MainFragmentAdapter adapter = new MainFragmentAdapter(this, datasource.getGoals());
                model.getGoals().observe(text -> v.listGoals.setAdapter(adapter));
                v.emptyGoals.setVisibility(View.INVISIBLE);
                v.listGoals.setVisibility(View.VISIBLE);
            }
        });

//        var addButton = view.goalAddButton;
//        // This triggers the popup for keyboard
//        addButton.setOnClickListener(v -> {
//            // Functionality for keyboard and input popup
//
//
//        });

//        this.view = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(view.getRoot());
    }
}
