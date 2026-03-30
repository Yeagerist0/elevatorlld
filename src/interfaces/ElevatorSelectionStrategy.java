package interfaces;

import enums.Direction;
import models.ElevatorCar;

import java.util.List;

/**
 * Strategy interface for selecting which elevator responds to an external request.
 * Implement this to add new dispatching algorithms without touching any other class.
 */
public interface ElevatorSelectionStrategy {
    ElevatorCar selectElevator(List<ElevatorCar> elevators, int requestedFloor, Direction direction);
}
