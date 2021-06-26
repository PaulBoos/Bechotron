package Modules.SlashCommands;

public class CommandException extends Exception {
	
	CommandException() {
		super();
	}
	CommandException(String message) {
		super(message);
	}
	CommandException(String message, Throwable reason) {
		super(message, reason);
	}
	CommandException(Throwable reason) {
		super(reason);
	}
	
}