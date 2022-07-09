package Modules.Calculator;

public class IllegalMathsException extends Exception {
	
	public IllegalMathsException() {
	}
	
	public IllegalMathsException(String message) {
		super(message);
	}
	
	public IllegalMathsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalMathsException(Throwable cause) {
		super(cause);
	}
	
	public IllegalMathsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
