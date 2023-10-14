package Modules.PrunClanTrade.Trade;

import Modules.PrunClanTrade.MaterialEmoji;
import Modules.PrunClanTrade.PrunWorldObject.Material;
import Utils.StringHelper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class MaterialOffer extends Offer {
	
	Material material;
	String amount;
	Price price;
	String location;
	boolean isBuy;
	long user;
	
	public MaterialOffer(Material material, String amount, Price price, String location, boolean isBuy, boolean isPublic, String notes, long user) {
		super(isPublic, notes);
		this.material = material;
		this.amount = amount;
		this.price = price;
		this.location = location;
		this.isBuy = isBuy;
		this.user = user;
	}
	
	@Override
	public MessageEmbed.Field getAsEmbedField(JDA jda) {
		Emoji emoji = MaterialEmoji.getEmoji(jda, material);
		return new MessageEmbed.Field(
				(emoji != null ? emoji.getFormatted() + " " : "") +
						(material == null ? "Unknown " : StringHelper.capitalizeFirstLettersAndSplitWords(material.name) + " ") +
						(isBuy ? "\nBuying Order " : "\nSelling Order "),
				
				(amount == null ? "" : ("Amount: " + amount + "\n")) +
						(price == null ? "" : ("Price: " + price.amount() + " " + price.currency() + "\n")) +
						(location == null || location.isEmpty() ? "" : ("Location: " + location + "\n")) +
						(notes == null || notes.isEmpty() ? "" : ("Notes: " + notes)) +
						(user == 0 ? "" : ("\nUser: <@" + user + ">")),
				
				true
		);
	}
	
}
