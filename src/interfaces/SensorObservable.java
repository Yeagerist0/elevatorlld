package interfaces;

/**
 * Observable side of the sensor pattern.
 * FloorSensor and WeightSensor implement this.
 */
public interface SensorObservable {
    void addObserver(SensorObserver observer);
    void removeObserver(SensorObserver observer);
    void notifyObservers(Object value);
}
