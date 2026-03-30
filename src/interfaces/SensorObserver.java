package interfaces;

/**
 * Observer side of the sensor pattern.
 * ElevatorCar implements this to react to floor-sensor and weight-sensor events.
 */
public interface SensorObserver {
    void onSensorUpdate(String sensorType, Object value);
}
