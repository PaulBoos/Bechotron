package Modules.SlashCommands;

import Modules.Music.MusicModule;
import Modules.Plex.Library.Media;
import Modules.Plex.Library.PlexAlbum;
import Modules.Plex.Library.PlexArtist;
import Modules.Plex.Library.PlexTrack;
import Modules.Plex.PlexServerModule;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.AttachedFile;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;


public class Command {
	
	public static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Command.class);
	
	public static final Command
		SAY = new Command(
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
		);
	public static final Command PING = new Command(
				"ping",
				"Pong.",
				event -> event.reply("Pong.").queue(),
				"Ping!",
				event -> event.getChannel().sendMessage("Pong.").queue()
		);
	public static final Command DELETE = new Command(
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
		);
	public static final Command MUSIC = new Command(
			"music",
			event -> {
				switch(event.getSubcommandName()) {
					case "play" -> {
						event.deferReply().queue();
						AudioChannel vc = event.getMember().getVoiceState().getChannel();
						TextChannel tc = event.getChannel().asTextChannel();
						String url = event.getOption("song").getAsString();
						MusicModule.manager.loadAndPlay(tc, vc, url);
						event.getHook().editOriginal("▶ Playing...").queue();
					}
					case "skip" -> {
						event.deferReply().queue();
						TextChannel tc = event.getChannel().asTextChannel();
						MusicModule.manager.skipTrack(tc);
						event.getHook().editOriginal("⏭ Skipping track!").queue();
					}
					case "stop" -> {
						event.deferReply().queue();
						TextChannel tc = event.getChannel().asTextChannel();
						MusicModule.manager.stopPlayback(tc);
						event.getHook().editOriginal("⏹ Stopping Playback!").queue();
					}
					case "volume" -> {
						event.deferReply().queue();
						MusicModule.manager.setVolume(event.getGuild(), Math.min(100, event.getOption("volume").getAsInt()));
						event.getHook().editOriginal(String.format("Set volume to %d%%", event.getOption("volume").getAsInt())).queue();
					}
				}
			},
			"Music Controller",
			new SubcommandData("play", "Play a song or add it to the playlist.")
				.addOption(STRING, "song", "what song to add", true),
			new SubcommandData("stop", "Skip the current song and clear the queue."),
			new SubcommandData("skip", "Skip the current song."),
			new SubcommandData("volume", "Change the current Volume.")
				.addOption(INTEGER, "volume", "Use a value between 1 - 200%", true)
		);
	
	public static final Command MEMBERINFO = new Command(
			"memberinfo",
			"Tell me something about the member",
			event -> {
				OptionMapping o = event.getOption("member");
				Member target = o == null ? event.getMember() : o.getAsMember();
				StringBuilder description = new StringBuilder();
				{
					assert target != null;
					if(!target.getUser().getName().equals(target.getEffectiveName()))
						description.append("Handle: ").append(target.getUser().getName());
					switch(target.getOnlineStatus()) {
						case ONLINE -> description.append("\n\uD83D\uDFE2 Online");
						case IDLE -> description.append("\n\uD83D\uDFE1 Idle");
						case DO_NOT_DISTURB -> description.append("\n\uD83D\uDD34 Do not Disturb");
						case OFFLINE -> description.append("\n⚪ Offline");
						default -> description.append("\n❔ Unknown");
					}
					if(target.getVoiceState().inAudioChannel()) description.append("\n\nConnected to:\n> ").append(target.getVoiceState().getChannel().getName());
				}
				event.replyEmbeds(new EmbedBuilder()
					.setTitle(target.getEffectiveName())
					.setDescription(description.toString())
					.setFooter("Requested by " + event.getMember().getUser().getName() + " #" + event.getMember().getIdLong())
					.setImage(target.getUser().getAvatarUrl()).build()).complete();
			},
			new OptionData(
					USER,
					"member",
					"Who do you want more information about?"
			)
		);
	public static final Command AVATAR = new Command(
			"avatar",
			"Send the user's Avatar",
			event -> event.reply(event.getOption("member").getAsMember().getUser().getEffectiveAvatarUrl()).queue(),
			new OptionData(
					USER,
					"member",
					"Whose Avatar you want to see",
					true
			)
		);
	public static final Command VIP = new Command(
			"vip",
			"Get / Send someone a VIP ticket!",
			event -> {
				event.deferReply().queue();
				OptionMapping target = event.getOption("target");
				if(target == null) event.getHook().editOriginalAttachments(AttachedFile.fromData(new File("images/vip.png"))).queue();
				else event.getHook().sendMessage("Hey! " + target.getAsMember().getAsMention() + ", have this VIP-Ticket!").addFiles(AttachedFile.fromData(new File("sources/images/vip.png"))).queue();
			},
			new OptionData(
				USER,
				"target",
				"Send someone this Ticket!"
			).setRequired(false)
		);
	public static final Command TIMEOUT = new Command(
			"timeout",
			"Timeout someone",
			event -> {
				event.reply("Okay!").complete();
				event.getOption("target").getAsMember().deafen(true).complete();
				event.getOption("target").getAsMember().deafen(false).completeAfter(event.getOption("time").getAsLong(), TimeUnit.SECONDS);
				event.getOption("target").getAsMember().deafen(false).completeAfter(event.getOption("time").getAsLong() + 10, TimeUnit.SECONDS);
			},
			new OptionData(
				USER,
				"target",
				"who"
			).setRequired(true),
			new OptionData(
				INTEGER,
				"time",
				"how long (in seconds)"
			)
		);
	public static final Command GLOBALBANREQUEST = new Command(
				"banrequest",
				"Request a global ban",
				event -> {
					event.deferReply().queue();
					TextChannel requestChannel = event.getJDA().getTextChannelById(1016790193161916467L);
					try {
						requestChannel.sendMessageEmbeds(
								new EmbedBuilder()
										.setTitle("Global Ban Request by " + event.getMember().getUser().getAsTag())
										.setThumbnail(event.getMember().getAvatarUrl())
										.setDescription(
												"**Target:** " + event.getOption("target").getAsMember().getAsMention()
												+ "\n\n**Reason:**\n" + event.getOption("reason").getAsString())
										.build()
						).addActionRow(
								new ButtonImpl("ban " + event.getOption("target").getAsMember().getIdLong(), "Accept", ButtonStyle.SUCCESS, false, null),
								new ButtonImpl("deletemessage", "Decline", ButtonStyle.DANGER, false, null)
						).queue();
						event.getHook().editOriginal("Processing Request.").queue();
					} catch(Exception e) {
						event.getHook().editOriginal("Could not process request.").queue();
						e.printStackTrace();
					}
					
				},
				new OptionData(
						USER,
						"target",
						"who to ban"
				).setRequired(true),
				new OptionData(
						STRING,
						"reason",
						"reason for banning"
				).setRequired(true)
		);
	
	public static final Command PLEX = new Command(
			"plex",
			"Request to play a Song, Album or Discography hosted on my Plex Server",
			event -> {
				event.deferReply().queue();
				final int id;
				int i;
				try {
					i = Integer.parseInt(event.getOption("plex").getAsString());
				} catch(NumberFormatException ignored) {
					i = -1;
				}
				id = i;
				String search = event.getOption("plex").getAsString().toLowerCase();
				
				
				PlexServerModule.getSearchable().stream().filter(
						object -> object.getID() == id || object.getSearchable().toLowerCase().contains(search)
				).findAny().ifPresent(
						searchable -> {
							if(event.getMember().getVoiceState().getChannel() != null) {
								if(searchable instanceof PlexArtist artist) {
									event.getHook().editOriginal("Playing " + artist.getName() + "'s Discography.").queue();
									for(PlexAlbum album: artist.getAlbums())
										for(PlexTrack track: album.getTracks())
											playSomePlexSong(event, track.getId());
								} else if(searchable instanceof PlexAlbum album) {
									event.getHook().editOriginal("Playing " + album.getTitle()).queue();
									for(PlexTrack track: album.getTracks())
										playSomePlexSong(event, track.getId());
								} else if(searchable instanceof PlexTrack song) {
									event.getHook().editOriginal("Playing " + song.getTitle()).queue();
									playSomePlexSong(event, song.getId());
								}
							} else event.getHook().editOriginal("You are not connected to a Voice Channel!").queue();
						}
				);
			},
			new OptionData(
					STRING,
					"plex",
					"Artist, Album or Song",
					true
			)
		);
	
	private static void playSomePlexSong(SlashCommandInteractionEvent event, int id) {
		try {
			PlexServerModule.getSongs().stream().filter(plexTrack -> plexTrack.getId() == id).findAny().ifPresent(track -> {
				for(Media m: track.getMedia())
					for(String url: m.getPlayableURLs())
						MusicModule.manager.silentLoadAndPlay(
								event.getMember().getVoiceState().getChannel(), url
						);
			});
		} catch(NullPointerException e) {
			throw new RuntimeException(e);
		}
	}
	
	private final SlashExecutor slashExecutor;
	private final TextExecutor textExecutor;
	private final String command, textCommand, description;
	private final CommandDataImpl commandData;
	
	public Command(String command, String description, SlashExecutor slashExecutor, String textCommand, TextExecutor textExecutor, OptionData... optionData) {
		this.command = command;
		this.textCommand = textCommand;
		this.description = description;
		this.slashExecutor = slashExecutor;
		this.textExecutor = textExecutor;
		
		commandData = new CommandDataImpl(command, description);
		for(OptionData od: optionData) commandData.addOption(od.getType(), od.getName(), od.getDescription(), od.isRequired());
	}
	
	public Command(String command, String description, SlashExecutor slashExecutor, OptionData... optionData) {
		this.command = command;
		this.textCommand = null;
		this.description = description;
		this.slashExecutor = slashExecutor;
		this.textExecutor = null;
		
		this.commandData = new CommandDataImpl(command, description);
		for(OptionData od: optionData) commandData.addOption(od.getType(), od.getName(), od.getDescription(), od.isRequired());
	}
	
	public Command(String command, SlashExecutor slashExecutor, String description, SubcommandData... subcommandData) {
		this.command = command;
		this.textCommand = null;
		this.description = description;
		this.slashExecutor = slashExecutor;
		this.textExecutor = null;
		
		this.commandData = new CommandDataImpl(command, description);
		commandData.addSubcommands(subcommandData);
	}
	
	public Command(String textCommand, TextExecutor textExecutor) {
		this.command = null;
		this.textCommand = textCommand;
		this.description = null;
		this.slashExecutor = null;
		this.textExecutor = textExecutor;
		this.commandData = null;
	}
	
	public void execute(@NotNull SlashCommandInteractionEvent event) {
		try {
			slashExecutor.execute(event);
		} catch(Permission.NoPermissionException e) {
			event.getChannel().sendMessage(e.getMessage()).queue();
		} catch(CommandException e) {
			event.getChannel().sendMessage(e.getMessage()).queue();
			e.printStackTrace();
		}
	}
	public void execute(@NotNull MessageReceivedEvent event) {
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
					throw new Permission.NoPermissionException("You are not allowed to use this command. Needed Permissions: " + sb);
				}
			}
			return true;
		} catch(SQLException throwables) {
			throwables.printStackTrace();
			throw new CommandException("Could not read Database. [" + LocalDateTime.now() + "]");
		}
	}
	
	
	public interface SlashExecutor {
		
		void execute(@NotNull SlashCommandInteractionEvent event) throws CommandException;
		
	}
	
	public interface TextExecutor {
		
		void execute(@NotNull MessageReceivedEvent event) throws CommandException;
		
	}
	
}
