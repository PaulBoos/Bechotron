package Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleHttpConnector {
	
	private static String sendPostRequest(String urlString, String payload) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		// Set the request method to GET
		con.setRequestMethod("POST");
		
		// Add any additional headers or parameters here, if needed
		
		// Enable the output stream and write the payload to the request body
		con.setDoOutput(true);
		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(payload);
		out.flush();
		out.close();
		
		// Send the request and read the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		// Return the response as a JSON string
		return response.toString();
	}
	
	public static String sendGetRequest(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		// Set the request method to GET
		con.setRequestMethod("GET");
		
		// Add any additional headers or parameters here, if needed
		
		// Send the request and read the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		// Return the response as a string
		return response.toString();
	}
	
}
