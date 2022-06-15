package Modules.Crusader.Entities;

import com.fasterxml.jackson.annotation.JsonGetter;
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
	
	@JacksonXmlProperty
	int id;
	
	@ConstructorProperties({"name", "prenom", "nickname", "id"})
	public Person(String name, String prenom, String nickname, int id) {
		this.name = name;
		this.prenom = prenom;
		this.nickname = nickname;
		this.id = id;
	}
	
	public String serialize() throws JsonProcessingException {
		return new XmlMapper().writeValueAsString(this);
	}
	
	public static Person deserialize(String input) throws JacksonException {
		return new XmlMapper().readValue(input, Person.class);
	}
	/*
	public String getFullName() {
		return String.format("%s \"%s\" %s", prenom, nickname, name);
	}
	*/
	@JsonGetter
	public String getNickname() {
		return nickname;
	}
	
	@JsonGetter
	public String getName() {
		return name;
	}
	
	@JsonGetter
	public String getPrenom() {
		return prenom;
	}
	
	@Override
	public String toString() {
		return String.format("Character #%05d {\n| name=\"%s\",\n| prenom=\"%s\",\n\\ nickname=\"%s\" }", id, name, prenom, nickname);
	}
	
}
