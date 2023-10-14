package Modules.PrunClanTrade.PrunWorldObject.PrunPlanet;

import Modules.PrunClanTrade.PrunWorldConcept.Currency;
import Modules.PrunClanTrade.PrunWorldConcept.ProductionCategory;
import Modules.PrunClanTrade.PrunWorldConcept.WorkforceLevel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.beans.ConstructorProperties;

@JsonRootName("ProductionFee")
public class ProductionFee {
	
	@JsonProperty("Category")
	public final ProductionCategory category;
	@JsonProperty("WorkforceLevel")
	public final WorkforceLevel workforceLevel;
	@JsonProperty("FeeAmount")
	public final float feeAmount;
	@JsonProperty("FeeCurrency")
	public final Currency feeCurrency;
	
	@JsonCreator
	@ConstructorProperties({"Category", "WorkforceLevel", "FeeAmount", "FeeCurrency"})
	public ProductionFee(ProductionCategory category, WorkforceLevel workforceLevel, float feeAmount, Currency feeCurrency) {
		this.category = category;
		this.workforceLevel = workforceLevel;
		this.feeAmount = feeAmount;
		this.feeCurrency = feeCurrency;
	}
	
}
