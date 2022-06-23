package Modules.Suggester;

import Modules.Module;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SuggesterModule extends ListenerAdapter implements Module {
	
	
	
	@Override
	public String getDescription() {
		return "This Module allows people to make suggestions.";
	}
	
}
