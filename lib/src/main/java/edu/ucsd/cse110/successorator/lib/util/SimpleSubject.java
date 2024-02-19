package edu.ucsd.cse110.successorator.lib.util;

import androidx.annotation.Nullable;

import java.util.List;

public class SimpleSubject<T> implements MutableSubject<T> {
    private @Nullable T value = null;
    private final List<Observer<T>> observers = new java.util.ArrayList<>();

    /**
     * Returns the value of the simple subject
     * @return value of the simple subject
     */
    @Override
    @Nullable
    public T getValue() {
        return value;
    }


    /**
     * Updates the value of the Simple subject
     * @param value The new value of the subject.
     */
    @Override
    public void setValue(T value) {
        this.value = value;
        notifyObservers();
    }

    /**
     * Adds an observer to the list of observers and updates value
     * @param observer The observer to add.
     */
    @Override
    public void observe(Observer<T> observer) {
        observers.add(observer);
        observer.onChanged(value);
    }

    /**
     * Removes a specific observer from the list
     * @param observer The observer to remove.
     */
    @Override
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers of the subject's new value. Used internally.
     */
    private void notifyObservers() {
        for (var observer : observers) {
            observer.onChanged(value);
        }
    }
    public List<Observer<T>> getObservers(){
        return observers;
    }

}
