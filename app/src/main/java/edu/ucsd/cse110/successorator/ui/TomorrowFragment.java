package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentTomorrowBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.ui.dialog.CreateGoalDialogFragment;
import edu.ucsd.cse110.successorator.ui.dialog.CreateTomorrowDialogFragment;
import edu.ucsd.cse110.successorator.ui.dialog.FocusModeDialogFragment;

public class TomorrowFragment extends Fragment implements FocusModeListener {
    private FragmentTomorrowBinding view;
    private TomorrowFragmentAdapter adapter;

    private MainViewModel activityModel;

    private int context = 0;

    /**
     * Required empty public constructor
     */
    public TomorrowFragment(){
    }

    public static TomorrowFragment newInstance(){
        TomorrowFragment fragment = new TomorrowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner,modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the adapter
        this.adapter = new TomorrowFragmentAdapter(requireContext(), List.of());

        var tomorrow = activityModel.getTodayTime().plusDays(1);
        System.out.println("Tomorrow date" + tomorrow.getYear() + " " + (tomorrow.getMonthValue()) + " " + tomorrow.getDayOfMonth());
        activityModel.getContext(activityModel.getGoalsByDay(tomorrow.getYear(),
                        (tomorrow.getMonthValue()), tomorrow.getDayOfMonth()),
                        activityModel.getCurrentContextValue())
                .observe(goal -> {
                    if (goal == null) return;
                    System.out.println("Tomorrow adapter" + goal.size());
                    adapter.clear();
                    adapter.addAll(new ArrayList<>(goal));
                    adapter.notifyDataSetChanged();
                });
    }

    public void addGoalIncomplete(Goal goal) {
        activityModel.appendIncomplete(goal);
        adapter.addAll(goal);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        this.view = FragmentTomorrowBinding.inflate(inflater, container, false);

        view.listGoals.setAdapter(adapter);

        createSpinner();
        showTopBar();
        activityModel.rollover();
        addPlusButtonListener();
        addFocusModeListener();
        addGoalListeners();
        createDeveloperButton();

        // Inflate the layout for this fragment
        return view.getRoot();
    }

    /*
When the application is resumed, the time is updated to the current time.
When the US7 button is tapped, the time is updated to the current time plus 24 hours.
     */
    @Override
    public void onResume(){
        super.onResume();
        // Show the current date at the top
        LocalDateTime tomorrowTime = activityModel.getTodayTime();
        activityModel.updateTodayTime(tomorrowTime);
        tomorrowTime = tomorrowTime.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd");
        String formattedDate = formatter.format(tomorrowTime);


//        SimpleDateFormat date = new SimpleDateFormat("E MM/dd", Locale.getDefault());
//
//        Calendar t = Calendar.getInstance();
//        t.add(Calendar.DATE, 1);
        String tomorrow = "Tomorrow, " + formattedDate;
        activityModel.rollover();
        updateGoals();

        view.topText.setText(tomorrow);
    }

    public void showTopBar(){
        // Show the current date at the top
//        SimpleDateFormat date = new SimpleDateFormat("E MM/dd", Locale.getDefault());

//        Calendar t = Calendar.getInstance();
//        t.add(Calendar.DATE, 1);
        LocalDateTime tomorrowTime = activityModel.getTodayTime().plusDays(1);
//        activityModel.updateTodayTime(tomorrowTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd");
        String formattedDate = formatter.format(tomorrowTime);
        String tomorrow = "Tomorrow, " + formattedDate;

        view.topText.setText(tomorrow);
    }


    public void addPlusButtonListener(){
        // Show DialogFragment when button is clicked
        view.imageButton.setOnClickListener(v -> {
            var dialogFragment = CreateTomorrowDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateGoalDialogFragment");
        });
    }

    public void addGoalListeners() {
        // Listener for taps/clicks on each list item
        view.listGoals.setOnItemClickListener((parent, view, position, id) -> {
            Goal goal = adapter.getItem(position);
            assert goal != null;
            // If the tapped goal is incomplete, make it complete
            if (!goal.isComplete()) {
                goal.makeComplete();
                activityModel.removeGoalIncomplete(goal.id());
                activityModel.appendComplete(goal);
            }
            // If goal is complete make incomplete
            else {
                goal.makeInComplete();
                activityModel.removeGoalComplete(goal.id());
                activityModel.prependIncomplete(goal);
            }
        });
    }

    public void addFocusModeListener(){
        view.hamburgerMenu.setOnClickListener(v -> {
            var dialogFragment = FocusModeDialogFragment.newInstance(this);
            dialogFragment.show(getParentFragmentManager(), "FocusModeDialogFragment");
            this.context = dialogFragment.getFocusContext();
        });
    }

    public void createSpinner(){
        /*
        https://developer.android.com/develop/ui/views/components/spinner#java
        Source Title: Add spinners to your app
        Date Captured: 3/5/2024 12:33 am
        Used as a reference to have a drop down menu to switch between views via spinner
        Handle: smhitle
         */
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        dropdownAdapter.add("");
        dropdownAdapter.add("Tomorrow");
        dropdownAdapter.add("Today");

        dropdownAdapter.add("Pending");
        dropdownAdapter.add("Recurring");

        view.dropdown.setAdapter(dropdownAdapter);

        view.dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                }
                else if (position == 1) {
                    dropdownAdapter.clear();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, MainFragment.newInstance())
                            .commit();
                }
                else if (position == 2) {
                    dropdownAdapter.clear();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, PendingFragment.newInstance())
                            .commit();
                }
                else if (position == 3) {
                    dropdownAdapter.clear();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, RecurringFragment.newInstance())
                            .commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    @Override
    public void onFocusModeSelected(int context) {
        this.context = context;
        updateGoals();
    }

    public void updateGoals() {
        LocalDateTime tomorrow = activityModel.getTodayTime().plusDays(1);
        Instant instant = tomorrow.atZone(ZoneId.systemDefault()).toInstant();
        Calendar tomorrowDate = Calendar.getInstance();
        tomorrowDate.setTimeInMillis(instant.toEpochMilli());
//        while (!activityModel.getCurrUpdateValue()){}
        System.out.println("curr context in main" + activityModel.getCurrentContextValue()) ;
        activityModel.getContext(activityModel.getGoalsByDay(tomorrowDate.get(Calendar.YEAR),
                                (tomorrowDate.get(Calendar.MONTH)+1), tomorrowDate.get(Calendar.DAY_OF_MONTH)),
                        activityModel.getCurrentContextValue())
                .observe(goal -> {
                    if (goal == null) {
                        System.out.println("way too early?");
                        return;
                    }
                    System.out.println("My size is " + goal.size());
                    adapter.clear();
                    adapter.addAll(new ArrayList<>(goal));
                    adapter.notifyDataSetChanged();
                });
    }

    public void createDeveloperButton(){
        // Show the current date at the top
        SimpleDateFormat date = new SimpleDateFormat("E MM/dd", Locale.getDefault());
        // Button for developer testing, changes the date by a day
        view.imageButton2.setOnClickListener(new View.OnClickListener(){
//            Calendar c = Calendar.getInstance();
//            Calendar c2 = Calendar.getInstance();
//            LocalDateTime todayTime = activityModel.getTodayTime();


            @Override
            public void onClick(View v){
//                LocalDateTime todayTime = activityModel.getTodayTime();
//                Instant instant = todayTime.atZone(ZoneId.systemDefault()).toInstant();
//                c.setTimeInMillis(instant.toEpochMilli());
//                c2.setTimeInMillis(instant.toEpochMilli());
//
//                c.add(Calendar.DATE, 1);
//                c2.add(Calendar.DATE, 1);
//                if (c.equals(c2)){
//                    c2.add(Calendar.DATE, 1);
//                }
//                String currentDate =  "Today, " + date.format(c.getTime());
                activityModel.updateTodayTime(activityModel.getTodayTime().plusDays(1));
                LocalDateTime current = activityModel.getTodayTime();
                LocalDateTime nextDay = current.plusDays(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd");
//                String formattedDate = formatter.format(current);
                String nextFormatted = formatter.format(nextDay);
                String nextDate = "Tomorrow, " + nextFormatted;

                view.topText.setText(nextDate);
                activityModel.rollover();
                updateGoals();

            }
        });
    }

}
