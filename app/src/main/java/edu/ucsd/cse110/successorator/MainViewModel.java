package edu.ucsd.cse110.successorator;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import edu.ucsd.cse110.successorator.lib.domain.ContextRepository;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * The main view model for the application
 */
public class MainViewModel extends ViewModel {

    // An initializer for the MainViewModel
    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getGoalRepositoryComplete(),
                                app.getGoalRepositoryIncomplete(), app.getGoalRepositoryRecurring(),
                                app.getTimeKeeper(), app.getActualTimeKeeper(), app.getContextRepository());
                    });
    private final GoalRepository goalRepositoryComplete;


    private  LocalDateTime todayTime;
    private final GoalRepository goalRepositoryIncomplete;

    private final GoalRepository goalRepositoryRecurring;

    public final TimeKeeper timeKeeper;

    public final TimeKeeper ActualTimeKeeper;

    private final ContextRepository contextRepository;
    private MutableSubject<List<Goal>> goals;
    private MutableSubject<Boolean> isGoalsEmpty;

    private MutableSubject<List<Goal>> goalsCompleted;

    private MutableSubject<List<Goal>> goalsIncompleted;

    private MutableSubject<Boolean> isCompletedGoalsEmpty;

    private MutableSubject<Boolean> isIncompletedGoalsEmpty;

    private MutableSubject<List<Goal>> recurringGoals;

    private MutableSubject<List<Goal>> FocusList;
    /**
     * Constructor for MainViewModel
     *
     * @param goalRepositoryComplete -
     * @param goalRepositoryIncomplete -
     * @param timeKeeper -
     */
    public MainViewModel(GoalRepository goalRepositoryComplete,
                         GoalRepository goalRepositoryIncomplete, GoalRepository goalRepositoryRecurring,
                         TimeKeeper timeKeeper, TimeKeeper ActualTimeKeeper, ContextRepository contextRepository) {

        this.goalRepositoryComplete = goalRepositoryComplete;
        this.goalRepositoryIncomplete = goalRepositoryIncomplete;
        this.goalRepositoryRecurring = goalRepositoryRecurring;
        this.timeKeeper = timeKeeper;
        this.ActualTimeKeeper = ActualTimeKeeper;
        this.contextRepository = contextRepository;

        // Observables
        this.goals = new SimpleSubject<>();
        this.isGoalsEmpty = new SimpleSubject<>();
        this.goalsCompleted = new SimpleSubject<>();
        this.goalsIncompleted = new SimpleSubject<>();
        this.isCompletedGoalsEmpty = new SimpleSubject<>();
        this.isIncompletedGoalsEmpty = new SimpleSubject<>();
        this.recurringGoals = new SimpleSubject<>();

        // Setting empty booleans to true upon initialization
        isGoalsEmpty.setValue(true);
        isCompletedGoalsEmpty.setValue(true);
        isIncompletedGoalsEmpty.setValue(true);

        this.goalRepositoryRecurring.findAll().observe(newGoals -> {
            List<Goal> orderedGoals = List.of();
            if (newGoals == null) return;
            else if (newGoals.size() == 0);
            else {
                orderedGoals = newGoals.stream()
                        .sorted(Comparator.comparingInt(Goal::getYear).thenComparing(Goal::getMonth)
                        .thenComparing(Goal::getDay).thenComparing(Goal::getHour).thenComparing(Goal::getMinutes))
                        .collect(Collectors.toList());
            }
            for (var goal : orderedGoals){
                System.out.println(goal.getText() + goal.getMinutes() + goal.getHour());
            }
            recurringGoals.setValue(orderedGoals);
        });

        // When the repository of completed goals changes, get the completed goals
        this.goalRepositoryComplete.findAll().observe(newGoals -> {
            // Gets all of the goals in the completed goals repository
            List<Goal> orderedGoals = List.of();
            if (newGoals == null) ;
            else if (newGoals.size() == 0);
            else {
                orderedGoals = newGoals.stream()
                        .collect(Collectors.toList());
            }

            goalsCompleted.setValue(orderedGoals);
        });

        // When the repository of incomplete goals changes, get the incomplete goals
        this.goalRepositoryIncomplete.findAll().observe(newGoals -> {
            // Gets all of the goals in the incomplete goals repository
            List<Goal> orderedGoals = List.of();
            if (newGoals == null) ;
            else if (newGoals.size() == 0);
            else {
                orderedGoals = newGoals.stream()
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
            goalsIncompleted.setValue(orderedGoals);
        });

        // Listens for if goals is empty
        this.goals.observe(gs -> {
            if (gs == null) return;
            isGoalsEmpty.setValue(gs.isEmpty());
        });

        // When the list of incomplete goals change, update the goals to reflect this change
        this.goalsIncompleted.observe(gs -> {
            if (gs == null) return;
            isIncompletedGoalsEmpty.setValue(gs.isEmpty());

            if (goalsCompleted.getValue() == null){
                goalsCompleted.setValue(List.of());
            }

            goals.setValue(Stream.concat(gs.stream(), goalsCompleted.getValue().stream())
                    .collect(Collectors.toList()));
        });

        // When the list of complete goals change, update the goals to reflect this change
        this.goalsCompleted.observe(gs -> {
            if (gs == null) return;
            isCompletedGoalsEmpty.setValue(gs.isEmpty());

            if (goalsIncompleted.getValue() == null){
                goalsIncompleted.setValue(List.of());
            }

            goals.setValue(Stream.concat(goalsIncompleted.getValue().stream(), gs.stream())
                    .collect(Collectors.toList()));
        });


    }

    /**
     * Things to Implement:
     *
     * As is stated by the assumptions doc, if a recurring goals is “rolled over” when incomplete,
     * then that is fine (it should rollover into the following days), but if it passes its repeat
     * period (for example, 7 days later for a weekly goal), then one should not see the goal twice,
     * but rather just see it once. Based on this, for recurring goals that are being rolled over, we
     * need to check if they are passing their boundary/repeating date and if so, just add/change the
     * date to adjust to the boundary. For example, if I Made a weekly goal on mar 1st and it rolled
     * over because of incompletion until mar 8th, then we should adjust the date of that goal in the
     * database to be mar 8th, instead of keeping it as mar 1st as planned.
     *
     * We need to adjust the rollover as well, so that if goals are incomplete, they are simply rolled
     * over to the next day, but you don’t add to the date until they are completed and rolled over if
     * they are recurrent goals (or until they pass their boundary period as described above). We don’t add to t
     *
     * The date for one time goals, because they don’t repeat. So, for the rollover, for one-time goals,
     * they are eventually just deleted, for recurrent goals, they are deleted and then readded with new dates,
     * where they are added either after they have been completed/deleted with the requisite new recurrent date
     * or if they pass their critical boundary/repeat stage, then they are deleted and readded with their new date
     * (with the new date simply being the previously stored date + the recurrent period/length). So, for recurrent
     * goals, if they are incomplete and rollover, we just store them with their date of instantiation/what date
     * they had based on their schedule and update it later to match the next recurrent date based on whether they
     * passed that date boundary or whether they completed before it.
     *
     * For Rollover/in general, we show date less than or equal to today for the today view (based on the rollover logic above)
     */
    public void rollover() {
        // Current time and time that the app was last opened
        LocalDateTime currentTime = getTodayTime();
        int lastOpenedHour = this.getFieldsForLastDate()[3];
        int lastOpenedMinute = this.getFieldsForLastDate()[4];
        int lastDay = this.getFieldsForLastDate()[2];
        int lastMonth =this.getFieldsForLastDate()[1];
        int lastYear = this.getFieldsForLastDate()[0];

        LocalDateTime previous = LocalDateTime.of(lastYear, lastMonth,
                lastDay, lastOpenedHour, lastOpenedMinute);

        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int currDay = currentTime.getDayOfMonth();
        int currMonth = currentTime.getMonthValue();
        int currYear = currentTime.getYear();

        // If current time is at least 24 hours ahead, perform completed goals deletion
        var minus24 = currentTime.minusHours(24);
        if(minus24.isAfter(previous)){
            this.deleteCompleted();
            IncompleteRecurrentRollover();
        }
        else if (minus24.isEqual(previous)){
            this.deleteCompleted();
            IncompleteRecurrentRollover();
        }
        else if (currentTime.isBefore(previous));
        else if (currDay > lastDay) {
            if ((lastDay + 1) < currDay) {
                this.deleteCompleted();
                IncompleteRecurrentRollover();
            } else {
                if (hour >= 2) {
                    this.deleteCompleted();
                    IncompleteRecurrentRollover();
                }
            }
        }
        else {
            if ((hour >= 2)
                    && (lastOpenedHour <= 2)) {
                this.deleteCompleted();
                IncompleteRecurrentRollover();
            }
        }
        this.deleteTime();
        this.appendTime(currentTime);
    }
    // updates dates of boundary recurring incomplete goals if necessary
    public void IncompleteRecurrentRollover(){
        var list = getRecurringGoalsIncomplete();
        if (list.getValue() == null || list.getValue().isEmpty()){
            return;
        }
        LocalDateTime today = getTodayTime();
        for (var goal : list.getValue()){
            if (today.isBefore(goal.getBoundaryRecurringDate())){

            }
            else {
                goal.updateRecurring();
                goalRepositoryIncomplete.remove(goal.id());
                goalRepositoryIncomplete.append(goal);
            }
        }
    }



    /**
     * Getter for the Subject of all goals, both incomplete and complete
     *
     * @return Subject with a value containing a list of all goals
     */
    public Subject<List<Goal>> getGoals() {
        return goals;
    }

    public Subject<List<Goal>> getPendingGoals() {
        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
        MutableSubject<List<Goal>> pendingGoals = new SimpleSubject<>();

        goalRepositoryIncomplete.getPendingGoals().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
            incomplete.setValue(goalList);
        });

        incomplete.observe(goals -> {
            if (goals == null) return;
            List<Goal> goalList = List.of();

            pendingGoals.setValue(Stream.concat(goals.stream(), goalList.stream())
                    .collect(Collectors.toList())
            );
        });

        return pendingGoals;
    }

    public Subject<List<Goal>> getRecurringGoalsIncomplete() {
        return goalRepositoryIncomplete.getRecurringGoals();
    }

    public Subject<List<Goal>> getRecurringGoalsComplete() {
        return goalRepositoryComplete.getRecurringGoals();
    }

    public Subject<List<Goal>> getGoalsByDayIncomplete(int year, int month, int day) {return goalRepositoryIncomplete.getGoalsByDay(year, month, day);}

    public Subject<List<Goal>> getGoalsByDayComplete(int year, int month, int day) {return goalRepositoryComplete.getGoalsByDay(year, month, day);}
    public Subject<List<Goal>> getGoalsByDay(int year, int month, int day){
        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
        MutableSubject<List<Goal>> complete = new SimpleSubject<>();
        MutableSubject<List<Goal>> goalsForDay = new SimpleSubject<>();

        goalRepositoryIncomplete.getGoalsByDay(year, month, day).observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Incomplete size" + goalList.size());
            incomplete.setValue(goalList);
        });

        goalRepositoryComplete.getGoalsByDay(year, month, day).observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Incomplete size" + goalList.size());
            complete.setValue(goalList);
        });

        incomplete.observe(goals -> {
            if (goals == null) return;

            if (complete.getValue() == null){
                complete.setValue(List.of());
            }

            goalsForDay.setValue(Stream.concat(goals.stream(), complete.getValue().stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(goalsForDay.getValue().size());
        });

        complete.observe(goals -> {
            if (goals == null) return;

            if (incomplete.getValue() == null){
                incomplete.setValue(List.of());
            }

            goalsForDay.setValue(Stream.concat(incomplete.getValue().stream(), goals.stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(goalsForDay.getValue().size());
        });

        return goalsForDay;

    }
    public Subject<List<Goal>> getRecurringGoals(){
        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
        MutableSubject<List<Goal>> complete = new SimpleSubject<>();
        MutableSubject<List<Goal>> recurring = new SimpleSubject<>();

        goalRepositoryIncomplete.getRecurringGoals().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
            incomplete.setValue(goalList);
//            System.out.println("incomp recurring size" + incomplete.getValue().size());
        });

        goalRepositoryComplete.getRecurringGoals().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Complete size" + goalList.size());
            complete.setValue(goalList);
        });

        incomplete.observe(goals -> {
            if (goals == null) return;

            if (complete.getValue() == null){
                complete.setValue(List.of());
            }

            recurring.setValue(Stream.concat(goals.stream(), complete.getValue().stream())
                    .collect(Collectors.toList())
            );
        });

        complete.observe(goals -> {
            if (goals == null) return;

            if (incomplete.getValue() == null){
                incomplete.setValue(List.of());
            }

            recurring.setValue(Stream.concat(incomplete.getValue().stream(), goals.stream())
                    .collect(Collectors.toList())
            );

//            System.out.println("Recurring size" + recurring.getValue().size());
        });

        return recurring;
    }

    public Subject<List<Goal>> getGoalsLessThanOrEqualToDay(int year, int month, int day) {
//        var first = goalRepositoryIncomplete.getGoalsLessThanOrEqualToDay(year, month, day);
//        var second = goalRepositoryComplete.getGoalsLessThanOrEqualToDay(year, month, day);

        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
        MutableSubject<List<Goal>> complete = new SimpleSubject<>();
        MutableSubject<List<Goal>> goalsForDay = new SimpleSubject<>();

        goalRepositoryIncomplete.getGoalsLessThanOrEqualToDay(year, month, day).observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Incomplete size" + goalList.size());
            incomplete.setValue(goalList);
        });

        goalRepositoryComplete.getGoalsLessThanOrEqualToDay(year, month, day).observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Complete size" + goalList.size());
            complete.setValue(goalList);
        });

        incomplete.observe(goals -> {
            if (goals == null) return;

            if (complete.getValue() == null){
                complete.setValue(List.of());
            }

            goalsForDay.setValue(Stream.concat(goals.stream(), complete.getValue().stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(goalsForDay.getValue().size());
        });

        complete.observe(goals -> {
            if (goals == null) return;

            if (incomplete.getValue() == null){
                incomplete.setValue(List.of());
            }

            goalsForDay.setValue(Stream.concat(incomplete.getValue().stream(), goals.stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(goalsForDay.getValue().size());
        });

        return goalsForDay;
    }

    public Subject<List<Goal>> getRecurringGoalsByDayComplete(int year, int month, int day) {return goalRepositoryComplete.getRecurringGoalsByDay(year, month, day);}

    public Subject<List<Goal>> getContext(Subject<List<Goal>> listOfGoals, int context){
        // if there is no context set in focus mode or the cancel button has been hit, then don't sort by anything
        if (context == 0) return listOfGoals;
        MutableSubject<List<Goal>> contextGoals = new SimpleSubject<List<Goal>>();
        contextGoals.setValue(List.of());
        listOfGoals.observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .filter(goal -> goal.getContext() == context)
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }

            contextGoals.setValue(goalList);

        });
        return contextGoals;
    }


    /**
     * Checks if list of goals is empty
     *
     * @return Subject with a value indicating whether or not list of goals is empty
     */
    public Subject<Boolean> isGoalsEmpty() {
        return isGoalsEmpty;
    }

    /*public Subject<Boolean> whichView() {
        return whichView;
    }
    */

    /**
     * Removes a specified goal from the repository of completed goals
     *
     * @param id - id of the goal to be removed
     */
    public void removeGoalComplete (int id){
        goalRepositoryComplete.remove(id);
    }

    /**
     * Removes a specified goal from the repository of incomplete goals
     *
     * @param id - id of the goal to be removed
     */
    public void removeGoalIncomplete (int id){
        goalRepositoryIncomplete.remove(id);
    }

    /**
     * Appends a goal to the repository of completed goals
     *
     * @param goal - the goal to be appended
     */
    public void appendComplete(Goal goal){
        goalRepositoryComplete.append(goal);
        this.goalsCompleted.setValue(goalRepositoryComplete.findAll().getValue());
    }

    /**
     * Appends a goal to the repository of incomplete goals
     *
     * @param goal - the goal to be appended
     */
    public void appendIncomplete(Goal goal){
        goalRepositoryIncomplete.append(goal);
        this.goalsIncompleted.setValue(goalRepositoryIncomplete.findAll().getValue());
    }

    /**
     * Prepends a goal to the repository of completed goals
     *
     * @param goal - the goal to be prepended
     */
    public void prependIncomplete(Goal goal){
        goalRepositoryIncomplete.prepend(goal);
    }

    /**
     * Deletes all of the one-time completed goals and updates any
     * recurring completed goals to their next recurring date as incomplete goals
     *
     * NEW ASSUMPTION (IMPORTANT): When doing the rollover, should the recurring goals get added
     * first or should the incompleted goals get rolld over first? The order of the goals changes based on this
     */
    public void deleteCompleted(){
        LocalDateTime today = getTodayTime();
        var recGoals = goalRepositoryComplete.getRecurringGoals();
        if (recGoals.getValue() == null || recGoals.getValue().isEmpty()){
            System.out.println("ayo bruh this happened");
            goalRepositoryComplete.deleteCompleted(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
            return;
        }
        ArrayList<Goal> toAdd = new ArrayList<>();
        for (var goal : recGoals.getValue()){
            goal.makeInComplete();
            toAdd.add(goal.updateRecurring());
        }
        goalRepositoryComplete.deleteCompleted(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        for (var goal : toAdd){
            goalRepositoryIncomplete.append(goal);
        }
    }


    /**
     * Appends a time to the time database
     *
     * @param localDateTime - the time/date of when last previously opened app
     */
    public void appendTime(LocalDateTime localDateTime){
        timeKeeper.setDateTime(localDateTime);
    }

    /**
     * Deletes time from the time keeper database
     */
    public void deleteTime(){
        timeKeeper.removeDateTime();
    }

    /**
     * Getter for the fields of the time the app was last opened in the database
     *
     * @return Array of fields such as hours, minutes, etc. for the time
     */
    public int[] getFieldsForLastDate() {
        return timeKeeper.getFields();
    }


    public void removeGoalFromRecurringList (int id){
//        List<Goal> listOfGoalsWithId = (List<Goal>) goalRepositoryRecurring.findListOfGoalsById(id).getValue();
//        for (var goal : listOfGoalsWithId){
//            int newSortOrder = goal.sortOrder();
//            if (goal.isComplete()){
//                removeGoalComplete(id);
//                InsertWithSortOrderAndRecurringToRecurringListComplete(goal, newSortOrder, null);
//            }
//            if (!goal.isComplete()){
//                removeGoalIncomplete(id);
//                InsertWithSortOrderAndRecurringToRecurringListIncomplete(goal, newSortOrder, null);
//            }
//        }
        goalRepositoryRecurring.remove(id);
    }

    public void appendToRecurringList(Goal goal){
        goalRepositoryRecurring.append(goal);
    }

    public Subject<List<Goal>> getGoalsFromRecurringList(){
//        System.out.println("it printed bruh");
//        var goalSubject = goalRepositoryRecurring.findAll();
//        var goalList = goalSubject.getValue();
//        if (goalList == null) {
//            System.out.println("early return");
//            var toReturn = new SimpleSubject<List<Goal>>();
//            toReturn.setValue(List.of());
//            return toReturn;
//        }
//        goalList.stream()
//                .sorted(Comparator.comparingInt(Goal::getYear).thenComparing(Goal::getMonth)
//                        .thenComparing(Goal::getDay).thenComparing(Goal::getHour).thenComparing(Goal::getMinutes))
//                .collect(Collectors.toList());
//        for (var goal: goalSubject.getValue()){
//            System.out.println(goal.getText());
//        }
//        System.out.println("It's empty");
//        return goalSubject;
        return recurringGoals;
    }


    public void InsertWithSortOrderToRecurringListComplete(Goal goal, int sortOrder){
        goalRepositoryComplete.InsertWithSortOrder(goal, sortOrder);
    }

    public void InsertWithSortOrderToRecurringListIncomplete(Goal goal, int sortOrder){
        goalRepositoryIncomplete.InsertWithSortOrder(goal, sortOrder);
    }

    public void InsertWithSortOrderAndRecurringToRecurringListComplete(Goal goal, int sortOrder, String recurring){
        goalRepositoryComplete.InsertWithSortOrderAndRecurring(goal, sortOrder, recurring);
    }
    public void InsertWithSortOrderAndRecurringToRecurringListIncomplete(Goal goal, int sortOrder, String recurring){
        goalRepositoryIncomplete.InsertWithSortOrderAndRecurring(goal, sortOrder, recurring);
    }
    public LocalDateTime getTodayTime() {
        int[] timeFields = ActualTimeKeeper.getFields();
        return LocalDateTime.of(timeFields[0], timeFields[1], timeFields[2],
                timeFields[3], timeFields[4]);
    }

    public void updateTodayTime(LocalDateTime localDateTime){
        this.ActualTimeKeeper.removeDateTime();
        this.ActualTimeKeeper.setDateTime(localDateTime);
    }

    public void setContext(int context){
        contextRepository.setContext(context);
    }

    public void setContextWithBoolean(int context, boolean update){
        contextRepository.setContextWithBoolean(context, update);
    }

    public boolean getCurrUpdateValue(){
        return contextRepository.getCurrentUpdateValue();
    }

    public void removeContext(){
        contextRepository.removeContext();
    }

    public int getCurrentContextValue(){
        return contextRepository.getContext();
    }

    public int getMaxGoalPair() {
        return contextRepository.getContext();
    }
}