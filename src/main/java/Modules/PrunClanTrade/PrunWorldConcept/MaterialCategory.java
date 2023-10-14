package Modules.PrunClanTrade.PrunWorldConcept;

import java.awt.*;

public enum MaterialCategory {
	AGRICULTURAL_PRODUCTS("Agricultural Products", new Color(92, 18, 18), new Color(117, 43, 43), new Color(219, 145, 145)),
	ALLOYS("Alloys", new Color(123, 76, 30), new Color(148, 101, 55), new Color(250, 203, 157)),
	CHEMICALS("Chemicals", new Color(183, 46, 91), new Color(208, 71, 116), new Color(255, 173, 218)),
	CONSTRUCTION_MATERIALS("Construction Materials", new Color(24, 91, 211), new Color(49, 116, 236), new Color(151, 218, 255)),
	CONSTRUCTION_PARTS("Construction Parts", new Color(41, 77, 107), new Color(66, 102, 132), new Color(168, 204, 234)),
	CONSTRUCTION_PREFABS("Construction Prefabs", new Color(15, 30, 98), new Color(40, 55, 123), new Color(142, 157, 225)),
	CONSUMABLES_BASIC("Consumables (Basic)", new Color(149, 46, 46), new Color(174, 71, 71), new Color(255, 173, 173)),
	CONSUMABLES_LUXURY("Consumables (Luxury)", new Color(136, 24, 39), new Color(161, 49, 64), new Color(255, 151, 166)),
	DRONES("Drones", new Color(140, 52, 18), new Color(165, 77, 43), new Color(255, 179, 145)),
	ELECTRONIC_DEVICES("Electronic Devices", new Color(86, 20, 147), new Color(111, 45, 172), new Color(213, 147, 255)),
	ELECTRONIC_PARTS("Electronic Parts", new Color(91, 46, 183), new Color(116, 71, 208), new Color(218, 173, 255)),
	ELECTRONIC_PIECES("Electronic Pieces", new Color(119, 82, 189), new Color(144, 107, 214), new Color(246, 209, 255)),
	ELECTRONIC_SYSTEMS("Electronic Systems", new Color(51, 26, 76), new Color(76, 51, 101), new Color(178, 153, 203)),
	ELEMENTS("Elements", new Color(61, 46, 32), new Color(86, 71, 57), new Color(188, 173, 159)),
	ENERGY_SYSTEMS("Energy Systems", new Color(21, 62, 39), new Color(46, 87, 64), new Color(148, 189, 166)),
	FUELS("Fuels", new Color(30, 123, 30), new Color(55, 148, 55), new Color(157, 250, 157)),
	GASES("Gases", new Color(0, 105, 107), new Color(25, 130, 132), new Color(127, 232, 234)),
	LIQUIDS("Liquids", new Color(114, 164, 202), new Color(139, 189, 227), new Color(241, 255, 255)),
	MEDICAL_EQUIPMENT("Medical Equipment", new Color(85, 170, 85), new Color(110, 195, 110), new Color(212, 255, 212)),
	METALS("Metals", new Color(54, 54, 54), new Color(79, 79, 79), new Color(181, 181, 181)),
	MINERALS("Minerals", new Color(153, 113, 73), new Color(178, 138, 98), new Color(255, 240, 200)),
	ORES("Ores", new Color(82, 87, 97), new Color(107, 112, 122), new Color(209, 214, 224)),
	PLASTICS("Plastics", new Color(121, 31, 60), new Color(146, 56, 85), new Color(248, 158, 187)),
	SHIP_ENGINES("Ship Engines", new Color(153, 41, 0), new Color(178, 66, 25), new Color(255, 168, 127)),
	SHIP_KITS("Ship Kits", new Color(153, 84, 0), new Color(178, 109, 25), new Color(255, 211, 127)),
	SHIP_PARTS("Ship Parts", new Color(153, 99, 0), new Color(178, 124, 25), new Color(255, 226, 127)),
	SHIP_SHIELDS("Ship Shields", new Color(224, 131, 0), new Color(249, 156, 25), new Color(255, 255, 127)),
	SOFTWARE_COMPONENTS("Software Components", new Color(136, 121, 47), new Color(161, 146, 72), new Color(255, 248, 174)),
	SOFTWARE_SYSTEMS("Software Systems", new Color(60, 53, 5), new Color(85, 78, 30), new Color(187, 180, 132)),
	SOFTWARE_TOOLS("Software Tools", new Color(129, 98, 19), new Color(154, 123, 44), new Color(255, 225, 146)),
	TEXTILES("Textiles", new Color(82, 90, 33), new Color(107, 115, 58), new Color(209, 217, 160)),
	UNIT_PREFABS("Unit Prefabs", new Color(29, 27, 28), new Color(54, 52, 53), new Color(156, 154, 155)),
	UTILITY("Utility", new Color(161, 148, 136), new Color(186, 173, 161), new Color(255, 255, 255));
	
	public final String name;
	public final Color one, two, font;
	
	MaterialCategory(String name, Color one, Color two, Color font) {
		this.name = name;
		this.one = one;
		this.two = two;
		this.font = font;
	}
	
	public static MaterialCategory getCategory(String name) {
		for (MaterialCategory category : values()) {
			if (category.name.equalsIgnoreCase(name)) {
				return category;
			}
		}
		return null;
	}
	
}
