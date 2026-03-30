package models;

import interfaces.SensorObservable;
import interfaces.SensorObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable sensor that monitors cabin load.
 * ElevatorCar subscribes to this; exceeding weightLimit triggers an alert.
 */
public class WeightSensor implements SensorObservable {

    private static final String TYPE = "WEIGHT_SENSOR";

    private double currentWeight;
    private final List<SensorObserver> observers = new ArrayList<>();

    private WeightSensor() {
        this.currentWeight = 0;
    }

    public static WeightSensor create() {
        return new WeightSensor();
    }

    /** Simulates a passenger stepping in/out; fires observer notification. */
    public void detectWeight(double weight) {
        this.currentWeight = weight;
        notifyObservers(weight);
    }

    public double getCurrentWeight() { return currentWeight; }

    @Override
    public void addObserver(SensorObserver o)    { observers.add(o); }

    @Override
    public void removeObserver(SensorObserver o) { observers.remove(o); }

    @Override
    public void notifyObservers(Object value) {
        for (SensorObserver o : observers) o.onSensorUpdate(TYPE, value);
    }
}
