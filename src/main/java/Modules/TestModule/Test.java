package Modules.TestModule;

import Head.BotInstance;
import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Test extends ListenerAdapter implements Module {
	
	private static final RequireModuleHook TESTMODULE = new RequireModuleHook();

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
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public List<RequireModuleHook> requireModules() {
		return null;
	}
	
	@Override
	public RequireModuleHook getMyRequireModuleHook() {
		return TESTMODULE;
	}
	
}
