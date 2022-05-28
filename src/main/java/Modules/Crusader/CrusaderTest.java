package Modules.Crusader;

import Modules.Crusader.Entities.Person;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CrusaderTest {
	
	
	
	public static void main(String[] args) throws JsonProcessingException {
		
		String xml = "<Person><name>Testperson</name><prenom>Peter</prenom><nickname>that dude</nickname></Person>";
		Person d = Person.deserialize(xml);
//		Person p = new Person("Testperson", "Peter", "that dude");
		System.out.println(d);
		
	}
	
}
