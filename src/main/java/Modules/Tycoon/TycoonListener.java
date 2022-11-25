package Modules.Tycoon;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TycoonListener extends ListenerAdapter {
	
	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		if (event.getName().equals("tycoon")) {
			event.reply("This is the Tycoon Module!").queue();
		}
	}
	
}
