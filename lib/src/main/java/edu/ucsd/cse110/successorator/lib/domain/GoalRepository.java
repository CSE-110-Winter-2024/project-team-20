package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * Interface to describe the attribute and behavior of a goal repository/list
 */
public interface GoalRepository {

    // getting a SimpleSubject to a goal based on its id

    /**
     * Find specific goal subject given a goal id
     * @param id = id of goal to find
     * @return a subject of the goal given a specific id
     */
    Subject<Goal> find(int id);

    /**
     * Returns a subject of a list of all the goals
     * @return subject of a list of all the goals
     */
    Subject<List<Goal>> findAll();

    Subject<List<Goal>> getPendingGoals();

    Subject<List<Goal>> getRecurringGoals();

    Subject<List<Goal>> getGoalsByDay(int year, int month, int day);

    /**
     * Removes a goal with a specific id from the list
     * @param id of goal to remove
     */
    void remove(int id);

    /**
     * Appends a goal to the back of the list
     * @param goal to be appended
     */
    void append(Goal goal);

    /**
     * Prepends goal to the front of the list
     * @param goal to be prepended
     */
    void prepend(Goal goal);

    /**
     * Removes all the goals in the list
     */
    void deleteCompleted();
}
