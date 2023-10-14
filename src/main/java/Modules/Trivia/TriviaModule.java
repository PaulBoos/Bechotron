package Modules.Trivia;

import Head.BotInstance;
import Modules.Module;
import Modules.RequireModuleHook;
import Modules.SlashCommands.Command;
import Utils.SimpleHttpConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.MessageEditAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TriviaModule implements Module {
	
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TriviaModule.class);
	private static final JsonMapper MAPPER = new JsonMapper();
	
	private static final int MAX_QUESTIONS = 50;
	private static final HashMap<String, Integer> categories = new HashMap<>();
	private static final String
			questionURL = "https://opentdb.com/api.php?amount=%s&category=%s&difficulty=%s&type=%s&token=%s",
			categoryURL = "https://opentdb.com/api_category.php",
			tokenURL = "https://opentdb.com/api_token.php?command=request";
	
	private final BotInstance botInstance;
	
	public TriviaModule(BotInstance botInstance) {
		this.botInstance = botInstance;
		botInstance.jda.addEventListener(new ButtonListener());
	}
	
	public void init() {
		try {
			CategoryResponse response = MAPPER.readValue(new URL(categoryURL), CategoryResponse.class);
			if(response.response_code == 0)
				response.trivia_categories.forEach(this::addCategory);
			else
				throw new RuntimeException("Could not get categories from Trivia API, Exit Code: " + response.response_code);
		} catch(JsonProcessingException e) {
			LOGGER.error(e.getMessage());
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static final Command TRIVIA = new Command(
			"trivia",
			"Request a trivia question (Start a new trivia thread)",
			event -> {
				newTrivia(event, BotInstance.triviaModule.grabToken(), "Trivia Thread Started! - " + event.getMember().getAsMention());
			}
	);
	
	private static void newTrivia(IReplyCallback event, String token, String reason) { // IReplyCallback is a functional interface for all >replyable< events
		try {
			QuestionChainLink trivia = BotInstance.triviaModule.askOnce(token);
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Trivia Question - " + trivia.question.category());
			eb.setDescription(trivia.question.question());
			List<Button> buttons = new ArrayList<>();
			
			// Add Answers as Buttons
			buttons.add(createButton(trivia.question.correct_answer(), "correctAnswer_" + trivia.token));
			List<String> incorrectAnswers = trivia.question.incorrect_answers();
			for(int i = 0; i < incorrectAnswers.size(); i++) {
				String wrongAnswer = incorrectAnswers.get(i);
				buttons.add(createButton(wrongAnswer, "wrongAnswer_" + i + "_" + trivia.token));
			}
			
			// Mix buttons
			for(int i = 0; i < buttons.size(); i++) {
				int randomIndex = (int) (Math.random() * buttons.size());
				Button temp = buttons.get(i);
				buttons.set(i, buttons.get(randomIndex));
				buttons.set(randomIndex, temp);
			}
			
			// Put Buttons in Rows
			ReplyCallbackAction callback = event.reply(reason).setSuppressedNotifications(true).addEmbeds(eb.build());
			while(!buttons.isEmpty()) {
				if(buttons.size() >= 5) {
					callback.addActionRow(buttons.subList(0, 5));
					buttons = buttons.subList(5, buttons.size());
				} else {
					callback.addActionRow(buttons);
					buttons.clear();
				}
			}
			
			// Put Skip / Exit at the end
			callback.addActionRow(
					Button.success("skipTrivia_" + trivia.token, Emoji.fromUnicode("⏭")),
					Button.danger("exitTrivia", "Exit"));
			
			callback.queue();
		} catch(TriviaRequestException e) {
			
			event.reply("Something went wrong. Oops!").queue();
			LOGGER.error("Error: ", e);
		}
	}
	
	private static Button createButton(String label, String id) {
		if(label.equalsIgnoreCase("true"))
			return Button.secondary(id, Emoji.fromUnicode("✅"));
		else if(label.equalsIgnoreCase("false"))
			return Button.secondary(id, Emoji.fromUnicode("❎"));
		else return Button.secondary(id, label);
	}
	
	private static class ButtonListener extends ListenerAdapter {
		@Override
		public void onButtonInteraction(ButtonInteractionEvent event) {
			if(event.getComponentId().startsWith("correctAnswer_")) {
				String token = event.getComponentId().substring(event.getComponentId().lastIndexOf('_') + 1);
				disableAllButtons(event.getMessage());
				event.reply(event.getMember().getAsMention() + " YES! " + (!event.getButton().getLabel().isEmpty() ? ("\"" + event.getButton().getLabel() + "\" is the correct answer.") : event.getButton().getEmoji().getFormatted() + " was correct."))
						.addActionRow(
								Button.primary("nextTrivia_" + token, Emoji.fromUnicode("\uD83D\uDD02")),
								Button.danger("exitTrivia", "Exit")
				).setSuppressedNotifications(true).queue();
			} else if(event.getComponentId().startsWith("wrongAnswer_")) {
				event.reply(event.getMember().getAsMention() + "Nope! " + (!event.getButton().getLabel().isEmpty() ? ("\"" + event.getButton().getLabel() + "\" is not the correct answer.") : event.getButton().getEmoji().getFormatted() + " was incorrect."))
						.setSuppressedNotifications(true).queue();
				disableOneButton(event.getMessage(), event.getComponent());
			} else if(event.getComponentId().startsWith("nextTrivia_")) {
				String token = event.getComponentId().substring(event.getComponentId().lastIndexOf('_') + 1);
				disableAllButtons(event.getMessage());
				newTrivia(event, token, "Next Question - " + event.getMember().getAsMention());
			} else if(event.getComponentId().startsWith("skipTrivia_")) {
				String token = event.getComponentId().substring(event.getComponentId().lastIndexOf('_') + 1);
				disableAllButtons(event.getMessage());
				newTrivia(event, token, "Skipped by " + event.getMember().getAsMention());
			} else if(event.getComponentId().equals("exitTrivia")) {
				disableAllButtons(event.getMessage());
				event.reply("Okay, goodbye " + event.getMember().getAsMention() + "! \uD83D\uDC4B Type `/trivia` again if you want to challenge your knowledge again!").setSuppressedNotifications(true).queue();
			}
		}
	}
	
	private static void disableOneButton(Message message, Button button) {
		// Get the Edit Action
		MessageEditAction messageEditAction = message.editMessageComponents();
		
		// Disable all Buttons
		List<ActionRow> actionRows = message.getActionRows();
		List<LayoutComponent> components = new ArrayList<>();
		for(ActionRow row : actionRows) {
			List<Button> newButtons = new ArrayList<>();
			for(Button b : row.getButtons()) {
				if(b.equals(button)) newButtons.add(button.asDisabled());
				else newButtons.add(b);
			}
			components.add(ActionRow.of(newButtons));
		}
		
		// Overwrite
		messageEditAction.setComponents(components).queue();
	}
	
	private static void disableAllButtons(Message message) {
		// Get the Edit Action
		MessageEditAction messageEditAction = message.editMessageComponents();
		
		// Disable all Buttons
		List<ActionRow> actionRows = message.getActionRows();
		List<LayoutComponent> components = new ArrayList<>();
		for(ActionRow row : actionRows) {
			List<Button> newButtons = new ArrayList<>();
			for(Button button : row.getButtons()) {
				newButtons.add(button.asDisabled());
			}
			components.add(ActionRow.of(newButtons));
		}
		
		// Overwrite
		messageEditAction.setComponents(components).queue();
	}
	
	private record QuestionResponse(int response_code, List<TriviaQuestion> results) {}
	private record CategoryResponse(int response_code, List<Category> trivia_categories) {}
	private record TokenResponse(int response_code, String response_message, String token) {}
	private record Category(int id, String name) {}
	private void addCategory(Category category) {
		categories.put(category.name, category.id);
	}
	public record QuestionChainLink(TriviaQuestion question, String token) {}
	
	public static class TriviaRequestException extends Exception {
		public TriviaRequestException() {
			super();
		}
		
		public TriviaRequestException(String message) {
			super(message);
		}
		
		public TriviaRequestException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public TriviaRequestException(Throwable cause) {
			super(cause);
		}
		
		protected TriviaRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}
	public static class NoQuestionsFoundException extends TriviaRequestException {
		public NoQuestionsFoundException() {
			super();
		}
		
		public NoQuestionsFoundException(String message) {
			super(message);
		}
		
		public NoQuestionsFoundException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public NoQuestionsFoundException(Throwable cause) {
			super(cause);
		}
		
		protected NoQuestionsFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}
	public static class TokenRenewalRequiredException extends TriviaRequestException {
		public TokenRenewalRequiredException() {
			super();
		}
		
		public TokenRenewalRequiredException(String message) {
			super(message);
		}
		
		public TokenRenewalRequiredException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public TokenRenewalRequiredException(Throwable cause) {
			super(cause);
		}
		
		protected TokenRenewalRequiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}
	
	public QuestionChainLink askOnce(@Nullable String token) throws TriviaRequestException {
		return askOnce("", token);
	}
	
	public QuestionChainLink askOnce(@Nullable String category, @Nullable String token) throws TriviaRequestException {
		category = category == null ? "" : category;
		return askOnce(category, "", "", token);
	}
	
	public QuestionChainLink askOnce(@Nullable String category, @Nullable String difficulty, @Nullable String type, @Nullable String token) throws TriviaRequestException {
		return askOnce(1, category, difficulty, type, token);
	}
	
	public List<QuestionChainLink> getQuestions(int amount, @Nullable String token) throws TriviaRequestException {
		return getQuestions(amount, "", token);
	}
	
	public List<QuestionChainLink> getQuestions(int amount, @Nullable String category, @Nullable String token) throws TriviaRequestException {
		return getQuestions(amount, category, "", "", token);
	}
	
	public List<QuestionChainLink> getQuestions(int amount, @Nullable String category, @Nullable String difficulty, @Nullable String type, @Nullable String token) throws TriviaRequestException {
		return getQuestions(1, amount, category, difficulty, type, token);
	}
	
	
	private QuestionChainLink askOnce(int retries, @Nullable String category, @Nullable String difficulty, @Nullable String type, @Nullable String token) throws TriviaRequestException {
		if(retries < 0) throw new TriviaRequestException("Token grab unsuccessful. Retry count exceeded.");
		try {
			category = category == null ? "" : category;
			difficulty = difficulty == null ? "" : difficulty;
			type = type == null ? "" : type;
			token = token == null ? grabToken() : token;
			QuestionResponse result = MAPPER.readValue(new URL(String.format(questionURL, 1, category, difficulty, type, token)), QuestionResponse.class);
			TriviaRequestException exception = handleResponseCode(result.response_code);
			if(exception != null) {
				if(exception instanceof TokenRenewalRequiredException) {
					return askOnce(retries - 1, category, difficulty, type, grabToken());
				} else {
					throw exception;
				}
			}
			TriviaQuestion resultQuestion = TriviaQuestion.deescapeCharacters(result.results().get(0));
			return new QuestionChainLink(resultQuestion, token);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private List<QuestionChainLink> getQuestions(int retries, int amount, @Nullable String category, @Nullable String difficulty, @Nullable String type, @Nullable String token) throws TriviaRequestException {
		if(retries < 0) throw new TriviaRequestException("Token grab unsuccessful. Retry count exceeded.");
		try {
			category = category == null ? "" : category;
			difficulty = difficulty == null ? "" : difficulty;
			type = type == null ? "" : type;
			token = token == null ? grabToken() : token;
			QuestionResponse result = MAPPER.readValue(new URL(String.format(questionURL, amount, category, difficulty, type, token)), QuestionResponse.class);
			TriviaRequestException exception = handleResponseCode(result.response_code);
			if(exception != null) {
				if(exception instanceof TokenRenewalRequiredException) {
					return getQuestions(retries - 1, category, difficulty, type, grabToken());
				} else {
					throw exception;
				}
			}
			List<QuestionChainLink> output = new ArrayList<>();
			final String finalToken = token; // For runtime safety
			result.results.forEach(item -> output.add(new QuestionChainLink(TriviaQuestion.deescapeCharacters(item), finalToken)));
			return output;
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private TriviaRequestException handleResponseCode(int responseCode) {
		return switch(responseCode) {
			case 1    -> new NoQuestionsFoundException();
			case 2    -> new TriviaRequestException("Invalid Parameter - \"Contains an invalid parameter. Arguements passed in aren't valid.\"");
			case 3, 4 -> new TokenRenewalRequiredException();
			default   -> null;
		};
	}
	
	private String grabToken() {
		try {
			TokenResponse response = MAPPER.readValue(new URL(tokenURL), TokenResponse.class);
			if(response.response_code != 0)
				throw new RuntimeException("Could not get token from Trivia API, Exit Code: " + response.response_code);
			return response.token;
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public String getDescription() {
		return null;
	}
	
	@Override
	public List<RequireModuleHook> requireModules() {
		return null;
	}
	
	@Override
	public RequireModuleHook getMyRequireModuleHook() {
		return null;
	}
}
