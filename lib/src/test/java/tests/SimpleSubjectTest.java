package tests;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import androidx.annotation.Nullable;

public class SimpleSubjectTest {

    @Test
    public void getValue_returnsInitialValue() {
        SimpleSubject<String> subject = new SimpleSubject<>();

        assertNull(subject.getValue());
    }

    @Test
    public void setValue_updatesValue() {
        SimpleSubject<String> subject = new SimpleSubject<>();

        subject.setValue("New Value");

       assertEquals("New Value", subject.getValue());
    }

    @Test
    public void observe_addsObserverAndNotifies() {
        SimpleSubject<String> subject = new SimpleSubject<>();
        TestObserver observer = new TestObserver();

        subject.observe(observer);

        assertTrue(subject.getObservers().contains(observer));
        assertTrue(observer.wasNotified);
    }

    @Test
    public void removeObserver_removesObserver() {
        SimpleSubject<String> subject = new SimpleSubject<>();
        TestObserver observer = new TestObserver();
        subject.observe(observer);

        subject.removeObserver(observer);

        assertFalse(subject.getObservers().contains(observer));
    }

    @Test
    public void notifyObservers_callsOnChangedOnAllObservers() {
        SimpleSubject<String> subject = new SimpleSubject<>();
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        subject.observe(observer1);
        subject.observe(observer2);

        subject.setValue("New Value");

       assertTrue(observer1.wasNotified);
       assertTrue(observer2.wasNotified);
    }

    private static class TestObserver implements Observer<String> {
        boolean wasNotified = false;

        @Override
        public void onChanged(@Nullable String newValue) {
            wasNotified = true;
        }
    }
}
