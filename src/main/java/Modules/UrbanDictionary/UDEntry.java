package Modules.UrbanDictionary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties("current_vote")
public class UDEntry {
	
	@JsonProperty
	String word, definition, example, author, permalink;
	@JsonProperty
	int thumbs_up, thumbs_down;
	Instant timestamp;
	
	// unused
	@JsonProperty
	List<String> sound_urls;
	@JsonProperty
	int defid;
	
	@JsonSetter("written_on")
	public void setWritten_on(String written_on) {
		timestamp = Instant.parse(written_on);
	}
	
	@Override
	public String toString() {
		return "UDEntry{" +
				"word='" + word + '\'' +
				", definition='" + definition + '\'' +
				", example='" + example + '\'' +
				", author='" + author + '\'' +
				", permalink='" + permalink + '\'' +
				", thumbs_up=" + thumbs_up +
				", thumbs_down=" + thumbs_down +
				", timestamp=" + timestamp +
				", sound_urls=" + sound_urls +
				", defid=" + defid +
				'}';
	}
}
