package sa.alrakeen.exceptions;

public class AuthorizationFailedException extends SecurityException {

    public AuthorizationFailedException() {
        super();
    }

    public AuthorizationFailedException(String message) {
        super(message);
    }

    public AuthorizationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
