package Modules.Games.Werwolf.Exceptions;

import Modules.Games.Werwolf.GameState;

public class WerewolfRuntimeException extends WerewolfException {
	
	WerewolfRuntimeException(String message) {
		super(message);
	}
	
	public static class WrongGameStateException extends WerewolfRuntimeException {
		WrongGameStateException(GameState expectedState, GameState currentState) {
			super("[ERROR] The game is not in the required state: " + expectedState + " but " + currentState);
		}
	}
	public static class GameErrorException extends WerewolfRuntimeException {
		GameErrorException(String message) {
			super("[ERROR] The game experienced an irrecoverable Error: " + message);
		}
	}
	
}
