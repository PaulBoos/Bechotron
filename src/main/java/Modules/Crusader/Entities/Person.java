package Modules.Crusader.Entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement
public class Person implements CrusaderEntity {
	
	// private static final int stringMaxLength = 63;
	
	@JacksonXmlProperty
	String name, prenom, nickname;
	
	
	
	public String getFullName() {
		return String.format("%s \"%s\" %s", prenom, nickname, name);
	}
	
	public String getName() {
		return name;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	@Override
	public String toString() {
		return "Character{" +
				"name='" + name + '\'' +
				", prenom='" + prenom + '\'' +
				'}';
	}
	
}
