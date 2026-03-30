package models;

import interfaces.SensorObservable;
import interfaces.SensorObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable sensor attached to each elevator.
 * Fires whenever the elevator reaches a new floor.
 * ElevatorCar subscribes to this and updates its currentFloor accordingly.
 */
public class FloorSensor implements SensorObservable {

    private static final String TYPE = "FLOOR_SENSOR";

    private int currentFloor;
    private final List<SensorObserver> observers = new ArrayList<>();

    private FloorSensor(int initialFloor) {
        this.currentFloor = initialFloor;
    }

    public static FloorSensor create(int initialFloor) {
        return new FloorSensor(initialFloor);
    }

    /** Called by the elevator movement simulation when a floor is reached. */
    public void detectFloor(int floor) {
        this.currentFloor = floor;
        notifyObservers(floor);
    }

    public int getCurrentFloor() { return currentFloor; }

    @Override
    public void addObserver(SensorObserver o)    { observers.add(o); }

    @Override
    public void removeObserver(SensorObserver o) { observers.remove(o); }

    @Override
    public void notifyObservers(Object value) {
        for (SensorObserver o : observers) o.onSensorUpdate(TYPE, value);
    }
}
