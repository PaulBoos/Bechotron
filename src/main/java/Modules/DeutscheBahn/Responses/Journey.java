package Modules.DeutscheBahn.Responses;

import Modules.DeutscheBahn.DBModule;
import Modules.DeutscheBahn.Objects.JourneyStop;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.beans.ConstructorProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "JourneyDetail")
public class Journey {
	
	@JacksonXmlProperty(localName = "Stop")
	private List<JourneyStop> route;
	@JacksonXmlProperty(localName = "Name")
	private List<LineSection> names;
	@JacksonXmlProperty(localName = "Type")
	private List<TypeSection> type;
	@JacksonXmlProperty(localName = "Operator")
	private List<OperatorSection> ops;
	@JacksonXmlProperty(localName = "Note")
	private List<Note> notes;
	
	@ConstructorProperties({"Stops", "Names", "Types", "Operators", "Notes"})
	public Journey(List<JourneyStop> route, List<LineSection> names, List<TypeSection> type, List<OperatorSection> ops, List<Note> notes) {
		this.route = route;
		this.names = names;
		this.type = type;
		this.ops = ops;
		this.notes = notes;
	}
	
	public List<JourneyStop> getRoute() {
		return route;
	}
	
	public List<LineSection> getNames() {
		return names;
	}
	
	public List<TypeSection> getType() {
		return type;
	}
	
	public List<OperatorSection> getOps() {
		return ops;
	}
	
	public List<Note> getNotes() {
		return notes;
	}
	
	@Override
	public String toString() {
		return "Journey{" +
				"route=" + route + ", \n" +
				"names=" + names + ", \n" +
				"type=" + type + ", \n" +
				"ops=" + ops + ", \n" +
				"notes=" + notes + ", \n" +
				'}';
	}
	
	@JacksonXmlRootElement(localName = "Name")
	public static class LineSection {
		@JacksonXmlProperty(isAttribute = true)
		protected final String name;
		@JacksonXmlProperty(isAttribute = true)
		protected final int routeIdxFrom, routeIdxTo;
		@ConstructorProperties({"name", "routeIdxFrom", "routeIdxTo"})
		private LineSection(String name, int routeIdxFrom, int routeIdxTo) {
			this.name = DBModule.fixHTMLCharacters(name);
			this.routeIdxFrom = routeIdxFrom;
			this.routeIdxTo = routeIdxTo;
		}
		@Override
		public String toString() {
			return "\n  LineSection: #" + routeIdxFrom + " [" + name + "] #" + routeIdxTo;
		}
		
		public String getName() {
			return name;
		}
		public int getRouteIdxFrom() {
			return routeIdxFrom;
		}
		public int getRouteIdxTo() {
			return routeIdxTo;
		}
		
	}
	
	@JacksonXmlRootElement(localName = "Type")
	public static class TypeSection {
		@JacksonXmlProperty(isAttribute = true)
		protected final String type;
		@JacksonXmlProperty(isAttribute = true)
		protected final int routeIdxFrom, routeIdxTo;
		@ConstructorProperties({"type", "routeIdxFrom", "routeIdxTo"})
		private TypeSection(String type, int routeIdxFrom, int routeIdxTo) {
			this.type = DBModule.fixHTMLCharacters(type);
			this.routeIdxFrom = routeIdxFrom;
			this.routeIdxTo = routeIdxTo;
		}
		@Override
		public String toString() {
			return "\n  TypeSection: #" + routeIdxFrom + " [" + type + "] #" + routeIdxTo;
		}
		
		public String getType() {
			return type;
		}
		public int getRouteIdxFrom() {
			return routeIdxFrom;
		}
		public int getRouteIdxTo() {
			return routeIdxTo;
		}
		
	}
	
	@JacksonXmlRootElement(localName = "Operator")
	public static class OperatorSection {
		@JacksonXmlProperty(isAttribute = true)
		protected final String name;
		@JacksonXmlProperty(isAttribute = true)
		protected final int routeIdxFrom, routeIdxTo;
		@ConstructorProperties({"name", "routeIdxFrom", "routeIdxTo"})
		private OperatorSection(String name, int routeIdxFrom, int routeIdxTo) {
			this.name = DBModule.fixHTMLCharacters(name);
			this.routeIdxFrom = routeIdxFrom;
			this.routeIdxTo = routeIdxTo;
		}
		@Override
		public String toString() {
			return "\n  Operator: #" + routeIdxFrom + " [" + name + "] #" + routeIdxTo;
		}
		
		public String getName() {
			return name;
		}
		public int getRouteIdxFrom() {
			return routeIdxFrom;
		}
		public int getRouteIdxTo() {
			return routeIdxTo;
		}
		
	}
	
	public static class Note {
		@JacksonXmlProperty(isAttribute = true)
		protected String key;
		@JacksonXmlProperty(isAttribute = true)
		protected int routeIdxFrom, routeIdxTo, priority;
		@JacksonXmlText
		protected String note;
		
		@Override
		public String toString() {
			return "\n  Note: [key=" + key + ", " + routeIdxFrom + " - " + routeIdxTo + ", priority=" + priority + "]\n    \"" + note + "\"";
		}
		
		public String getKey() {
			return key;
		}
		public int getRouteIdxFrom() {
			return routeIdxFrom;
		}
		public int getRouteIdxTo() {
			return routeIdxTo;
		}
		public int getPriority() {
			return priority;
		}
		public String getNote() {
			return note;
		}
		
	}
}
