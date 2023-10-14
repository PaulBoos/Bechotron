package Utils;

public class StringHelper {
	
	public static String capitalizeFirstLetter(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}
	
	
	public static String capitalizeWords(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		
		StringBuilder result = new StringBuilder();
		boolean wordStart = true; // Flag to track when we start a new word
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (Character.isLetter(c)) { // Check if the character is a letter
				if (wordStart) {
					c = Character.toUpperCase(c); // Capitalize the first letter of the word
					wordStart = false;
				} else {
					c = Character.toLowerCase(c); // Convert all other letters in the word to lowercase
				}
			} else {
				wordStart = true; // We have encountered a non-letter character, so the next character will be the start of a new word
				if (c != '_') {
					result.append(' ');
				}
			}
			result.append(c);
		}
		
		return result.toString();
	}
	
	public static String capitalizeFirstLetterLowerAfter(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}
	
	public static String capitalizeFirstLettersAndSplitWords(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		
		StringBuilder result = new StringBuilder();
		boolean wordStart = true; // Flag to track when we start a new word
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if(Character.isUpperCase(c)) {
				result.append(' ');
				wordStart = true;
			}
			if (Character.isLetter(c)) { // Check if the character is a letter
				if (wordStart) {
					c = Character.toUpperCase(c); // Capitalize the first letter of the word
					wordStart = false;
				} else {
					c = Character.toLowerCase(c); // Convert all other letters in the word to lowercase
				}
			} else {
				wordStart = true; // We have encountered a non-letter character, so the next character will be the start of a new word
				if (c != '_') {
					result.append(' ');
				}
			}
			result.append(c);
		}
		return result.toString();
	}
	
	public static class Bar {
		
		/**
		 * Formats a simple bar.
		 * @param bar      The bar design.
		 * @param marker   The marker design.
		 * @param length   The length of the bar.
		 * @param location The location of the marker.
		 * @return         The formatted bar.
		 */
		public static String formatBarWithMarker(String bar, String marker, int length, int location) {
			return bar.repeat(location) + marker + bar.repeat(length - location);
		}
		
		/**
		 * Formats a bar with only a positive side and different bar-designs on the left and right.
		 * @param before   The bar-design on the left side of the bar.
		 * @param marker   The marker on the bar.
		 * @param after    The bar-design on the right side of the bar.
		 * @param length   The length of the bar.
		 * @param location The location of the marker.
		 * @return         The formatted bar.
		 */
		public static String formatBarWithMarkerTwoSided(String before, String marker, String after, int length, int location) {
			return before.repeat(location) + marker + after.repeat(length - location);
		}
		
		/**
		 * Formats a bar with a "Zero Marker" in the middle and a positive and negative side.
		 * @param bar      The bar character to use.
		 * @param marker   The marker character to use.
		 * @param zero     The zero marker character to use.
		 * @param length   The length of the bar.
		 * @param location The location of the marker.
		 * @return         The formatted bar.
		 */
		public static String formatBarWithMarkerRealNumber(String bar, String marker, String zero, int length, int location) {
			if(location == 0)
				return formatBarWithMarker(bar, zero, length/2*2, length/2);
			else if(location < 0)
				return formatBarWithMarker(bar, marker, length/2, Math.max(location,-length/2-1) + 1 + length/2) + zero + bar.repeat(length/2);
			else
				return bar.repeat(length/2) + zero + formatBarWithMarker(bar, marker, length/2, Math.min(location, length/2+1)-1);
			
		}
		
	}
	
}
