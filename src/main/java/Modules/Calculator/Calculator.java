package Modules.Calculator;

import Modules.Module;
import Modules.RequireModuleHook;

import java.util.List;

public class Calculator implements Module {
	
	private RequireModuleHook HOOK = new RequireModuleHook();
	
	@Override
	public String getDescription() {
		return "This module is a Calculator! Type some Math expression and I will try to solve it (Like Wolfram Alpha, but stupid)";
	}
	
	@Override
	public String getName() {
		return "Calculator Module";
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
