package interfaces;

import enums.Direction;

/**
 * Button present outside the elevator on every floor.
 * Two instances per floor: UP and DOWN.
 */
public interface OutsideButton extends Button {
    int       getFloor();
    Direction getDirection();
}
