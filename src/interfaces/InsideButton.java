package interfaces;

import enums.ButtonType;

/**
 * Button present inside the elevator cabin.
 * Types: OPEN_DOOR, CLOSE_DOOR, FLOOR_NUMBER, ALARM.
 */
public interface InsideButton extends Button {
    ButtonType getButtonType();
}
