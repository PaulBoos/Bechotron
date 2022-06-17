package Utils;

public class DayTime {
	
	private int millis;
	
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
			case 1 -> this.millis = checkSize("Hours", 0, Integer.parseInt(strings[0]), 23) * 3600_000;
			case 2 -> this.millis =
					checkSize("Hours", 0, Integer.parseInt(strings[0]), 23) * 3600_000
					+ checkSize("Minutes", 0, Integer.parseInt(strings[1]), 59) * 60_000;
			case 3 -> {
				String[] split = strings[2].split("\\.");
				if(split.length == 1) {
					this.millis =
							checkSize("Hours", 0, Integer.parseInt(strings[0]), 23) * 3600_000
							+ checkSize("Minutes", 0, Integer.parseInt(strings[1]), 59) * 60_000
							+ checkSize("Seconds", 0, Integer.parseInt(split[0]), 59) * 1000;
				} else if(split.length == 2) {
					this.millis =
							checkSize("Hours", 0, Integer.parseInt(strings[0]), 23) * 3600_000
							+ checkSize("Minutes", 0, Integer.parseInt(strings[1]), 59) * 60_000
							+ checkSize("Seconds", 0, Integer.parseInt(split[0]), 59) * 1000
							+ checkSize("Milliseconds", 0, (int) (Double.parseDouble("0." + split[1]) * 1000), 999);
				} else throw new IllegalArgumentException("Supply DayTime in one of the following formats:\n  HH:MM:SS.000\n  HH:MM:SS\n  HH:MM\n  HH");
			}
			default -> throw new IllegalArgumentException("Supply DayTime in one of the following formats:\n  HH:MM:SS.000\n  HH:MM:SS\n  HH:MM\n  HH");
		}
	}
	
	/**
	 * Shift the time within the object.
	 * @param unit the unit to increment.
	 * @param shift the value to increment by.
	 * @return the same object.
	 */
	public DayTime shiftTime(Unit unit, int shift) {
		switch(unit) {
			case HOURS -> millis += shift * 3600_000;
			case MINUTES -> millis += shift * 60_000;
			case SECONDS -> millis += shift * 1000;
			case MILLIS -> millis += shift;
		}
		millis %= 86_400_000;
		return this;
	}
	
	public int getMillis() {
		return millis;
	}
	
	private int checkSize(String name, int min, int var, int max) {
		if(var >= min && var <= max) return var;
		else throw new IllegalArgumentException(name + " must be between " + min + " and " + max + " (value is " + var + ")");
	}
	
	@Override
	public String toString() {
		return String.format("DayTime(%02d:%02d:%02d.%03d)", (millis -(millis % 3600_000)) / 3600_000, ((millis % 3600_000) -(millis % 60_000)) / 60_000, ((millis % 60_000) -(millis % 1000)) / 1000, millis % 1000);
	}
	
	public enum Unit {
		HOURS, MINUTES, SECONDS, MILLIS
	}
	
}
