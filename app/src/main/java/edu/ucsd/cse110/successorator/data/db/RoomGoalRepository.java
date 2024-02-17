package edu.ucsd.cse110.successorator.data.db;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.util.*;

public class RoomGoalRepository implements GoalRepository {
    private final GoalDao goalDao;
    public RoomGoalRepository(GoalDao goalDao){
        this.goalDao = goalDao;
    }
    @Override
    public Subject<Goal> find(int id){
        var entityLiveData = goalDao.findAsLiveData(id);
        var goalLiveData = Transformations.map(entityLiveData, GoalEntity::toGoal);
        return new LiveDataSubjectAdapter<>(goalLiveData);
    }
    @Override
    public Subject<List<Goal>> findAll(){
        var entitiesLiveData = goalDao.findAllAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }
    @Override
    public void save(Goal goal){
        goalDao.insert(GoalEntity.fromGoal(goal));
    }
    public void save(List<Goal> goals){
        var entities = goals.stream()
                .map(GoalEntity::fromGoal)
                .collect(Collectors.toList());
                goalDao.insert(entities);
    }

    public void append(Goal goal){
        goalDao.append(GoalEntity.fromGoal(goal));
    }

    public void prepend(Goal goal){
        goalDao.prepend(GoalEntity.fromGoal(goal));
    }

    public void remove(int id){
        goalDao.delete(id);
    }

    public Integer count(){
        return goalDao.count();
    }
}