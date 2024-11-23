package iit.y3.oopcw.exceptions;

public class PreConditionFailedException extends RuntimeException {

    private static final long serialVersionUID = -792124409755276370L;

    public PreConditionFailedException() {
        super();
    }

    public PreConditionFailedException(final String message) {
        super(message);
    }
}