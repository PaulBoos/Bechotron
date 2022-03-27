package Modules.Games.Werwolf;

public enum GameState {
	SETUP("[Setup]"),
	ERROR("[ERROR]"),
	LOBBY("[Lobby]"),
	RUNNING("[Running]");
	
	private String display;
	
	GameState(String display) {
		this.display = display;
	}
	
	@Override
	public String toString() {
		return display;
	}
}
