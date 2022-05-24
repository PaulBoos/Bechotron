package Modules.TestModule;

import Head.BotInstance;
import Modules.Module;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Test extends ListenerAdapter implements Module {

	BotInstance bot;
	
	public Test(BotInstance instance) {
	
	}
	
	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		if(event.getChannel().getIdLong() == 937710341998120980L)
			event.editButton(event.getButton().asDisabled()).queue();
	}
	
	@Override
	public String getDescription() {
		return "testmodule";
	}
}
