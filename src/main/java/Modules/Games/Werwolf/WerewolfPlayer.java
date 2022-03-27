package Modules.Games.Werwolf;

import net.dv8tion.jda.api.entities.PrivateChannel;
import org.apache.commons.collections4.set.ListOrderedSet;

import java.util.Set;

public class WerewolfPlayer {
	
	private final long id;
	private final WerewolfGame game;
	private long messageChannel;
	private Set<WerewolfRole> roles;
	private WerewolfPlayer inLoveWith;
	private boolean isDead = false;
	
	WerewolfPlayer(long id, WerewolfGame game) {
		this.id = id;
		this.game = game;
		roles = new ListOrderedSet<>();
	}
	
	public PrivateChannel getMessageChannel() throws UserNotRecognizedException {
		try {
			return game.jda.getUserById(id).openPrivateChannel().complete();
		} catch(NullPointerException e) {
			throw new UserNotRecognizedException();
		}
		
	}
	public long getId() {
		return id;
	}
	public Set<WerewolfRole> getRoles() {
		return roles;
	}
	public boolean hasRole(WerewolfRole role) {
		return roles.contains(role);
	}
	public boolean isInLove() {
		return inLoveWith != null;
	}
	public WerewolfPlayer inLoveWith() {
		return inLoveWith;
	}
	
	
	/**
	 *
	 * @param role role to add.
	 * @return if addition was successful.
	 */
	public boolean assignRole(WerewolfRole role) {
		return roles.add(role);
	}
	
	/**
	 *
	 * @return true if player is killed and not already dead.
	 */
	public boolean killPlayer() {
		if(!isDead) {
			isDead = true;
			//TODO kill
			return true;
		} else {
			return false;
		}
	}
	
	
	
	protected static class UserNotRecognizedException extends Exception {}
	
}
