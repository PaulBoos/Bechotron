package Modules.TestModule;

import Head.BotInstance;
import Modules.Module;
import Modules.PrunClanTrade.MaterialEmoji;
import Modules.PrunClanTrade.PrunWorldObject.Material;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Test extends ListenerAdapter implements Module {
	
	private static final RequireModuleHook TESTMODULE = new RequireModuleHook();
	private static Logger logger = LoggerFactory.getLogger("TESTING LOGGER");

	BotInstance bot;
	
	public static void main(String[] args) throws IOException, UnsupportedFlavorException {
		BotInstance.main(null);
		Test test = new Test(BotInstance.steamModule.botInstance);
		MaterialEmoji.getEmojiMap();
		for(long l: MaterialEmoji.getContainer().guilds) {
			Guild g = test.bot.jda.getGuildById(l);
			for(Channel c: g.getTextChannels()) {
				c.delete().queue();
			}
			for(Category c: g.getCategories()) {
				c.delete().queue();
			}
		}
		MaterialEmoji.synchronizeEmojiMap(test.bot.jda);
	}
	
	// https://discord.new/PH6tuZefgnAc
	// https://discord.com/template/PH6tuZefgnAc
	
//	public List<Long> uploadEmoji(long guild) throws IOException {
//		Guild g = bot.jda.getGuildById(guild);
//		List<Long> out = new ArrayList<>();
//		final int count = Material.allMaterials.length;
//		AtomicInteger counter = new AtomicInteger(1);
//		for(Material m: Material.allMaterials) {
//			File f = new File("../output/materialpng/" + m.ticker + ".png");
//			if(f.exists()) {
//				g.createEmoji(m.ticker.length() > 1 ? m.ticker : m.ticker + "_", Icon.from(f)).queue(e -> {
//					out.add(e.getIdLong());
//					System.out.println("Added " + String.format("%3s", e.getName()) + " (" + counter.getAndIncrement() + "/" + count + ")");
//				});
//			}
//		}
//		return out;
//	}
	
	public static void writeMaterialSVGs() throws IOException {
		final String svg = """
			<?xml version="1.0" encoding="UTF-8" standalone="no"?>
			<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"  version="1.2" baseProfile="tiny">
				<defs>
					<linearGradient gradientUnits="userSpaceOnUse" x1="0" y1="0" x2="256" y2="256" id="gradient1">
						<stop offset="1e-07" stop-color="#%02X%02X%02X" stop-opacity="1" />
						<stop offset="1" stop-color="#%02X%02X%02X" stop-opacity="1" />
					</linearGradient>
				</defs>
				
				<g fill="url(#gradient1)" stroke="none" transform="matrix(1,0,0,1,0,0)">
					<rect x="0" y="0" width="256" height="256" />
				</g>
				<text fill="#%02X%02X%02X" x="128" y="128" dominant-baseline="central" text-anchor="middle" fill-opacity="1" stroke="none" font-family="Arial" font-size="84" font-weight="700" font-style="normal">%s</text>
			</svg>
			""";
		FileWriter writer;
		if((!new File("../output/materialsvg/").exists()) && (!new File("../output/materialsvg/").mkdirs())) throw new RuntimeException("Could not create directory");
		for(Material material : Material.allMaterials) {
			writer = new FileWriter("../output/materialsvg/" + material.ticker + ".svg");
			writer.write(String.format(
					svg,
					material.category.one.getRed(), material.category.one.getGreen(), material.category.one.getBlue(),
					material.category.two.getRed(), material.category.two.getGreen(), material.category.two.getBlue(),
					material.category.font.getRed(), material.category.font.getGreen(), material.category.font.getBlue(),
					material.ticker)
			);
			writer.close();
		}
	}
	
	public Test(BotInstance instance) {
		instance.jda.addEventListener(this);
		bot = instance;
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
