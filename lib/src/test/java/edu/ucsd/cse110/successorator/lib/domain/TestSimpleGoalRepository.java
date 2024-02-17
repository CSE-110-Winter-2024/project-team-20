package edu.ucsd.cse110.successorator.lib.domain;
import edu.ucsd.cse110.successorator.lib.data.DataSource;
import org.junit.Test;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import java.util.List;

import static org.junit.Assert.*;

public class TestSimpleGoalRepository {

    @Test
    public void testConstructor() {
        DataSource dataSource = new DataSource();
        SimpleGoalRepository simpleGoalRepository = new SimpleGoalRepository(dataSource);

        assertSame(dataSource, simpleGoalRepository.getDataSource());
    }

    @Test
    public void testCount() {
        DataSource dataSource = new DataSource();
        SimpleGoalRepository simpleGoalRepository = new SimpleGoalRepository(dataSource);

        // Initial count should be 0
        assertEquals((int)0, (int) simpleGoalRepository.count());

        // Add goals and check count
        simpleGoalRepository.save(new Goal(1, "Goal 1", false));
        assertEquals((int)1, (int) simpleGoalRepository.count());
        simpleGoalRepository.save(new Goal(2, "Goal 2", true));
        assertEquals((int)2, (int) simpleGoalRepository.count());
    }

    @Test
    public void testFind() {
        DataSource dataSource = new DataSource();
        SimpleGoalRepository simpleGoalRepository = new SimpleGoalRepository(dataSource);

        // Add goals
        Goal goal1 = new Goal(1, "Goal 1", false);
        Goal goal2 = new Goal(2, "Goal 2", true);
        simpleGoalRepository.save(goal1);
        simpleGoalRepository.save(goal2);

        // Find by ID
        SimpleSubject<Goal> subject1 = simpleGoalRepository.find(1);
        assertEquals(goal1, subject1.getValue());
        SimpleSubject<Goal> subject2 = simpleGoalRepository.find(2);
        assertEquals(goal2, subject2.getValue());

        // Find non-existent ID
        assertNotNull(simpleGoalRepository.find(3));
    }

    @Test
    public void testFindAll() {
        DataSource dataSource = new DataSource();
        SimpleGoalRepository simpleGoalRepository = new SimpleGoalRepository(dataSource);

        // Add goals
        Goal goal1 = new Goal(1, "Goal 1", false);
        Goal goal2 = new Goal(2, "Goal 2", true);
        simpleGoalRepository.save(goal1);
        simpleGoalRepository.save(goal2);

        // Find all
        List<Goal> allGoals = simpleGoalRepository.findAll().getValue();
        assertEquals(List.of(goal1, goal2), allGoals);
    }

    @Test
    public void testSave() {
        DataSource dataSource = new DataSource();
        SimpleGoalRepository simpleGoalRepository = new SimpleGoalRepository(dataSource);

        // Add goals
        Goal goal1 = new Goal(1, "Goal 1", false);
        Goal goal2 = new Goal(2, "Goal 2", true);
        simpleGoalRepository.save(goal1);
        simpleGoalRepository.save(goal2);

        // Check dataSource and subjects
        assertEquals(List.of(goal1, goal2), dataSource.getGoals());
        assertEquals(goal1, simpleGoalRepository.find(goal1.id()).getValue());
        assertEquals(goal2, simpleGoalRepository.find(goal2.id()).getValue());
        assertEquals(List.of(goal1, goal2), simpleGoalRepository.findAll().getValue());
    }
}
