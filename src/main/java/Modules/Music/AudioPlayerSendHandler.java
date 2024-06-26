package Modules.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

class AudioPlayerSendHandler implements AudioSendHandler {
	
	private final AudioPlayer audioPlayer;
	private final ByteBuffer buffer;
	private final MutableAudioFrame frame;
	
	public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
		this.buffer = ByteBuffer.allocate(1024);
		this.frame = new MutableAudioFrame();
		frame.setBuffer(buffer);
	}
	
	@Override
	public boolean canProvide() {
		return this.audioPlayer.provide(frame);
	}
	
	@Override
	public ByteBuffer provide20MsAudio() {
		return this.buffer.flip();
	}
	
	@Override
	public boolean isOpus() {
		return true;
	}
	
}
