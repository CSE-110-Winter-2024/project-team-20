package edu.ucsd.cse110.successorator.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentPendingDialogCreateGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.ui.FocusModeListener;

public class CreatePendingDialogFragment extends DialogFragment {
    private MainViewModel activityModel;

    private FragmentPendingDialogCreateGoalBinding view;

    CreatePendingDialogFragment(){}

    public static CreatePendingDialogFragment newInstance() {
        Bundle args = new Bundle();
        CreatePendingDialogFragment fragment = new CreatePendingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentPendingDialogCreateGoalBinding.inflate(getLayoutInflater());
        captureGoalInput();
        return makeDialog();
    }

    private Dialog makeDialog(){
        /*
        https://stackoverflow.com/questions/17237952/dialogfragment-and-force-to-show-keyboard
        Source Title: DialogFragment and force to show keyboard
        Date Captured: 2/17/2024 4:48 pm
        Used as a reference to get the keyboard to show up on screen and have the editText focus,
        built upon previous code written just in a different way
        Handle: smhitle
         */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder
                .setView(view.getRoot())
                .setNegativeButton("Cancel", this::onNegativeButtonClick);
        Dialog dialog = dialogBuilder.create();
        var edit = view.goalEditText;
        edit.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        return dialog;
    }

    private void captureGoalInput(){
        view.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input of the textEdit
                if (view.workBtn.isChecked() || view.homeBtn.isChecked() ||
                        view.schoolBtn.isChecked() || view.errandBtn.isChecked()) {
                    var input = view.goalEditText.getText().toString();
                    LocalDateTime currentTime = activityModel.getTodayTime();
                    // Create a new Goal with the text and add it
                    int recurring = 0;
                    int contextOption = 0;

                    if (view.homeBtn.isChecked()) {
                        contextOption = 1;
                    } else if (view.workBtn.isChecked()) {
                        contextOption = 2;
                    } else if (view.schoolBtn.isChecked()) {
                        contextOption = 3;
                    } else if (view.errandBtn.isChecked()) {
                        contextOption = 4;
                    }

                    //need to set onClick to change date array based on whether or not the calendar was chosen for a future date
                    int[] date = {currentTime.getYear(), currentTime.getMonthValue(),
                            currentTime.getDayOfMonth(), currentTime.getHour(), currentTime.getMinute()};
                    int goalPair = activityModel.getMaxGoalPair()+1;
                    var newGoal = new Goal(null, input, false, -1, true, recurring,
                            date[0], date[1], date[2], date[3], date[4], contextOption, goalPair);
                    // Create a new Goal with the text and add it
                    activityModel.appendIncomplete(newGoal);
                    dismiss();
                }
            }
        });

        /*
        https://stackoverflow.com/questions/12937731/android-enter-key-listener
        Source Title:
        Date Captured: 2/17/2024 4:51 pm
        Used as a reference to have the done/check button on the keyboard to "mark as done"
        and capture the input to add as a goal. When typing .setOnEditorActionListener()
        the code automatically generated, the rest was reused from previously written code
        Handle: smhitle
         */
//        view.goalEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            // Capture the input
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
//                    // Get the input of the textEdit
//                    var input = view.goalEditText.getText().toString();
//                    LocalDateTime currentTime = LocalDateTime.now();
//                    // Create a new Goal with the text and add it
//                    String recurring = null;
//
//                    //need to set onClick to change date array based on whether or not the calendar was chosen for a future date
//                    int[] date = {currentTime.getYear(), currentTime.getMonthValue(),
//                            currentTime.getDayOfMonth(), currentTime.getHour(), currentTime.getMinute()};
//
//                    var newGoal = new Goal(null, input, false, -1, true, recurring,
//                            date[0], date[1], date[2], date[3], date[4]);
//                    // Create a new Goal with the text and add it
//                    activityModel.appendIncomplete(newGoal);
//                    dismiss();
//                }
//                return false;
//            }
//        });
    }

    /**
     * Behavior of the negative button of the DialogFragment
     *
     * @param dialog - instance of the dialog the negative button belongs to
     * @param which - the identifier of the button that was clicked
     */
    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }
}
