package models;

import controller.ElevatorController;

/**
 * Operator role: can add new floors and toggle elevator maintenance mode.
 * Only an Operator can put a car into UNDER_MAINTENANCE state.
 */
public class Operator {

    private final String             name;
    private final ElevatorController controller;

    private Operator(String name, ElevatorController controller) {
        this.name       = name;
        this.controller = controller;
    }

    public static Operator create(String name, ElevatorController controller) {
        return new Operator(name, controller);
    }

    /** Adds a new floor to the building (controller registers it and updates all panels). */
    public void addFloor(int floorNumber) {
        System.out.println("[Operator " + name + "] Adding floor " + floorNumber);
        controller.addFloor(floorNumber);
    }

    /** Puts an elevator into maintenance or restores it to service. */
    public void setMaintenance(int elevatorId, boolean inMaintenance) {
        System.out.println("[Operator " + name + "] "
            + (inMaintenance ? "Taking" : "Restoring")
            + " Elevator " + elevatorId
            + (inMaintenance ? " out of service." : " back to service."));
        controller.setElevatorMaintenance(elevatorId, inMaintenance);
    }
}
