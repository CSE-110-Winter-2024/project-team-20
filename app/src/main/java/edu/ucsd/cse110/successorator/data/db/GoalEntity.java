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

    GoalEntity(@NonNull Integer id, @NonNull String text, boolean isComplete){
        this.id = id;
        this.text = text;
        this.isComplete = isComplete;
    }

    public static GoalEntity fromGoal(@NonNull Goal goal){
        return new GoalEntity(goal.id(), goal.getText(), goal.isComplete());
    }

    public @NonNull Goal toGoal(){
        return new Goal(id, text, isComplete);
    }
}

