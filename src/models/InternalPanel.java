package models;

import enums.ButtonType;
import interfaces.InsideButton;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel inside the elevator cabin.
 * Contains:
 *   - One floor-number button per floor
 *   - Open door button
 *   - Close door button
 *   - Alarm button
 *
 * Each button is an anonymous InsideButton that delegates to ElevatorCar.
 * Created by ElevatorController after the car is built.
 */
public class InternalPanel {

    private final Map<Integer, InsideButton> floorButtons = new LinkedHashMap<>();
    private final InsideButton openButton;
    private final InsideButton closeButton;
    private final InsideButton alarmButton;

    private InternalPanel(ElevatorCar car, List<Integer> floors) {
        for (int floor : floors) {
            final int f = floor;
            floorButtons.put(floor, new InsideButton() {
                @Override public void press()             { car.addDestination(f); }
                @Override public ButtonType getButtonType() { return ButtonType.FLOOR_NUMBER; }
            });
        }

        openButton = new InsideButton() {
            @Override public void press()             { car.openDoor(); }
            @Override public ButtonType getButtonType() { return ButtonType.OPEN_DOOR; }
        };

        closeButton = new InsideButton() {
            @Override public void press()             { car.closeDoor(); }
            @Override public ButtonType getButtonType() { return ButtonType.CLOSE_DOOR; }
        };

        alarmButton = new InsideButton() {
            @Override public void press()             { car.triggerAlarm(); }
            @Override public ButtonType getButtonType() { return ButtonType.ALARM; }
        };
    }

    public static InternalPanel create(ElevatorCar car, List<Integer> floors) {
        return new InternalPanel(car, floors);
    }

    public void pressFloor(int floor) {
        InsideButton btn = floorButtons.get(floor);
        if (btn != null) btn.press();
        else System.out.println("  Floor " + floor + " not in this elevator's panel.");
    }

    public void pressOpen()  { openButton.press(); }
    public void pressClose() { closeButton.press(); }
    public void pressAlarm() { alarmButton.press(); }
}
