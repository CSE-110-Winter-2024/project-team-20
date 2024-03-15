package edu.ucsd.cse110.successorator.ui;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.databinding.GoalListItemBinding;

/**
 * Adapter class for the MainFragment, connects goals to the items of the ListView
 */
public class MainFragmentAdapter extends ArrayAdapter<Goal> {

    /**
     * Constructor for the main fragment adapter
     *
     * @param context - application resources
     * @param goals - goals that are to be added to the ListView
     */
    public MainFragmentAdapter(Context context, List<Goal> goals) {
        super(context, 0, new ArrayList<>(goals));
    }

    /**
     * Adds an object to the backing list of the adapter
     *
     * @param object - object to be added to the backing list of the adapter
     */
    @Override
    public void add(@Nullable Goal object) {
        super.add(object);
    }

    /**
     * Gets the view for a specific item to go on the ListView
     *
     * @param position - position of the list item
     * @param convertView - recycled view
     * @param parent - the ViewGroup that the returned view will be attached to
     * @return The root of the binding view
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        var goal = getItem(position);
        assert goal != null;

        // Binds the data to the ListView
        GoalListItemBinding binding;
        if (convertView != null) {
            binding = GoalListItemBinding.bind(convertView);
        }
        else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = GoalListItemBinding.inflate(layoutInflater, parent, false);
        }

        binding.goalText.setText(goal.getText());

        if (goal.getContext() == 1){
            binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.home_context_icon));
            binding.goalContextIcon.setText("H");
        } else if (goal.getContext() == 2){
            binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.work_context_icon));
            binding.goalContextIcon.setText("W");
        } else if (goal.getContext() == 3){
            binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.school_context_icon));
            binding.goalContextIcon.setText("S");
        } else if (goal.getContext() == 4){
            binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.errand_context_icon));
            binding.goalContextIcon.setText("E");
        }

        // Strike-through text if it is complete
        if (goal.isComplete()) {
            binding.goalText.setPaintFlags(binding.goalText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.complete_context_icon));
        } else {
            if (goal.getContext() == 1){
                binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.home_context_icon));
                binding.goalContextIcon.setText("H");
            } else if (goal.getContext() == 2){
                binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.work_context_icon));
                binding.goalContextIcon.setText("W");
            } else if (goal.getContext() == 3){
                binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.school_context_icon));
                binding.goalContextIcon.setText("S");
            } else if (goal.getContext() == 4){
                binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.errand_context_icon));
                binding.goalContextIcon.setText("E");
            }
            binding.goalText.setPaintFlags(0);
        }


        return binding.getRoot();
    }
}
