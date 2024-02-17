package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public interface SimpleGoalRepository {
    // DataSource getter used for testing
    DataSource getDataSource();

    // Potentially useful for UI Implementation
    Integer count();

    // getting a SimpleSubject to a goal based on its id
    SimpleSubject<Goal> find(int id);

    // get all Goals, Incomplete and Complete
    SimpleSubject<List<Goal>> findAll();

    // add a goal to the list
    void save(Goal goal);
}
