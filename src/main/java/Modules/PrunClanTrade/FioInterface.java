package Modules.PrunClanTrade;

import Modules.PrunClanTrade.PrunWorldObject.Planet;
import Utils.SimpleHttpConnector;
import Utils.StringHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class FioInterface extends SimpleHttpConnector {
	
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(FioInterface.class);
	private final ObjectMapper mapper = new ObjectMapper();
	private HashMap<String, String> nametonature;
	private HashMap<String, String> alphatonature;
	
	public FioInterface() {
		loadMaps();
		reloadAllPlanets();
	}
	
	private void loadMaps() {
		try {
			nametonature = mapper.readValue(new File("./sources/prun/nametonature.json"), HashMap.class);
			alphatonature = mapper.readValue(new File("./sources/prun/alphatonature.json"), HashMap.class);
		} catch(FileNotFoundException ignored) {
			reloadAllPlanets();
//			loadMaps();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Planet getPlanet(String planet) {
		loadMaps();
		if(nametonature.containsKey(StringHelper.capitalizeFirstLetterLowerAfter(planet))) {
			planet = nametonature.get(StringHelper.capitalizeFirstLetterLowerAfter(planet));
		} else if(alphatonature.containsKey(planet.toLowerCase())) {
			planet = alphatonature.get(planet);
		}
		try {
			return mapper.readValue(new File("./sources/prun/planets/" + planet + ".json"), Planet.class);
		} catch(IOException e) {
			return null;
		}
	}
	
	public boolean reloadAllPlanets() {
		try {
			if(nametonature == null) nametonature = new HashMap<>();
			if(alphatonature == null) alphatonature = new HashMap<>();
			
			List<Planet> planets;
			String output = sendGetRequest("https://rest.fnar.net/planet/allplanets/full");
			ObjectMapper mapper = new ObjectMapper();
			planets = mapper.readValue(output, mapper.getTypeFactory().constructCollectionType(List.class, Planet.class));
			
			for(Planet planet : planets) {
				mapper.writerWithDefaultPrettyPrinter().writeValue(new File("./sources/prun/planets/"+planet.naturalId+".json"), planet);
				
				if(!planet.name.equals(planet.naturalId)) {
					nametonature.put(planet.name, planet.naturalId);
				}
				alphatonature.put(planet.alphanumericId, planet.naturalId);
			}
			
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("./sources/prun/nametonature.json"), nametonature);
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("./sources/prun/alphatonature.json"), alphatonature);
			
			return true;
		} catch(IOException e) {
			logger.error("Error while reloading all planets", e);
			return false;
		}
	}
	
	public static String loadInfrastructure(String infrastructure) throws IOException {
		return sendGetRequest("https://rest.fnar.net/infrastructure/" + infrastructure);
	}
	
	public static String sendGetRequest(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		// Set the request method to GET
		con.setRequestMethod("GET");
		
		// Add any additional headers or parameters here, if needed
		con.setConnectTimeout(5000);
		con.setReadTimeout(0);
		
		// Send the request and read the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
	
}
