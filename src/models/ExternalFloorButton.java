package models;

import enums.Direction;
import interfaces.ExternalRequestHandler;
import interfaces.OutsideButton;

/**
 * Physical button installed outside the elevator on each floor.
 * Notifies the ElevatorController (via ExternalRequestHandler interface)
 * so that exactly one elevator is dispatched.
 */
public class ExternalFloorButton implements OutsideButton {

    private final int                    floor;
    private final Direction              direction;
    private final ExternalRequestHandler handler;

    private ExternalFloorButton(int floor, Direction direction, ExternalRequestHandler handler) {
        this.floor     = floor;
        this.direction = direction;
        this.handler   = handler;
    }

    public static ExternalFloorButton create(int floor, Direction direction,
                                             ExternalRequestHandler handler) {
        return new ExternalFloorButton(floor, direction, handler);
    }

    @Override
    public void press() {
        System.out.println("  [Outside] Floor " + floor + " " + direction + " button pressed.");
        handler.handleExternalRequest(floor, direction);
    }

    @Override public int       getFloor()     { return floor; }
    @Override public Direction getDirection() { return direction; }
}
