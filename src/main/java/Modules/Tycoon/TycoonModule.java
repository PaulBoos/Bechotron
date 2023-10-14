package Modules.Tycoon;

import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class TycoonModule extends ListenerAdapter implements Module {
	private static final RequireModuleHook HOOK = new RequireModuleHook();
	
	@Override
	public String getDescription() {
		return "Take part in my global multiplayer tycoon game!";
	}
	
	@Override
	public String getName() {
		return "Tycoon Module";
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
