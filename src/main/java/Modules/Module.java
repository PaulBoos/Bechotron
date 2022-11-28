package Modules;

import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public interface Module {
	
	void init(Guild guild);
	
	String getDescription();
	
	String getName();
	
	List<RequireModuleHook> requireModules();
	RequireModuleHook getMyRequireModuleHook();
	
}
