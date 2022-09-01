package Modules.Inventory;

import Modules.Module;
import Modules.RequireModuleHook;

import java.util.List;

public class InventoryModule implements Module {
	
	private static final RequireModuleHook HOOK = new RequireModuleHook();
	
	@Override
	public String getDescription() {
		return null;
	}
	
	@Override
	public String getName() {
		return "Inventory Module";
	}
	
	@Override
	public void init() {
	
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
