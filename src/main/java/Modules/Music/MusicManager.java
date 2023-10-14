package Modules.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MusicManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MusicManager.class);
	
	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;
	
	public MusicManager() {
		this.musicManagers = new HashMap<>();
		
		this.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}
	
	public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		long guildId = Long.parseLong(guild.getId());
		GuildMusicManager musicManager = musicManagers.get(guildId);
		
		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager);
			musicManagers.put(guildId, musicManager);
		}
		
		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		
		return musicManager;
	}
	
	public void setVolume(Guild g, int volume) {
		GuildMusicManager gmm = getGuildAudioPlayer(g);
		gmm.player.setVolume(volume);
	}
	
	public void loadAndPlay(final TextChannel channel, final AudioChannel vchannel, final String trackUrl) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		
		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				//channel.sendMessage("Adding to queue " + track.getInfo().title).queue();
				
				play(channel.getGuild(), musicManager, track, vchannel);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();
				
				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}
				
				channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();
				
				play(channel.getGuild(), musicManager, firstTrack, vchannel);
			}
			
			@Override
			public void noMatches() {
				channel.sendMessage("I found no matches. (Maybe tell Becher)").queue();
				LOGGER.info("Found nothing when trying to load " + trackUrl);
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("An error occured. Please try again later. (Maybe tell Becher)").queue();
				LOGGER.info("Could not play " + trackUrl + " because: " + exception.getMessage());
			}
		});
	}
	
	public void silentLoadAndPlay(final AudioChannel vchannel, final String trackUrl) {
		GuildMusicManager musicManager = getGuildAudioPlayer(vchannel.getGuild());
		
		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				play(vchannel.getGuild(), musicManager, track, vchannel);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();
				
				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}
				
				play(vchannel.getGuild(), musicManager, firstTrack, vchannel);
			}
			
			@Override
			public void noMatches() {
				LOGGER.warn("Found nothing when trying to load " + trackUrl);
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				LOGGER.error("Could not play " + trackUrl + " because: " + exception.getMessage());
			}
		});
	}
	
	public AudioPlayerManager getPlayerManager() {
		return playerManager;
	}
	
	private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, AudioChannel vChannel) {
		connectToVoiceChannel(guild.getAudioManager(), vChannel);
		
		musicManager.scheduler.queue(track);
	}
	
	private void connectToVoiceChannel(AudioManager audioManager, AudioChannel vChannel) {
		if (!audioManager.isConnected()) {
			audioManager.openAudioConnection(vChannel);
		}
	}
	
	public void skipTrack(TextChannel channel) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.nextTrack(false);
	}
	
	public void stopPlayback(TextChannel channel) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.clearQueue();
		skipTrack(channel);
	}
	
}