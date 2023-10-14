package Modules.Nday;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonRootName("NdayNation")
@JacksonXmlRootElement(localName = "NdayNation")
public class Nation {
	
	private NdayGame game;
	private long id;
	private String name;
	private String flag;
	private int materials;
	private int population;
	private int nukes, interceptors;
	private int radiation;
	private long channel;
	
	// Set up JSON so game is a reference to the parent object
	@JsonCreator
	public Nation(@JsonProperty("id") long id,
				  @JsonProperty("name") String name,
				  @JsonProperty("flag") String flag,
				  @JsonProperty("materials") int materials,
				  @JsonProperty("population") int population,
				  @JsonProperty("nukes") int nukes,
				  @JsonProperty("interceptors") int interceptors,
				  @JsonProperty("radiation") int radiation,
				  @JsonProperty("channel") long channel) {
		this.id = id;
		this.name = name;
		this.flag = flag;
		this.materials = materials;
		this.population = population;
		this.nukes = nukes;
		this.interceptors = interceptors;
		this.radiation = radiation;
		this.channel = channel;
	}
	
	@JsonIgnore
	public NdayGame getGame() {
		return game;
	}
	
	@JsonIgnore
	public void setGame(NdayGame game) {
		this.game = game;
	}
	
	@JsonGetter("name")
	public String getName() {
		return name;
	}
	
	@JsonSetter("name")
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonGetter("flag")
	public String getFlag() {
		return flag;
	}
	
	@JsonSetter("flag")
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	@JsonGetter("materials")
	public int getMaterials() {
		return materials;
	}
	
	@JsonSetter("materials")
	public void setMaterials(int materials) {
		this.materials = materials;
	}
	
	@JsonIgnore
	public int getIncome() {
		return (int) (population * game.getIncomePerPopulation());
	}
	
	@JsonGetter("population")
	public int getPopulation() {
		return population;
	}
	
	@JsonSetter("population")
	public void setPopulation(int population) {
		this.population = population;
	}
	
	@JsonGetter("nukes")
	public int getNukes() {
		return nukes;
	}
	
	@JsonSetter("nukes")
	public void setNukes(int nukes) {
		this.nukes = nukes;
	}
	
	@JsonGetter("interceptors")
	public int getInterceptors() {
		return interceptors;
	}
	
	@JsonSetter("interceptors")
	public void setInterceptors(int interceptors) {
		this.interceptors = interceptors;
	}
	
	@JsonGetter("radiation")
	public int getRadiation() {
		return radiation;
	}
	
	@JsonSetter("radiation")
	public void setRadiation(int radiation) {
		this.radiation = radiation;
	}
	
	@JsonGetter("channel")
	public long getChannel() {
		return channel;
	}
	
	@JsonSetter("channel")
	public void setChannel(long channel) {
		this.channel = channel;
	}
}
