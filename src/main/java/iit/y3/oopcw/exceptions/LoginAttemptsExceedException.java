package iit.y3.oopcw.exceptions;

public class LoginAttemptsExceedException extends RuntimeException{

    public LoginAttemptsExceedException() {
        super();
    }

    public LoginAttemptsExceedException(final String message) {
        super(message);
    }
}
