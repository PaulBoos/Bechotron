package Modules;

import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public interface Module {
	
	default Module load(Guild guild) {
		return this;
	}
	default Module unload(Guild guild) {
		return this;
	}
	String getName();
	String getDescription();
	List<RequireModuleHook> requireModules();
	RequireModuleHook getMyRequireModuleHook();
	
}
