package Head;

import Modules.Module;
import net.dv8tion.jda.api.entities.Guild;

public interface GuildModule extends Module {
	
	void init(Guild guild);
	
}
