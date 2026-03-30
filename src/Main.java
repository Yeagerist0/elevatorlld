import controller.ElevatorController;
import models.ElevatorCar;
import models.Operator;
import strategies.NearestElevatorStrategy;
import strategies.OddEvenFloorStrategy;

/**
 * Demo: 10-floor building, 2 elevators, 700 kg weight limit each.
 *
 * Covers all required scenarios:
 *   1.  Outside button (UP/DOWN) dispatches exactly one elevator
 *   2.  Inside panel: floor selection, open, close, alarm
 *   3.  Observer pattern: FloorSensor and WeightSensor notifications
 *   4.  Maintenance mode (only an Operator can set it)
 *   5.  Operator: addFloor, setMaintenance
 *   6.  Strategy swap at runtime (Nearest -> OddEven)
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("═══════════ ELEVATOR SYSTEM BOOT ═══════════\n");

        // ── 1. Build the controller with the default strategy ─────────────────
        ElevatorController controller = ElevatorController.create(NearestElevatorStrategy.create());

        // ── 2. Add floors 0-9 ────────────────────────────────────────────────
        System.out.println("--- Setting up floors ---");
        for (int i = 0; i <= 9; i++) controller.addFloor(i);

        // ── 3. Add elevators ─────────────────────────────────────────────────
        System.out.println("\n--- Setting up elevators ---");
        ElevatorCar e1 = ElevatorCar.create(1, 0, 700);   // starts at ground
        ElevatorCar e2 = ElevatorCar.create(2, 5, 700);   // starts at floor 5
        controller.addElevator(e1);
        controller.addElevator(e2);

        // ── 4. Create operator ───────────────────────────────────────────────
        Operator operator = Operator.create("Ramesh", controller);

        controller.printStatus();

        // ─────────────────────────────────────────────────────────────────────
        System.out.println("=== Scenario 1: Outside UP button on floor 3 ===");
        // E2 is at floor 5 (dist=2), E1 at floor 0 (dist=3)  →  E2 is dispatched
        controller.getFloor(3).pressUp();

        System.out.println("\n=== Scenario 2: Passenger inside E1 presses floor 7 ===");
        e1.getInternalPanel().pressFloor(7);

        System.out.println("\n=== Scenario 3: Outside DOWN button on floor 8 ===");
        controller.getFloor(8).pressDown();

        System.out.println("\n=== Scenario 4: Open & Close door buttons inside E1 ===");
        e1.getInternalPanel().pressOpen();
        e1.getInternalPanel().pressClose();

        System.out.println("\n=== Scenario 5: Alarm button inside E2 ===");
        e2.getInternalPanel().pressAlarm();

        System.out.println("\n=== Scenario 6: WeightSensor fires — overload on E1 ===");
        e1.getWeightSensor().detectWeight(750.0);   // observer notified, alert printed

        System.out.println("\n=== Scenario 7: Operator puts E2 into maintenance ===");
        operator.setMaintenance(2, true);

        System.out.println("\n=== Scenario 8: Floor 6 UP — only E1 operational ===");
        controller.getFloor(6).pressUp();           // must go to E1, E2 is out of service

        System.out.println("\n=== Scenario 9: Operator restores E2 ===");
        operator.setMaintenance(2, false);

        System.out.println("\n=== Scenario 10: Runtime strategy swap to OddEven ===");
        controller.setStrategy(OddEvenFloorStrategy.create());

        System.out.println("\n--- Floor 5 DOWN  (odd floor -> odd-id elevator = E1) ---");
        controller.getFloor(5).pressDown();

        System.out.println("\n--- Floor 4 UP  (even floor -> even-id elevator = E2) ---");
        controller.getFloor(4).pressUp();

        System.out.println("\n=== Scenario 11: Operator adds floor 10 ===");
        operator.addFloor(10);

        controller.printStatus();
    }
}
