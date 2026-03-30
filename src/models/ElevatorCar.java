package models;

import enums.ElevatorState;
import interfaces.SensorObserver;

import java.util.Collections;
import java.util.TreeSet;

/**
 * Core entity representing a single elevator cabin.
 *
 * Implements SensorObserver to react to:
 *   - FloorSensor  -> updates currentFloor, drives state machine
 *   - WeightSensor -> alerts if weight limit is exceeded
 *
 * Movement is simulated by firing the FloorSensor directly at the target floor.
 * In production this would be async callbacks from physical sensors.
 */
public class ElevatorCar implements SensorObserver {

    private final int id;
    private int          currentFloor;
    private ElevatorState state;
    private double        currentWeight;
    private final double  weightLimit;

    private final FloorSensor  floorSensor;
    private final WeightSensor weightSensor;
    private InternalPanel      internalPanel;

    // SCAN-style queues for efficient floor ordering
    private final TreeSet<Integer> upQueue;   // ascending — floors above current
    private final TreeSet<Integer> downQueue; // descending — floors below current

    private ElevatorCar(int id, int initialFloor, double weightLimit) {
        this.id            = id;
        this.currentFloor  = initialFloor;
        this.state         = ElevatorState.IDLE;
        this.currentWeight = 0;
        this.weightLimit   = weightLimit;
        this.floorSensor   = FloorSensor.create(initialFloor);
        this.weightSensor  = WeightSensor.create();
        this.upQueue       = new TreeSet<>();
        this.downQueue     = new TreeSet<>(Collections.reverseOrder());

        floorSensor.addObserver(this);
        weightSensor.addObserver(this);
    }

    public static ElevatorCar create(int id, int initialFloor, double weightLimit) {
        return new ElevatorCar(id, initialFloor, weightLimit);
    }

    // ── Setters called by controller after construction ──────────────────────

    public void setInternalPanel(InternalPanel panel) {
        this.internalPanel = panel;
    }

    // ── Public state accessors ────────────────────────────────────────────────

    public int            getId()            { return id; }
    public int            getCurrentFloor()  { return currentFloor; }
    public ElevatorState  getState()         { return state; }
    public double         getCurrentWeight() { return currentWeight; }
    public double         getWeightLimit()   { return weightLimit; }
    public boolean        isOperational()    { return state != ElevatorState.UNDER_MAINTENANCE; }
    public InternalPanel  getInternalPanel() { return internalPanel; }
    public FloorSensor    getFloorSensor()   { return floorSensor; }
    public WeightSensor   getWeightSensor()  { return weightSensor; }

    // ── Destination management ────────────────────────────────────────────────

    public void addDestination(int floor) {
        if (!isOperational()) {
            System.out.println("  [Elevator " + id + "] Under maintenance — request ignored.");
            return;
        }
        if (floor == currentFloor) {
            openDoor();
            return;
        }
        if (floor > currentFloor) upQueue.add(floor);
        else                       downQueue.add(floor);

        System.out.println("  [Elevator " + id + "] Destination " + floor + " queued.");
        processNext();
    }

    private void processNext() {
        // Only start moving if currently idle
        if (state == ElevatorState.MOVING_UP || state == ElevatorState.MOVING_DOWN) return;

        if (!upQueue.isEmpty()) {
            int target = upQueue.first();
            state = ElevatorState.MOVING_UP;
            System.out.println("  [Elevator " + id + "] Moving UP -> floor " + target);
            floorSensor.detectFloor(target);   // sensor fires on arrival
        } else if (!downQueue.isEmpty()) {
            int target = downQueue.first();
            state = ElevatorState.MOVING_DOWN;
            System.out.println("  [Elevator " + id + "] Moving DOWN -> floor " + target);
            floorSensor.detectFloor(target);
        }
    }

    // ── Door & alarm actions ──────────────────────────────────────────────────

    public void openDoor() {
        if (!isOperational()) return;
        System.out.println("  [Elevator " + id + " @ floor " + currentFloor + "] Door OPENED.");
    }

    public void closeDoor() {
        if (!isOperational()) return;
        System.out.println("  [Elevator " + id + " @ floor " + currentFloor + "] Door CLOSED.");
    }

    public void triggerAlarm() {
        System.out.println("  *** ALARM in Elevator " + id + " at floor " + currentFloor + " ***");
    }

    // ── Operator control ──────────────────────────────────────────────────────

    public void setMaintenance(boolean inMaintenance) {
        if (inMaintenance) {
            state = ElevatorState.UNDER_MAINTENANCE;
        } else {
            state = ElevatorState.IDLE;
        }
        System.out.println("  [Elevator " + id + "] State -> " + state);
    }

    // ── SensorObserver ────────────────────────────────────────────────────────

    @Override
    public void onSensorUpdate(String sensorType, Object value) {
        switch (sensorType) {
            case "FLOOR_SENSOR":
                int arrivedAt = (int) value;
                currentFloor  = arrivedAt;
                upQueue.remove(arrivedAt);
                downQueue.remove(arrivedAt);
                System.out.println("  [Elevator " + id + "] Arrived at floor " + currentFloor);
                openDoor();
                closeDoor();
                if (upQueue.isEmpty() && downQueue.isEmpty()) {
                    state = ElevatorState.IDLE;
                    System.out.println("  [Elevator " + id + "] IDLE at floor " + currentFloor);
                } else {
                    processNext();
                }
                break;

            case "WEIGHT_SENSOR":
                double weight = (double) value;
                currentWeight = weight;
                if (weight > weightLimit) {
                    System.out.println("  *** [Elevator " + id + "] OVERLOAD: "
                        + weight + "kg / limit " + weightLimit + "kg — doors held open! ***");
                }
                break;
        }
    }

    @Override
    public String toString() {
        return "Elevator[id=" + id
            + " | floor=" + currentFloor
            + " | state=" + state
            + " | weight=" + currentWeight + "kg / " + weightLimit + "kg]";
    }
}
