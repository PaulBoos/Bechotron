package Modules;

public interface Module {
	
	String getDescription();
	
	default void init() {}
	
}
