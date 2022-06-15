package Modules.Crusader;

import Modules.Crusader.Entities.Person;
import com.fasterxml.jackson.core.JacksonException;

public class CrusaderTest {
	
	
	
	public static void main(String[] args) throws JacksonException {
		String xml = "<Person><name>Testperson</name><prenom>Peter</prenom><nickname>that dude</nickname><id>10</id></Person>";
		Person d = Person.deserialize(xml);
		Person p = new Person("Testperson", "Peter", "that dude", 1);
		System.out.println(d);
		System.out.println(p);
		System.out.println(d.serialize());
		//System.out.println(d.getFullName());
	}
	
}
