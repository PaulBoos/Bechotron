package Modules.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class TrackScheduler extends AudioEventAdapter {
	
	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;
	
	public TrackScheduler(AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
	}
	
	public boolean queue(AudioTrack track) {
		if(!this.player.startTrack(track, true)) {
			return this.queue.offer(track);
		}
		return false;
	}
	
	public void clearQueue() {
		queue.clear();
	}
	
	public void nextTrack(boolean noInterrupt) {
		this.player.startTrack(queue.poll(), noInterrupt);
	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if(endReason.mayStartNext) {
			nextTrack(false);
		}
	}
}
