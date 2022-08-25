package Modules.DBIP;

import Modules.Module;
import in.ankushs.dbip.api.DbIpClient;
import in.ankushs.dbip.api.GeoEntity;

import java.io.File;

public class DBIPModule implements Module {
	
	final File gzipFile = new File("./dbip-city-lite-2022-08.csv.gz");
	final DbIpClient client = new DbIpClient(gzipFile);
	
	public void lookup(String ip) {
		System.out.println("DBIP lookup for " + ip);
		final GeoEntity geoEntity = client.lookup("31.45.127.255");
		final String city = geoEntity.getCity();
		final String country = geoEntity.getCountry();
		final String province = geoEntity.getProvince();
		final String countryCode = geoEntity.getCountryCode();
		
		System.out.println("city : " + city);
		System.out.println("province : " + province);
		System.out.println("country : " + country);
		System.out.println("country code : " + countryCode);
	}
	
	public static void main(String[] args) {
		final DBIPModule module = new DBIPModule();
		module.lookup("1.1.1.1");
	}
	
	@Override
	public String getDescription() {
		return "Module for looking up IP addresses geographic information.\nIP Geolocation by DB-IP (https://db-ip.com/)";
	}
	
	public class DBIPPacket {
	
	
	
	}
	
}
