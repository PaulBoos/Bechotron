package Modules;

public interface GlobalModule extends Module {
	
	@Override
	default String getDescription() {
		return "This Module is global and can not be removed.\n=============================================\n";
	}
	
}
