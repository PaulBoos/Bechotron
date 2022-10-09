package Utils;

import Modules.Calculator.IllegalMathsException;

public class Numbers {
	
	/**
	 * @return true if adding a + b would overflow Integer.
	 */
	public static boolean sumIsOverflowSafe(int a, int b) {
		return b > 0 ? (Integer.MAX_VALUE - a >= b) // If b is positive, b must be smaller than the distance between MAX and a
				: (Integer.MIN_VALUE - a <= b); // If b is negative, b must be smaller (bigger) than the distance between MIN and a
	}
	
	/**
	 * Exception is thrown when an integer would (or does) overflow.
	 */
	public static class IntegerOverflowException extends IllegalMathsException {
		public IntegerOverflowException() {
		}
		
		public IntegerOverflowException(String message) {
			super(message);
		}
		
		public IntegerOverflowException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public IntegerOverflowException(Throwable cause) {
			super(cause);
		}
		
		public IntegerOverflowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}
	
}
