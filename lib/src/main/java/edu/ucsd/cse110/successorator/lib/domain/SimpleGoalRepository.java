

package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;


import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleGoalRepository implements GoalRepository {

    private DataSource dataSource;

    private DataSource empty;

    public SimpleGoalRepository(DataSource dataSource) { this.dataSource = dataSource; }

    public DataSource getDataSource() { return this.dataSource; }
    @Override
    public Subject<Goal> find(int id) {
        return (Subject<Goal>) dataSource.getGoalEntrySubject(id);
    }

    @Override
    public Subject<List<Goal>> findAll() {
        return dataSource.getAllGoalEntrySubject();
    }

    @Override
    public Subject<List<Goal>> getPendingGoals() {
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        for (int i = 0; i < goals.getValue().size(); i++){
            if (goals.getValue().get(i).isPending() != true){
                goals.getValue().remove(i);
            }
        }
        return goals;
    }

    @Override
    public Subject<List<Goal>> getRecurringGoals() {
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        for (int i = 0; i < goals.getValue().size(); i++){
            if (goals.getValue().get(i).getRecurring().compareTo("null") == 0){
                goals.getValue().remove(i);
            }
        }
        return goals;
    }

    @Override
    public Subject<List<Goal>> getRecurringGoalsIncomplete() {
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        for (int i = 0; i < goals.getValue().size(); i++){
            if (goals.getValue().get(i).getRecurring().compareTo("null") == 0 || goals.getValue().get(i).isComplete()){
                goals.getValue().remove(i);
            }
        }
        return goals;
    }

    @Override
    public Subject<List<Goal>> getRecurringGoalsComplete() {
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        for (int i = 0; i < goals.getValue().size(); i++){
            if (goals.getValue().get(i).getRecurring().compareTo("null") == 0 || !goals.getValue().get(i).isComplete()){
               goals.getValue().remove(i);
               i--;
            }
        }
        return goals;
    }

    @Override
    public Subject<List<Goal>> getGoalsByDay(int year, int month, int day) {
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        for (int i = 0; i < goals.getValue().size(); i++){
            if (goals.getValue().get(i).getYear() != year && goals.getValue().get(i).getMonth() != month && goals.getValue().get(i).getDay() != day){
                goals.getValue().remove(i);
                i--;
            }
        }
        return goals;
    }

    @Override
    public Subject<List<Goal>> getGoalsByDayIncomplete(int year, int month, int day) {
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        for (int i = 0; i < goals.getValue().size(); i++){
            if (goals.getValue().get(i).getYear() != year || goals.getValue().get(i).getMonth() != month || goals.getValue().get(i).getDay() != day || goals.getValue().get(i).isComplete()){
                goals.getValue().remove(i);
                i--;
            }
        }
        return goals;
    }

    @Override
    public Subject<List<Goal>> getGoalsByDayComplete(int year, int month, int day) {
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        for (int i = 0; i < goals.getValue().size(); i++){
            if (goals.getValue().get(i).getYear() != year || goals.getValue().get(i).getMonth() != month || goals.getValue().get(i).getDay() != day || !goals.getValue().get(i).isComplete()){
                goals.getValue().remove(i);
                i--;
            }
        }
        return goals;
    }

    @Override
    public Subject<List<Goal>> getRecurringGoalsByDay(int year, int month, int day) {
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        for (int i = 0; i < goals.getValue().size(); i++){
            if (goals.getValue().get(i).getYear() != year || goals.getValue().get(i).getMonth() != month || goals.getValue().get(i).getDay() != day || goals.getValue().get(i).getRecurring().compareTo("null") == 0){
                goals.getValue().remove(i);
                i--;
            }
        }
        return goals;
    }

    @Override
    public Subject<List<Goal>> getRecurringGoalsByDayComplete(int year, int month, int day) {
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        for (int i = 0; i < goals.getValue().size(); i++){
            if (goals.getValue().get(i).getYear() != year || goals.getValue().get(i).getMonth() != month || goals.getValue().get(i).getDay() != day || goals.getValue().get(i).getRecurring().compareTo("null") == 0 || !goals.getValue().get(i).isComplete()){
                goals.getValue().remove(i);
                i--;
            }
        }
        return goals;
    }

    @Override
    public Subject<List<Goal>> getGoalsLessThanOrEqualToDay(int year, int month, int day) {
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        for (int i = 0; i < goals.getValue().size(); i++){
            if (goals.getValue().get(i).getYear() > year || goals.getValue().get(i).getMonth() > month || goals.getValue().get(i).getDay() > day){
                goals.getValue().remove(i);
                i--;
            }
        }
        return goals;
    }

    @Override
    public boolean isGoalsEmpty(){
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        return goals.getValue().isEmpty();
    }

    @Override
    public void remove(int id) {
        dataSource.removeGoal(id);
    }

    @Override
    public void removeGoalComplete(int id){
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        goals.getValue().remove(id);
    }

    @Override
    public void removeGoalIncomplete(int id){
        Subject<List<Goal>> goals = dataSource.getAllGoalEntrySubject();
        goals.getValue().remove(id);
    }

    @Override
    public void append(Goal goal) {
        dataSource.putGoalEntry(goal.withSortOrder(dataSource.getMaxSortOrder() + 1));
    }

    @Override
    public void appendComplete(Goal goal) {
        dataSource.putGoalEntry(goal.withSortOrder(dataSource.getMaxSortOrder() + 1));
    }

    @Override
    public void appendIncomplete(Goal goal) {
        dataSource.putGoalEntry(goal.withSortOrder(dataSource.getMaxSortOrder() + 1));
    }

    @Override
    public void prepend(Goal goal) {
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putGoalEntry(
                goal.withSortOrder(dataSource.getMinSortOrder() - 1)
        );
    }

    @Override
    public void deleteCompleted() {
        for (int i = 0; i < dataSource.getGoals().size(); i++){
            if (dataSource.getGoals().get(i).isComplete()){
                dataSource.removeGoal(dataSource.getGoals().get(i).id());
            }
        }
    }
}