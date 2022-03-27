package Utils.Time;

public class DayTime {
	
	private final int millis;
	
	public DayTime(int millis) {
		this.millis = millis;
	}
	
	/**
	 * @param input Daytime in formats
	 *   HH
	 *   HH:MM
	 *   HH:MM:SS
	 *   HH:MM:SS.000
	 */
	public DayTime(String input) {
		String[] strings = input.split(":");
		switch(strings.length) {
			case 1 -> this.millis = (Integer.parseInt(strings[0]) * 3600_000);
			case 2 -> this.millis = (Integer.parseInt(strings[0]) * 3600_000) + (Integer.parseInt(strings[1]) * 60_000);
			case 3 -> this.millis = (Integer.parseInt(strings[0]) * 3600_000) + (Integer.parseInt(strings[1]) * 60_000) + (Integer.parseInt(strings[2]) * 1000);
			default -> throw new IllegalArgumentException("Supply DayTime in one of the following formats:\n  HH:MM:SS.000\n  HH:MM:SS\n  HH:MM\n  HH");
		}
	}
	
	
	
}
