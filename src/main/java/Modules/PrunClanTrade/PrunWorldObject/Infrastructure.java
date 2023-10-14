package Modules.PrunClanTrade.PrunWorldObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("Infrastructure")
public class Infrastructure {
	
	
	public String InfrastructureId;
	public InfrastructureReport[] InfrastructureReports;
	
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonRootName("InfrastructureReport")
	public static class InfrastructureReport {
		
		public int NextPopulationPioneer,
				NextPopulationSettler,
				NextPopulationTechnician,
				NextPopulationEngineer,
				NextPopulationScientist;
		
	}
	
}
