package Modules.Suggester;

import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class SuggesterModule extends ListenerAdapter implements Module {
	
	private static final RequireModuleHook HOOK = new RequireModuleHook();
	
	
	
	@Override
	public String getDescription() {
		return "This Module allows people to make suggestions.";
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public List<RequireModuleHook> requireModules() {
		return null;
	}
	
	@Override
	public RequireModuleHook getMyRequireModuleHook() {
		return HOOK;
	}
	
	
}
