package Modules.Crusader.Entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public interface CrusaderEntity {
	
	XmlMapper mapper = new XmlMapper();
	
	default String serialize() {
		return "";
	}
	
	
	
}
