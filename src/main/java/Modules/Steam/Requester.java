package Modules.Steam;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

public class Requester {

	private static final String APIKEY = "5DEA4155CD1AA32A38BFEA25BB7B2C32";
	
	private static final SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(APIKEY).build();
	
	public static GetOwnedGames request(String userkey) throws SteamApiException {
		GetOwnedGamesRequest request = SteamWebApiRequestFactory.createGetOwnedGamesRequest(userkey);
		return client.processRequest(request);
	}
	
	public static void main(String[] args) throws SteamApiException {
		System.out.println(request("76561197960434622"));
	}
	
}
