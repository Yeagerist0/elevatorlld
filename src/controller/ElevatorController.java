package controller;

import enums.Direction;
import interfaces.ElevatorSelectionStrategy;
import interfaces.ExternalRequestHandler;
import models.ElevatorCar;
import models.Floor;
import models.InternalPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Central controller for the elevator system.
 *
 * Responsibilities:
 *   - Maintains the list of elevators and floors
 *   - Implements ExternalRequestHandler: receives outside-button events and
 *     dispatches exactly ONE elevator via the pluggable ElevatorSelectionStrategy
 *   - Allows runtime strategy swapping (setStrategy)
 *   - Exposes operator-level controls (maintenance, addFloor)
 */
public class ElevatorController implements ExternalRequestHandler {

    private final List<ElevatorCar>      elevators;
    private final TreeMap<Integer, Floor> floors;       // sorted by floor number
    private ElevatorSelectionStrategy    strategy;

    private ElevatorController(ElevatorSelectionStrategy strategy) {
        this.elevators = new ArrayList<>();
        this.floors    = new TreeMap<>();
        this.strategy  = strategy;
    }

    public static ElevatorController create(ElevatorSelectionStrategy strategy) {
        return new ElevatorController(strategy);
    }

    // ── Building setup ────────────────────────────────────────────────────────

    /** Registers a floor and installs its outside buttons pointing back to this controller. */
    public void addFloor(int floorNumber) {
        floors.put(floorNumber, Floor.create(floorNumber, this));
        System.out.println("  Floor " + floorNumber + " added.");
    }

    /**
     * Registers an elevator and wires its InternalPanel with the current floor list.
     * Must be called after all initial floors are added.
     */
    public void addElevator(ElevatorCar car) {
        List<Integer> floorNums = new ArrayList<>(floors.keySet());
        InternalPanel panel = InternalPanel.create(car, floorNums);
        car.setInternalPanel(panel);
        elevators.add(car);
        System.out.println("  Elevator " + car.getId() + " registered.");
    }

    // ── ExternalRequestHandler ────────────────────────────────────────────────

    /**
     * Called by outside buttons. Strategy selects exactly one elevator to dispatch.
     */
    @Override
    public void handleExternalRequest(int floor, Direction direction) {
        ElevatorCar selected = strategy.selectElevator(elevators, floor, direction);
        if (selected == null) {
            System.out.println("  [Controller] No operational elevator available!");
            return;
        }
        System.out.println("  [Controller] Dispatching Elevator " + selected.getId()
            + " to floor " + floor + " (" + direction + ")");
        selected.addDestination(floor);
    }

    // ── Strategy swap ─────────────────────────────────────────────────────────

    public void setStrategy(ElevatorSelectionStrategy strategy) {
        this.strategy = strategy;
        System.out.println("  [Controller] Strategy -> " + strategy.getClass().getSimpleName());
    }

    // ── Operator controls ─────────────────────────────────────────────────────

    public void setElevatorMaintenance(int elevatorId, boolean inMaintenance) {
        for (ElevatorCar car : elevators) {
            if (car.getId() == elevatorId) {
                car.setMaintenance(inMaintenance);
                return;
            }
        }
        System.out.println("  Elevator " + elevatorId + " not found.");
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public Floor            getFloor(int n)     { return floors.get(n); }
    public List<ElevatorCar> getElevators()     { return elevators; }

    public void printStatus() {
        System.out.println("\n──── System Status ────");
        elevators.forEach(System.out::println);
        System.out.println("──────────────────────\n");
    }
}
