package models;

import enums.Direction;
import interfaces.ExternalRequestHandler;

/**
 * Represents a building floor.
 * Holds an UP button and a DOWN button (both ExternalFloorButton).
 * Ground floor's DOWN and top floor's UP are never pressed in practice,
 * but the objects still exist — invalid presses simply have no effect.
 */
public class Floor {

    private final int                 floorNumber;
    private final ExternalFloorButton upButton;
    private final ExternalFloorButton downButton;

    private Floor(int floorNumber, ExternalRequestHandler handler) {
        this.floorNumber = floorNumber;
        this.upButton    = ExternalFloorButton.create(floorNumber, Direction.UP,   handler);
        this.downButton  = ExternalFloorButton.create(floorNumber, Direction.DOWN, handler);
    }

    public static Floor create(int floorNumber, ExternalRequestHandler handler) {
        return new Floor(floorNumber, handler);
    }

    public int getFloorNumber() { return floorNumber; }

    public void pressUp()   { upButton.press(); }
    public void pressDown() { downButton.press(); }

    @Override
    public String toString() { return "Floor[" + floorNumber + "]"; }
}
