package strategies;

import enums.Direction;
import interfaces.ElevatorSelectionStrategy;
import models.ElevatorCar;

import java.util.List;

/**
 * Alternate strategy: odd-numbered floors go to odd-id elevators,
 * even-numbered floors go to even-id elevators.
 * Falls back to NearestElevatorStrategy if the dedicated car is unavailable.
 *
 * Purpose: demonstrates that new dispatching algorithms can be plugged in
 * by implementing ElevatorSelectionStrategy — zero changes to any other class.
 */
public class OddEvenFloorStrategy implements ElevatorSelectionStrategy {

    private final NearestElevatorStrategy fallback = NearestElevatorStrategy.create();

    private OddEvenFloorStrategy() {}

    public static OddEvenFloorStrategy create() {
        return new OddEvenFloorStrategy();
    }

    @Override
    public ElevatorCar selectElevator(List<ElevatorCar> elevators,
                                      int requestedFloor, Direction direction) {
        boolean wantOdd = (requestedFloor % 2 != 0);

        for (ElevatorCar car : elevators) {
            if (!car.isOperational()) continue;
            if ((car.getId() % 2 != 0) == wantOdd) return car;
        }

        // Dedicated elevator unavailable — fall back to nearest
        System.out.println("  [OddEven] Dedicated car unavailable, falling back to nearest.");
        return fallback.selectElevator(elevators, requestedFloor, direction);
    }
}
