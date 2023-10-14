package Modules.PrunClanTrade;

import Modules.PrunClanTrade.PrunWorldObject.Material;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.json.JsonMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.ConstructorProperties;
import java.io.File;
import java.io.IOException;
import java.util.*;

@JsonRootName("MaterialEmoji")
public class MaterialEmoji {
	
	static Logger LOGGER = LoggerFactory.getLogger(MaterialEmoji.class);
	
	private static EmojiMapContainer container;
	@JsonProperty
	public final String materialTicker;
	@JsonProperty
	public final String emojiIdentifier;
	@JsonProperty
	public final int guild;
	
	@JsonCreator
	@ConstructorProperties({"materialTicker", "emojiIdentifier", "guild"})
	public MaterialEmoji(String materialTicker, String emojiIdentifier, int guild) {
		this.materialTicker = materialTicker;
		this.emojiIdentifier = emojiIdentifier;
		this.guild = guild;
	}
	
	public static Emoji getEmoji(JDA jda, Material material) {
		return jda.getEmojiById(container.emojiMap.get(material.materialId).substring(2)); // !!! WARNING !!! 2 chars can only be used if we stay below 10 guilds
	}
	
	private static EmojiMapContainer loadEmojiMap() {
		container = EmojiMapContainer.loadEmojiMap();
		LOGGER.info("Successfully loaded Material Emoji Map from storage");
		return container;
	}
	
	public static EmojiMapContainer getEmojiMap() {
		return container != null ? container : loadEmojiMap();
	}
	
	
	public static void synchronizeEmojiMap(JDA jda) { // "guildIndex-emojiid"
		List<Material> requiresEmoji = new ArrayList<>(Arrays.asList(Material.allMaterials));
		for(Map.Entry<String, String> entry: container.emojiMap.entrySet()) {
			String[] s = entry.getValue().split("-");
			int guildIndex = Integer.parseInt(s[0]);
			long guildid = container.guilds[guildIndex];
			long emojiid = Long.parseLong(s[1]);
			Guild g = jda.getGuildById(guildid);
			CustomEmoji emoji;
			if(g != null) {
				emoji = g.getEmojiById(emojiid);
				if(emoji != null) {
					requiresEmoji.remove(Material.getMaterial(entry.getKey()));
				}
			}
		}
		int guildIndex = 0;
		Guild g = jda.getGuildById(container.guilds[guildIndex]);
		int guildEmojiCounter = g.getEmojis().size();
		for(Material material: requiresEmoji) {
			while(g.getMaxEmojis() - guildEmojiCounter <= 0) {
				guildIndex++;
				if(guildIndex < container.guilds.length) {
					g = jda.getGuildById(container.guilds[guildIndex]);
					guildEmojiCounter = g.getEmojis().size();
				}
				else throw new RuntimeException("Guilds are satisfied with Emojis.");
			}
			String ticker = material.ticker;
			try {
				Icon icon = Icon.from(new File("../output/materialpng/" + ticker + ".png"));
				long id = g.createEmoji(ticker.length() > 1 ? ticker.toUpperCase() : "_" + ticker.toUpperCase(), icon).complete().getIdLong();
				//long id = (long) (Math.random()*1000);
				container.emojiMap.put(material.materialId, guildIndex + "-" + id);
				System.out.println("I've made " + ticker + " now in Guild #" + guildIndex);
				guildEmojiCounter++;
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
		LOGGER.info("Successfully Synchronized Material Emoji Map.");
		writeEmojiMap();
	}
	
	public static void writeEmojiMap() {
		try {
			JsonMapper mapper = new JsonMapper();
			File f = new File("./sources/materialEmoji.json");
			mapper.writerWithDefaultPrettyPrinter().writeValue(f, container);
			LOGGER.info("Successfully Wrote Material Emoji Map.");
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static EmojiMapContainer getContainer() {
		return container;
	}
	
	@JsonRootName("EmojiMap")
	public static class EmojiMapContainer {
		
		@JsonProperty
		public long[] guilds;
		@JsonProperty
		public HashMap<String, String> emojiMap;
		
		@JsonCreator
		@ConstructorProperties({"guilds", "emojiMap"})
		private EmojiMapContainer(long[] guilds, HashMap<String, String> emojiMap) {
			this.guilds = guilds;
			this.emojiMap = emojiMap;
		}
		
		private static EmojiMapContainer loadEmojiMap() {
			try {
				JsonMapper mapper = new JsonMapper();
				File f = new File("./sources/materialEmoji.json");
				if(f.exists()) return mapper.readValue(f, EmojiMapContainer.class);
				else {
					return new EmojiMapContainer(new long[] {
							1062025138117283870L,
							1062026385457172490L,
							1062026436531212308L,
							1062026500557258752L,
							1062026572732846140L,
							1062026701871259778L,
							1062030332418871358L,
							1062049092504670358L
					}, new HashMap<>());
				}
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
}
