package Modules.Crusader.Entities;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.beans.ConstructorProperties;

@JacksonXmlRootElement
public class Person implements CrusaderEntity {
	
	// private static final int stringMaxLength = 63;
	
	@JacksonXmlProperty
	String name, prenom, nickname;
	
	@ConstructorProperties({"name", "prenom", "nickname"})
	public Person(String name, String prenom, String nickname) {
		this.name = name;
		this.prenom = prenom;
		this.nickname = nickname;
	}
	
	public String serialize() {
		return String.format("<Person><name>%s</name><prenom>%s</prenom><nickname>%s</nickname></Person>", this.name, this.prenom, this.nickname);
	}
	
	public static Person deserialize(String input) throws JacksonException {
		return new XmlMapper().readValue(input, Person.class);
	}
	
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
