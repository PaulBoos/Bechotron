package Utils;

import java.util.HashMap;

public class StringAccumulator {
	
	private HashMap<String, Integer> strings;
	
	public StringAccumulator() {
		strings = new HashMap<>();
	}
	
	public void addString(String error) {
		if(strings.containsKey(error)) {
			strings.put(error, strings.get(error) + 1);
		} else {
			strings.put(error, 1);
		}
	}
	
	public void addString(String input, int count) {
		if(strings.containsKey(input)) {
			strings.put(input, strings.get(input) + count);
		} else {
			strings.put(input, count);
		}
	}
	
	public void addString(StringAccumulator stringAccumulator) {
		for(String error : stringAccumulator.getStrings().keySet()) {
			addString(error, stringAccumulator.getStrings().get(error));
		}
	}
	
	public HashMap<String, Integer> getStrings() {
		return strings;
	}
	
	public void clear() {
		strings.clear();
	}
	
	public boolean hasStrings() {
		return !strings.isEmpty();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String error : strings.keySet()) {
			sb.append(error).append(" (").append(strings.get(error)).append(")\n");
		}
		return sb.toString();
	}
	
}
