package iit.y3.oopcw.exceptions;

public class PreConditionFailedCustomException extends RuntimeException {

    private static final long serialVersionUID = -792124409755276370L;

	public String userName;
    
    public PreConditionFailedCustomException() {
        super();
    }

    public PreConditionFailedCustomException(final String message,final String userName) {
        super(message);
        this.userName = userName;
    }

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}