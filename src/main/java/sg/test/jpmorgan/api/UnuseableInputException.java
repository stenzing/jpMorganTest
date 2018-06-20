package sg.test.jpmorgan.api;

public class UnuseableInputException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnuseableInputException(String message) {
		super(message);
	}
}
