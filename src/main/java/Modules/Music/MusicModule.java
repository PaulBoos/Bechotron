package Modules.Music;

import Modules.Module;
import Modules.Music.MusicManager;

public class MusicModule implements Module {
	
	public static MusicManager manager;
	
	public MusicModule() {
		manager = new MusicManager();
	}
	
}
