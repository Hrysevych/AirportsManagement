package sa.alrakeen.exceptions;

public class AirportNameAlreadyExistsException extends RuntimeException {

    public AirportNameAlreadyExistsException() {
        super("Airport with such name already exists in the database.");
    }

    public AirportNameAlreadyExistsException(String message) {
        super(message);
    }

    public AirportNameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
