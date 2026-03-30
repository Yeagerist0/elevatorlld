package interfaces;

import enums.Direction;

/**
 * Abstraction used by outside buttons to dispatch a pickup request.
 * ElevatorController implements this — Floor/buttons never depend on the controller class directly.
 */
public interface ExternalRequestHandler {
    void handleExternalRequest(int floor, Direction direction);
}
