# Elevator System — Low-Level Design

Java implementation of a multi-elevator dispatch system, built around the classic elevator LLD interview problem. The demo drives a 10-floor building with two elevators through ten scenarios covering dispatch, panels, sensors, maintenance, and a runtime strategy swap.

**Dispatch strategy.** Pressing an external up/down button on a floor asks the active `DispatchStrategy` to pick an elevator. Two strategies are implemented — `NearestElevatorStrategy` (closest idle car) and `OddEvenFloorStrategy` (odd floors go to odd-numbered elevators) — and the controller can swap between them at runtime without restarting the system.

**Internal panel.** Each elevator exposes floor selection, door open/close, and an alarm button, independent of how it was dispatched.

**Observer pattern.** `FloorSensor` and `WeightSensor` notify registered listeners on events; a weight sensor firing above capacity triggers an overload alert.

**Operator role.** An `Operator` can add floors and toggle an elevator into or out of maintenance mode; elevators under maintenance are skipped by the dispatch strategy.

## Structure

`controller/`, `models/`, `strategies/`, `interfaces/`, `enums/`, and `Main.java` (a runnable demo that walks through all ten scenarios with console output).

## Run

Compile with `javac` and run `Main`, or open the project in your IDE. It's a self-contained console demo with no external dependencies.
