package Modules.PrunClanTrade.PrunWorldObject;

import Modules.PrunClanTrade.PrunWorldObject.PrunPlanet.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.beans.ConstructorProperties;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Planet {
	
	@JsonProperty("PlanetId")
	public final String alphanumericId;
	@JsonProperty("PlanetNaturalId")
	public final String naturalId;
	@JsonProperty("PlanetName")
	public final String name;
	@JsonProperty("Namer")
	public final String namer;
	@JsonProperty("NamingDataEpochMs")
	public final long namingDataEpochMs;
	@JsonProperty("Nameable")
	public final boolean nameable;
	@JsonProperty("SystemId")
	public final String systemId;
	@JsonProperty("Gravity")
	public final float gravity;
	@JsonProperty("MagneticField")
	public final float magneticField;
	@JsonProperty("Mass")
	public final double mass;
	@JsonProperty("MassEarth")
	public final double massEarth;
	@JsonProperty("OrbitSemiMajorAxis")
	public final float orbitSemiMajorAxis;
	@JsonProperty("OrbitEccentricity")
	public final float orbitEccentricity;
	@JsonProperty("OrbitInclination")
	public final float orbitInclination;
	@JsonProperty("OrbitRightAscension")
	public final float orbitRightAscension;
	@JsonProperty("OrbitPeriapsis")
	public final float orbitPeriapsis;
	@JsonProperty("OrbitIndex")
	public final int orbitIndex;
	@JsonProperty("Pressure")
	public final float pressure;
	@JsonProperty("Radiation")
	public final double radiation;
	@JsonProperty("Radius")
	public final float radius;
	@JsonProperty("Sunlight")
	public final float sunlight;
	@JsonProperty("Surface")
	public final boolean surface;
	@JsonProperty("Temperature")
	public final float temperature;
	@JsonProperty("Fertility")
	public final float fertility;
	@JsonProperty("HasLocalMarket")
	public final boolean hasLocalMarket;
	@JsonProperty("HasChamberOfCommerce")
	public final boolean hasChamberOfCommerce;
	@JsonProperty("HasWarehouse")
	public final boolean hasWarehouse;
	@JsonProperty("HasAdministrationCenter")
	public final boolean hasAdministrationCenter;
	@JsonProperty("HasShipyard")
	public final boolean hasShipyard;
	@JsonProperty("FactionCode")
	public final String factionCode;
	@JsonProperty("FactionName")
	public final String factionName;
	@JsonProperty("GovernorId")
	public final String governorId;
	@JsonProperty("GovernorUserName")
	public final String governorUserName;
	@JsonProperty("GovernorCorporationId")
	public final String governorCorporationId;
	@JsonProperty("GovernorCorporationName")
	public final String governorCorporationName;
	@JsonProperty("GovernorCorporationCode")
	public final String governorCorporationCode;
	@JsonProperty("CurrencyName")
	public final String currencyName;
	@JsonProperty("CurrencyCode")
	public final String currencyCode;
	@JsonProperty("CollectorId")
	public final String collectorId;
	@JsonProperty("CollectorName")
	public final String collectorName;
	@JsonProperty("CollectorCode")
	public final String collectorCode;
	@JsonProperty("BaseLocalMarketFee")
	public final float baseLocalMarketFee;
	@JsonProperty("LocalMarketFeeFactor")
	public final float localMarketFeeFactor;
	@JsonProperty("WarehouseFee")
	public final float warehouseFee;
	@JsonProperty("PopulationId")
	public final String populationId;
	@JsonProperty("COGCProgramStatus")
	public final String COGCProgramStatus;
	@JsonProperty("PlanetTier")
	public final int planetTier;
	@JsonProperty("UserNameSubmitted")
	public final String userNameSubmitted;
	@JsonProperty("Timestamp")
	public final String timestamp;
	@JsonProperty("Resources")
	public final List<Resource> resources;
	@JsonProperty("BuildRequirements")
	public final List<PlanetMaterial> buildRequirements;
	@JsonProperty("ProductionFees")
	public final List<ProductionFee> productionFees;
	@JsonProperty("COGCPrograms")
	public final List<COGCProgram> COGCPrograms;
	@JsonProperty("COGCVotes")
	public final List<COGCVote> COGCVotes;
	
	@JsonCreator
	@ConstructorProperties({"PlanetId", "PlanetNaturalId", "PlanetName", "Namer", "NamingDataEpochMs", "Nameable", "SystemId", "Gravity", "MagneticField", "Mass", "MassEarth", "OrbitSemiMajorAxis", "OrbitEccentricity", "OrbitInclination", "OrbitRightAscension", "OrbitPeriapsis", "OrbitIndex", "Pressure", "Radiation", "Radius", "Sunlight", "Surface", "Temperature", "Fertility", "HasLocalMarket", "HasChamberOfCommerce", "HasWarehouse", "HasAdministrationCenter", "HasShipyard", "FactionCode", "FactionName", "GovernorId", "GovernorUserName", "GovernorCorporationId", "GovernorCorporationName", "GovernorCorporationCode", "CurrencyName", "CurrencyCode", "CollectorId", "CollectorName", "CollectorCode", "BaseLocalMarketFee", "LocalMarketFeeFactor", "WarehouseFee", "PopulationId", "COGCProgramStatus", "PlanetTier", "UserNameSubmitted", "Timestamp", "Resources", "BuildRequirements", "ProductionFees", "COGCPrograms", "COGCVotes"})
	public Planet(String alphanumericId,
				  String naturalId,
				  String name,
				  String namer,
				  long namingDataEpochMs,
				  boolean nameable,
				  String systemId,
				  float gravity,
				  float magneticField,
				  double mass,
				  double massEarth,
				  float orbitSemiMajorAxis,
				  float orbitEccentricity,
				  float orbitInclination,
				  float orbitRightAscension,
				  float orbitPeriapsis,
				  int orbitIndex,
				  float pressure,
				  double radiation,
				  float radius,
				  float sunlight,
				  boolean surface,
				  float temperature,
				  float fertility,
				  boolean hasLocalMarket,
				  boolean hasChamberOfCommerce,
				  boolean hasWarehouse,
				  boolean hasAdministrationCenter,
				  boolean hasShipyard,
				  String factionCode,
				  String factionName,
				  String governorId,
				  String governorUserName,
				  String governorCorporationId,
				  String governorCorporationName,
				  String governorCorporationCode,
				  String currencyName,
				  String currencyCode,
				  String collectorId,
				  String collectorName,
				  String collectorCode,
				  float baseLocalMarketFee,
				  float localMarketFeeFactor,
				  float warehouseFee,
				  String populationId,
				  String cogcProgramStatus,
				  int planetTier,
				  String userNameSubmitted,
				  String timestamp,
				  List<Resource> resources,
				  List<PlanetMaterial> buildRequirements,
				  List<ProductionFee> productionFees,
				  List<COGCProgram> COGCPrograms,
				  List<COGCVote> COGCVotes) {
		
		this.alphanumericId = alphanumericId;
		this.naturalId = naturalId;
		this.name = name;
		this.namer = namer;
		this.namingDataEpochMs = namingDataEpochMs;
		this.nameable = nameable;
		this.systemId = systemId;
		this.gravity = gravity;
		this.magneticField = magneticField;
		this.mass = mass;
		this.massEarth = massEarth;
		this.orbitSemiMajorAxis = orbitSemiMajorAxis;
		this.orbitEccentricity = orbitEccentricity;
		this.orbitInclination = orbitInclination;
		this.orbitRightAscension = orbitRightAscension;
		this.orbitPeriapsis = orbitPeriapsis;
		this.orbitIndex = orbitIndex;
		this.pressure = pressure;
		this.radiation = radiation;
		this.radius = radius;
		this.sunlight = sunlight;
		this.surface = surface;
		this.temperature = temperature;
		this.fertility = fertility;
		this.hasLocalMarket = hasLocalMarket;
		this.hasChamberOfCommerce = hasChamberOfCommerce;
		this.hasWarehouse = hasWarehouse;
		this.hasAdministrationCenter = hasAdministrationCenter;
		this.hasShipyard = hasShipyard;
		this.factionCode = factionCode;
		this.factionName = factionName;
		this.governorId = governorId;
		this.governorUserName = governorUserName;
		this.governorCorporationId = governorCorporationId;
		this.governorCorporationName = governorCorporationName;
		this.governorCorporationCode = governorCorporationCode;
		this.currencyName = currencyName;
		this.currencyCode = currencyCode;
		this.collectorId = collectorId;
		this.collectorName = collectorName;
		this.collectorCode = collectorCode;
		this.baseLocalMarketFee = baseLocalMarketFee;
		this.localMarketFeeFactor = localMarketFeeFactor;
		this.warehouseFee = warehouseFee;
		this.populationId = populationId;
		this.COGCProgramStatus = cogcProgramStatus;
		this.planetTier = planetTier;
		this.userNameSubmitted = userNameSubmitted;
		this.timestamp = timestamp;
		this.resources = resources;
		this.buildRequirements = buildRequirements;
		this.productionFees = productionFees;
		this.COGCPrograms = COGCPrograms;
		this.COGCVotes = COGCVotes;
	}
	
	public static List<Planet> loadAllPlanets() throws IOException {
		Path folder = Paths.get(".\\sources\\prun\\planets\\");
		File[] files = folder.toFile().listFiles();
		List<Planet> planets = new ArrayList<>();
		JsonMapper mapper = new JsonMapper();
		for (File file : files) {
			planets.add(mapper.readValue(file, Planet.class));
		}
		return planets;
	}
	
	@Override
	public String toString() {
		return "Planet " + (name.equals(naturalId) ? naturalId : name + " (" + naturalId + ")");
	}
}
