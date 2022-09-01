package Modules;

import java.util.List;

public interface Module {
	
	String getDescription();
	
	String getName();
	
	void init();
	
	List<RequireModuleHook> requireModules();
	RequireModuleHook getMyRequireModuleHook();
	
}
