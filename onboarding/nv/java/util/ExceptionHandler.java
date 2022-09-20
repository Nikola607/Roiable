package onboarding.nv.java.util;

public class ExceptionHandler extends Exception {

	private String message;

	public ExceptionHandler(String message) {
		super();
		message = this.message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
