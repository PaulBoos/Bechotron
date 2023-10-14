package Modules.Trivia;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.unbescape.html.HtmlEscape;

import java.util.ArrayList;
import java.util.List;

@JsonRootName("triviaQuestion")
public record TriviaQuestion(String category, Type type, Difficulty difficulty, String question, String correct_answer, List<String> incorrect_answers) {
	
	public static TriviaQuestion deescapeCharacters(TriviaQuestion triviaQuestion) {
		List<String> incorrect_answers = triviaQuestion.incorrect_answers;
		List<String> slightly_less_incorrect_answers = new ArrayList<>();
		for(String incorrect_answer: incorrect_answers) {
			slightly_less_incorrect_answers.add(HtmlEscape.unescapeHtml(incorrect_answer));
		}
		return new TriviaQuestion(
				HtmlEscape.unescapeHtml(triviaQuestion.category),
				triviaQuestion.type,
				triviaQuestion.difficulty,
				HtmlEscape.unescapeHtml(triviaQuestion.question),
				HtmlEscape.unescapeHtml(triviaQuestion.correct_answer),
				slightly_less_incorrect_answers
		);
	}
	
	@JsonRootName("difficulty")
	public static enum Difficulty {
		@JsonProperty("easy") EASY, @JsonProperty("medium") MEDIUM, @JsonProperty("hard") HARD
	}
	
	@JsonRootName("type")
	public static enum Type {
		@JsonProperty("multiple") MULTIPLE, @JsonProperty("boolean") BOOLEAN
	}
	
}
