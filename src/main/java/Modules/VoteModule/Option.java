package Modules.VoteModule;

public class Option {
	
	private final String optionName;
	private final String optionDescription;
	
	public Option(String optionName, String optionDescription) {
		this.optionName = optionName;
		this.optionDescription = optionDescription;
	}
	
	public String getOptionName() {
		return optionName;
	}
	public String getOptionDescription() {
		return optionDescription;
	}
	
}
