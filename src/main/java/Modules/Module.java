package Modules;

import java.util.List;

public interface Module {
	
	String getDescription();
	
	String getName();
	
	List<RequireModuleHook> requireModules();
	RequireModuleHook getMyRequireModuleHook();
	
}
