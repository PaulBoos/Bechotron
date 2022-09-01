package Utils.Time;

public enum TimeUnit {
	
	MILLISECOND(1),
	SECOND(1_000),
	MINUTE(60_000),
	HOUR(3_600_000),
	DAY(86_400_000),
	WEEK(604_800_000),
	MONTH(2_592_000_000L),
	YEAR(31_536_000_000L);
	
	private final long millis;
	
	TimeUnit(long millis) {
		this.millis = millis;
	}
	
	public double getAs(TimeUnit unit) {
		return (float) millis / unit.millis;
	}
	
}
