package Head;

import Modules.Module;
import net.dv8tion.jda.api.JDA;

public interface BotModule extends Module {
	
	void init(JDA jda);
	
}
