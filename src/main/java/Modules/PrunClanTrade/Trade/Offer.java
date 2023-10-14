package Modules.PrunClanTrade.Trade;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.Event;

public abstract class Offer {
	
	
	boolean isPublic;
	String notes;
	
	protected Offer createOffer(Event event) {
		return null; // TODO
	}
	
	protected Offer(boolean isPublic, String notes) {
		this.isPublic = isPublic;
		this.notes = notes;
	}
	
	public abstract MessageEmbed.Field getAsEmbedField(JDA jda);
	
}
