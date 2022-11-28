package Modules.VoteModule;

import Head.BotInstance;
import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VotingModule extends ListenerAdapter implements Module {
	
	BotInstance botInstance;
	HashMap<Integer, Ballot> openBallots = new HashMap<>();
	private final long guildID;
	private final long[] mods;
	
	public VotingModule(BotInstance botInstance, long guildID, long... mods) {
		this.botInstance = botInstance;
		this.guildID = guildID;
		botInstance.jda.addEventListener(this);
		this.mods = mods;
	}
	
	public Ballot openBallot(String ballotName, String ballotDescription, long[] legalVoters, boolean allowChanges, Option[] options,
							 Ballot.BallotEventHandler ballotOpenedCode, Ballot.BallotEventHandler voteAddedCode, Ballot.BallotEventHandler voteRemovedCode, Ballot.BallotEventHandler voteChangedCode, Ballot.BallotEventHandler ballotEndCode) {
		Ballot ballot = new Ballot(this, ballotName, ballotDescription, getNewBallotID(), legalVoters, allowChanges, options, ballotOpenedCode, voteAddedCode, voteRemovedCode, voteChangedCode, ballotEndCode);
		openBallots.put(ballot.getBallotID(), ballot);
		return ballot;
	}
	
	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		String[] arguments = event.getButton().getId().split(" ");
		if(arguments[0].equals("open-ballot")) {
		
		} else if(arguments[0].equals("vote")) {
			int ballotID = Integer.parseInt(arguments[1]);
			int optionID = Integer.parseInt(arguments[2]);
			try {
				openBallots.get(ballotID).castVote(event.getUser().getIdLong(), openBallots.get(ballotID).getOptions()[optionID]);
				event.reply("Vote cast!").setEphemeral(true).queue();
			} catch(Ballot.BallotVoteException | NullPointerException e) {
				event.reply(e.getMessage()).setEphemeral(true).queue();
				if(e.getMessage().equals("This ballot is closed.")) {
					openBallots.remove(ballotID);
				}
			}
		} else if(arguments[0].equals("end-ballot")) {
			int ballotID = Integer.parseInt(arguments[1]);
			openBallots.get(ballotID).endBallot();
			openBallots.remove(ballotID);
		}
	}
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		if(event.getGuild().getIdLong() != guildID) return;
		String[] arguments = event.getMessage().getContentRaw().split(" ");
		if(arguments[0].equals("banvote")) {
			if(event.getAuthor().getIdLong() == botInstance.jda.getGuildById(guildID).getOwnerIdLong()) {
				Member banMember = event.getMessage().getMentions().getMembers().get(0);
				banBallot(
						event.getMessage().getContentRaw().substring("banvote".length() + 1),
						event.getChannel().getIdLong(),
						banMember.getIdLong(),
						mods
				);
			}
		}
	}
	
	public void banBallot(String description, long channelID, long toBan, long[] legalVoters) {
		User target = botInstance.jda.getUserById(toBan);
		Option YES = new Option("Yes", "Ban " + target.getAsMention() + " from the server."),
				NO = new Option("No", "Do not ban " + target.getAsMention() + " from the server."),
				ABSTAIN = new Option("Abstain", "Abstain from voting.");
		Ballot b = openBallot(
				"Ban " + target.getAsTag() + " from the server?",
				"**Vote to ban " + target.getAsMention() + " from the server for the following reason:**\n>>> " + description,
				legalVoters,
				true,
				new Option[] {
						YES, NO, ABSTAIN
				},
				(bot, ballot, args) -> {
					String channel = args[0];
					EmbedBuilder eb = new EmbedBuilder();
					eb.setTitle(ballot.getBallotName());
					eb.setDescription(ballot.getBallotDescription());
					for(Option o : ballot.getOptions()) {
						eb.addField(o.getOptionName(), o.getOptionDescription(), false);
					}
					bot.jda.getTextChannelById(channel).sendMessageEmbeds(eb.build())
							.addActionRow(
									Button.success("vote " + ballot.getBallotID() + " 0", YES.getOptionName()),
									Button.danger("vote " + ballot.getBallotID() + " 1", NO.getOptionName()),
									Button.secondary("vote " + ballot.getBallotID() + " 2", ABSTAIN.getOptionName())
							).queue();
				},
				(bot, ballot, args) -> {
					HashMap<Option, Integer> map = ballot.getVotesAccumulated();
					if(map.containsKey(YES) && map.get(YES) > legalVoters.length/2) {
						ballot.endBallot();
					} else if(ballot.votes.size() == ballot.getLegalVoters().length) {
						ballot.endBallot();
					}
				},
				(bot, ballot, args) -> {},
				(bot, ballot, args) -> {},
				(bot, ballot, args) -> {
					HashMap<Option, Integer> map = ballot.getVotesAccumulated();
					if(
							(map.containsKey(YES) && map.containsKey(NO) && map.get(YES) > map.get(NO)) ||
							(map.containsKey(YES) && !map.containsKey(NO))
					) {
						try {
							bot.jda.getGuildById(guildID).ban(bot.jda.getUserById(toBan), 0, TimeUnit.SECONDS).queue();
							bot.jda.getGuildById(guildID).getTextChannelById(channelID).sendMessage("Banned " + target.getAsMention() + " from the server. The vote was closed.").queue();
							return;
						} catch(IllegalArgumentException e) {
							bot.jda.getGuildById(guildID).getTextChannelById(channelID).sendMessage("Failed to ban " + target.getAsMention() + " from the server. The vote was closed.").queue();
							return;
						}
					}
					bot.jda.getGuildById(guildID).getTextChannelById(channelID).sendMessage("The ballot to ban " + target.getAsMention() + " from the server has ended with no majority.").queue();
				}
		);
		b.startBallot(channelID);
	}
	
	protected int getNewBallotID() {
		int rand = (int) (Math.random() * 1000000000);
		while(openBallots.containsKey(rand)) {
			rand = (int) (Math.random() * 1000000000);
		}
		return rand;
	}
	
	@Override
	public void init(Guild guild) {
	
	}
	
	@Override
	public String getDescription() {
		return "This module allows users to create and vote on ballots.";
	}
	
	@Override
	public String getName() {
		return "Voting Module";
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
