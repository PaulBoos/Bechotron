package Modules.SlashCommands;

import Modules.Music.MusicModule;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;


public enum Command {
	
	say(
			"say",
			"Make the Bot say something",
			event -> {
				event.reply(event.getOption("message").getAsString()).queue();
			},
			"say",
			event -> {
				event.getChannel().sendMessage(event.getMessage().getContentRaw().substring(event.getMessage().getContentRaw().indexOf(" "))).queue();
			},
			new OptionData(
					STRING,
					"message",
					"What to say"
			).setRequired(true)
	),
	ping(
			"ping",
			"Pong.",
			event -> {
				event.reply("Pong.").queue();
			},
			"Ping!",
			event -> {
				event.getChannel().sendMessage("Pong.").queue();
			}
	),
	delete(
			"delete",
			"Delete x Messages",
			event -> {
				Long i;
				try {
					i = event.getOption("count").getAsLong();
				} catch(Exception e) {
					i = 1L;
				}
				int length = i.intValue();
				if(length > 100) length = 100;
				if(length < 0) length = 1;
				List<Message> history = event.getChannel().getHistoryBefore(event.getInteraction().getIdLong(), length).complete().getRetrievedHistory();
				if(history.size() < length) length = history.size();
				List<Message> delete = history.subList(history.size() - length, history.size());
				if(delete.size() > 1) {
					for(Message m: delete) {
						event.getChannel().deleteMessageById(m.getId()).queue();
					}
					event.reply("Deleted " + length + " Messages.").complete().deleteOriginal().completeAfter(10, TimeUnit.SECONDS);
				} else {
					delete.get(0).delete().complete();
					event.reply("Deleted one Message.").complete().deleteOriginal().completeAfter(10, TimeUnit.SECONDS);
				}
			},
			new OptionData(
					INTEGER,
					"count",
					"Count of Messages to delete."
			)
	),
	music(
			"music",
			"Music Controller",
			event -> {
				switch(event.getOption("action").getAsString()) {
					case "play" -> {
						event.deferReply().queue();
						VoiceChannel vc = event.getMember().getVoiceState().getChannel();
						TextChannel tc = event.getTextChannel();
						String url = event.getOption("url").getAsString();
						MusicModule.manager.loadAndPlay(tc, vc, url);
						event.getHook().editOriginal("\u25B6 Playing...").queue();
					}
				}
			}
	),
	memberinfo(
			"memberinfo",
			"Tell me something about the member",
			event -> {
				OptionMapping o = event.getOption("member");
				Member target;
				if(o==null) target = event.getMember(); else target = o.getAsMember();
				StringBuilder description = new StringBuilder();
				{
					assert target != null;
					if(target.getUser().getName() != target.getEffectiveName())
						description.append("Handle: ").append(target.getUser().getName());
					switch(target.getOnlineStatus()) {
						case ONLINE -> description.append("\n\uD83D\uDFE2 Online");
						case IDLE -> description.append("\n\uD83D\uDFE1 Idle");
						case DO_NOT_DISTURB -> description.append("\n\uD83D\uDD34 Do not Disturb");
						case OFFLINE -> description.append("\n\u26AA Offline");
						default -> description.append("\n\u2754 Unknown");
					}
					if(target.getVoiceState().inVoiceChannel()) description.append("\nConnected to:\n> ").append(target.getVoiceState().getChannel().getName());

				}
				event.replyEmbeds(new EmbedBuilder()
						.setTitle(target.getEffectiveName())
						.setDescription(description.toString())
						.setFooter("Requested by " + event.getMember().getUser().getAsTag() + " #" + event.getMember().getIdLong())
						.setImage(target.getUser().getAvatarUrl()).build()).complete();
			},
			new OptionData(
					USER,
					"member",
					"Who do you want more information about?"
			)
	),
	avatar(
			"avatar",
			"Send the user's Avatar",
			event -> {
				event.reply(event.getOption("member").getAsMember().getUser().getAvatarUrl()).queue();
			}
	),
	vip(
			"vip",
			"Get / Send someone a VIP ticket!",
			event -> {
				event.deferReply().queue();
				OptionMapping target = event.getOption("target");
				if(target == null) event.getHook().sendFile(new File("images/vip.png")).queue();
					else event.getHook().sendMessage("Hey! " + target.getAsMember().getAsMention() + ", have this VIP-Ticket!").addFile(new File("images/vip.png")).queue();
			},
			new OptionData(
					USER,
					"target",
					"Send someone this Ticket!"
			).setRequired(false)
	),
	timeout(
			"timeout",
			"Timeout someone",
			event -> {
				event.reply("Okay!").complete();
				event.getOption("target").getAsMember().deafen(true).complete();
				event.getOption("target").getAsMember().deafen(false).completeAfter(event.getOption("time").getAsLong(), TimeUnit.MINUTES);
			},
			new OptionData(
					USER,
					"target",
					"who"
			).setRequired(true),
			new OptionData(
					INTEGER,
					"time",
					"how long (in minutes)"
			)
	);
	
	private final SlashExecutor slashExecutor;
	private final TextExecutor textExecutor;
	private final String command, textCommand, description;
	private final CommandData commandData;
	
	Command(String command, String description, SlashExecutor slashExecutor, String textCommand, TextExecutor textExecutor, OptionData... optionData) {
		this.command = command;
		this.textCommand = textCommand;
		this.description = description;
		this.slashExecutor = slashExecutor;
		this.textExecutor = textExecutor;
		
		commandData = new CommandData(command, description);
		for(OptionData od: optionData) commandData.addOption(od.getType(), od.getName(), od.getDescription(), od.isRequired());
	}
	
	Command(String command, String description, SlashExecutor slashExecutor, OptionData... optionData) {
		this.command = command;
		this.textCommand = null;
		this.description = description;
		this.slashExecutor = slashExecutor;
		this.textExecutor = null;
		
		this.commandData = new CommandData(command, description);
		for(OptionData od: optionData) commandData.addOption(od.getType(), od.getName(), od.getDescription(), od.isRequired());
	}
	
	//TODO SUBCOMMAND SUPPORT
//	Command(String command, String description, SlashExecutor slashExecutor, SubcommandData... subcommandData) {
//		this.command = command;
//		this.textCommand = null;
//		this.description = description;
//		this.slashExecutor = slashExecutor;
//		this.textExecutor = null;
//
//		this.commandData = new CommandData(command, description);
//
//	}
	
	Command(String textCommand, TextExecutor textExecutor) {
		this.command = null;
		this.textCommand = textCommand;
		this.description = null;
		this.slashExecutor = null;
		this.textExecutor = textExecutor;
		this.commandData = null;
	}
	
	public void execute(@NotNull SlashCommandEvent event) {
		try {
			slashExecutor.execute(event);
		} catch(Permission.NoPermissionException e) {
			event.getChannel().sendMessage(e.getMessage()).queue();
		} catch(CommandException e) {
			event.getChannel().sendMessage(e.getMessage()).queue();
			e.printStackTrace();
		}
	}
	public void execute(@NotNull GuildMessageReceivedEvent event) {
		try {
			textExecutor.execute(event);
		} catch(Permission.NoPermissionException e) {
			event.getChannel().sendMessage(e.getMessage()).queue();
		} catch(CommandException e) {
			event.getChannel().sendMessage(e.getMessage()).queue();
			e.printStackTrace();
		}
	}
	
	public CommandData init() {
		return commandData;
	}
	public String getCommand() {
		return command;
	}
	public String getTextCommand() {
		return textCommand;
	}
	public String getDescription() {
		return description;
	}
	private synchronized boolean checkPerms(CommandDB db, long memberID, Permission... perms) throws CommandException {
		try {
			ArrayList<Permission> read = db.readPermissions(memberID);
			for(Permission needed: perms) {
				boolean permissionIsMet = false;
				for(Permission check: read) {
					if(check.includesPermission(needed)) {
						permissionIsMet = true;
						break;
					}
				}
				if(!permissionIsMet) {
					StringBuilder sb = new StringBuilder();
					for(Permission perm: perms) sb.append(perm.sign).append(", ");
					sb.delete(sb.length()-2,sb.length()-1);
					throw new Permission.NoPermissionException("You are not allowed to use this command. Needed Permissions: " + sb.toString());
				}
			}
			return true;
		} catch(SQLException throwables) {
			throwables.printStackTrace();
			throw new CommandException("Could not read Database. [" + LocalDateTime.now() + "]");
		}
	}
	
	public interface SlashExecutor {
		
		void execute(@NotNull SlashCommandEvent event) throws CommandException;
		
	}
	public interface TextExecutor {
		
		void execute(@NotNull GuildMessageReceivedEvent event) throws CommandException;
		
	}
	
}
