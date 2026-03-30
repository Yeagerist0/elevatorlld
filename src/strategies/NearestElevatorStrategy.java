package strategies;

import enums.Direction;
import enums.ElevatorState;
import interfaces.ElevatorSelectionStrategy;
import models.ElevatorCar;

import java.util.List;

/**
 * Default strategy: dispatches the elevator closest to the requested floor.
 *
 * Tie-breaking priority:
 *   1. IDLE elevator
 *   2. Elevator already moving in the same direction and hasn't passed the floor
 *   3. Any other operational elevator (by raw distance)
 */
public class NearestElevatorStrategy implements ElevatorSelectionStrategy {

    private NearestElevatorStrategy() {}

    public static NearestElevatorStrategy create() {
        return new NearestElevatorStrategy();
    }

    @Override
    public ElevatorCar selectElevator(List<ElevatorCar> elevators,
                                      int requestedFloor, Direction direction) {
        ElevatorCar best   = null;
        int         minScore = Integer.MAX_VALUE;

        for (ElevatorCar car : elevators) {
            if (!car.isOperational()) continue;

            int dist  = Math.abs(car.getCurrentFloor() - requestedFloor);
            int score = dist;

            // Bias: heavily prefer idle elevators
            if (car.getState() == ElevatorState.IDLE) {
                score -= 10_000;
            }
            // Prefer elevators already heading in the right direction and not past the floor
            else if (car.getState() == ElevatorState.MOVING_UP
                     && direction == Direction.UP
                     && car.getCurrentFloor() <= requestedFloor) {
                score -= 5_000;
            } else if (car.getState() == ElevatorState.MOVING_DOWN
                     && direction == Direction.DOWN
                     && car.getCurrentFloor() >= requestedFloor) {
                score -= 5_000;
            }

            if (score < minScore) {
                minScore = score;
                best     = car;
            }
        }
        return best;
    }
}
