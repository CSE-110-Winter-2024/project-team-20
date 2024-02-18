package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;

public class SuccessoratorApplication extends Application {
    private DataSource dataSource;
    private GoalRepository goalRepositoryComplete;

    private GoalRepository goalRepositoryIncomplete;

    @Override
    public void onCreate(){
        super.onCreate();

// Pre persistence model
//        this.dataSource = new DataSource();
//        this.goalRepository= new SimpleGoalRepository(dataSource);

        var database = Room.databaseBuilder(getApplicationContext(),
                SuccessoratorDatabase.class, "successorator-database")
                .allowMainThreadQueries()
                .build();
        this.goalRepositoryComplete = new RoomGoalRepository(database.goalDao());

        var database2 = Room.databaseBuilder(getApplicationContext(),
                        SuccessoratorDatabase.class, "successorator-database")
                .allowMainThreadQueries()
                .build();
        this.goalRepositoryIncomplete = new RoomGoalRepository(database2.goalDao());

        // can use default goals to test
//        var sharedPreferences = getSharedPreferences("Successorator", MODE_PRIVATE);
//        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
//        if (isFirstRun && database.goalDao().count() == 0){
//            goalRepository.save(DataSource.DEFAULT_GOALS);
//
//            sharedPreferences.edit()
//                    .putBoolean("isFirstRun", false)
//                    .apply();
//        }
    }

    public GoalRepository getGoalRepositoryComplete(){
        return goalRepositoryComplete;
    }

    public GoalRepository getGoalRepositoryIncomplete(){
        return goalRepositoryIncomplete;
    }
}
