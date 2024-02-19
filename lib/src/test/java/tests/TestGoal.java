package tests;

import org.junit.Test;


import static org.junit.Assert.*;

import edu.ucsd.cse110.successorator.lib.domain.Goal;


public class TestGoal {

    @Test
    public void makeComplete_changesStatusToComplete() {
        Goal goal = new Goal(1, "Midterm", false, 2);

        goal.makeComplete();

        assertTrue(goal.isComplete());
    }

    @Test
    public void makeInComplete_changesStatusToIncomplete() {
        Goal goal = new Goal(2, "Grocery", true, 1);

        goal.makeInComplete();

        assertFalse(goal.isComplete());
    }

    @Test
   public void equals_returnsTrueForSameObjects() {
        Goal goal1 = new Goal(3, "Finish project", true, 3);
        Goal goal2 = goal1;

        assertTrue(goal1.equals(goal2));
    }

    @Test
    public void equals_returnsTrueForEquivalentObjects() {
        Goal goal1 = new Goal(4, "Exercise daily", true, 4);
        Goal goal2 = new Goal(4, "Exercise daily", true, 4);

        assertTrue(goal1.equals(goal2));
    }

    @Test
    public void equals_returnsFalseForDifferentIds() {
        Goal goal1 = new Goal(5, "Read a book", false, 5);
        Goal goal2 = new Goal(6, "Read a book", false, 5);

        assertFalse(goal1.equals(goal2));
    }

    @Test
   public void equals_returnsFalseForDifferentTexts() {
        Goal goal1 = new Goal(7, "Study for exam", true, 6);
        Goal goal2 = new Goal(7, "Practice for exam", true, 6);

        assertFalse(goal1.equals(goal2));
    }

    @Test
    public void equals_returnsFalseForDifferentCompletionStatuses() {
        Goal goal1 = new Goal(8, "Cook dinner", false, 7);
        Goal goal2 = new Goal(8, "Cook dinner", true, 7);

        assertFalse(goal1.equals(goal2));
    }

    @Test
    public void equals_handlesNullObject() {
        Goal goal = new Goal(9, "Relax", true, 8);

        assertFalse(goal.equals(null));
    }

    @Test
    public void equals_handlesDifferentObjectTypes() {
        Goal goal = new Goal(10, "Meditate", true, 9);

        assertFalse(goal.equals(new String()));
    }

    @Test
    public void hashCode_returnsSameHashForEquivalentObjects() {
        Goal goal1 = new Goal(11, "Travel", true, 10);
        Goal goal2 = new Goal(11, "Travel", true, 10);

        assertEquals(goal1.hashCode(), goal2.hashCode());
    }

    @Test
    public void hashCode_returnsDifferentHashesForDifferentObjects() {
        Goal goal1 = new Goal(12, "Sleep", false, 11);
        Goal goal2 = new Goal(13, "Sleep", false, 11);

        assertNotEquals(goal1.hashCode(), goal2.hashCode());
    }
}
