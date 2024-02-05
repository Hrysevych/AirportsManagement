package sa.alrakeen.exceptions;

public class AirportCodeAlreadyExistsException extends RuntimeException {

    public AirportCodeAlreadyExistsException() {
        super("Airport with such code already exists in the database.");
    }

    public AirportCodeAlreadyExistsException(String message) {
        super(message);
    }

    public AirportCodeAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
