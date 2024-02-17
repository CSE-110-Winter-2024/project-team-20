package edu.ucsd.cse110.successorator.data.db;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.room.Index;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
@Entity(tableName = "Goals")
public class GoalEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "isComplete")
    public boolean isComplete;

    @ColumnInfo(name = "sort_order")
            public int sort_order;

    GoalEntity(@NonNull Integer id, @NonNull String text, boolean isComplete, int sort_order){
        this.id = id;
        this.text = text;
        this.isComplete = isComplete;
        this.sort_order = sort_order;
    }

    public static GoalEntity fromGoal(@NonNull Goal goal){
        return new GoalEntity(goal.id(), goal.getText(), goal.isComplete(), goal.sortOrder());
    }

    public @NonNull Goal toGoal(){
        return new Goal(id, text, isComplete, sort_order);
    }
}