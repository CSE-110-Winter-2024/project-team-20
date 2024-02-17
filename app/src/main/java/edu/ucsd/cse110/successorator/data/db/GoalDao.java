package edu.ucsd.cse110.successorator.data.db;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface GoalDao {

    // Only one insert since we only insert one at time
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(GoalEntity goal);

    @Query("SELECT * FROM Goals WHERE id = :id")
    GoalEntity find(int id);

    @Query("SELECT * FROM Goals ORDER BY id")
    List<GoalEntity> find();

    @Query("SELECT * FROM Goals WHERE id = :id")
    List<GoalEntity> findAsLiveData(int id);

    @Query("SELECT * FROM Goals ORDER BY id")
    List<List<GoalEntity>> findAllAsLiveData();

    @Query("SELECT * FROM Goals WHERE isComplete = :isComplete")
    List<GoalEntity> find(boolean isComplete);

    @Query("SELECT COUNT(*) FROM Goals")
    int count();

    @Query("SELECT MIN(id) FROM Goals")
    int getMinID();

    @Query("SELECT MAX(id) FROM Goals")
    int getMaxID();

    @Query("DELETE from Goals WHERE id = :id")
    void delete(int id);

    @Transaction
    default int add(GoalEntity goal){
        var maxID = getMaxID();
        var newGoalEntity = new GoalEntity(maxID+1, goal.text, goal.isComplete);
        return Math.toIntExact(insert(newGoalEntity));
    }
}
