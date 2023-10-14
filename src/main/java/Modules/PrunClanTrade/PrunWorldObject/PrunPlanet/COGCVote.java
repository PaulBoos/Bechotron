package Modules.PrunClanTrade.PrunWorldObject.PrunPlanet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

public class COGCVote {
	
	@JsonProperty("CompanyName")
	public final String companyName;
	@JsonProperty("CompanyCode")
	public final String companyCode;
	@JsonProperty("Influence")
	public final float influence;
	@JsonProperty("VoteType")
	public final String voteType;
	@JsonProperty("VoteTimeEpochMs")
	public final long voteTimeEpochMs;
	
	@JsonCreator
	@ConstructorProperties({"CompanyName", "CompanyCode", "Influence", "VoteType", "VoteTimeEpochMs"})
	public COGCVote(String companyName, String companyCode, float influence, String voteType, long voteTimeEpochMs) {
		this.companyName = companyName;
		this.companyCode = companyCode;
		this.influence = influence;
		this.voteType = voteType;
		this.voteTimeEpochMs = voteTimeEpochMs;
	}
	
}
