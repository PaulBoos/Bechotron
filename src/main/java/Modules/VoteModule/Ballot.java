package Modules.VoteModule;

import Head.BotInstance;

import java.util.HashMap;

public class Ballot {
	
	private final VotingModule module;
	private final int ballotID;
	private String ballotName;
	private String ballotDescription;
	private long[] legalVoters;
	private boolean allowChanges;
	private boolean isClosed = true;
	HashMap<Long, Option> votes = new HashMap<>();
	private final Option[] options;
	private final BallotEventHandler ballotOpenedCode;
	private final BallotEventHandler voteAddedCode;
	private final BallotEventHandler voteRemovedCode;
	private final BallotEventHandler voteChangedCode;
	private final BallotEventHandler ballotEndCode;
	private String ballotMessageID;
	
	public Ballot(VotingModule module, String ballotName, String ballotDescription, int ballotID, long[] legalVoters, boolean allowChanges, Option[] options,
				  BallotEventHandler ballotOpenedCode, BallotEventHandler voteAddedCode, BallotEventHandler voteRemovedCode, BallotEventHandler voteChangedCode, BallotEventHandler ballotEndCode) {
		this.module = module;
		this.ballotName = ballotName;
		this.ballotDescription = ballotDescription;
		this.legalVoters = legalVoters;
		this.ballotID = ballotID;
		this.allowChanges = allowChanges;
		this.options = options;
		this.ballotOpenedCode = ballotOpenedCode;
		this.voteAddedCode = voteAddedCode;
		this.voteRemovedCode = voteRemovedCode;
		this.voteChangedCode = voteChangedCode;
		this.ballotEndCode = ballotEndCode;
	}
	
	public HashMap<Option, Integer> getVotesAccumulated() {
		HashMap<Option, Integer> votesAccumulated = new HashMap<>();
		for(Long l: votes.keySet()) {
			if(votesAccumulated.containsKey(votes.get(l))) {
				votesAccumulated.replace(votes.get(l), votesAccumulated.get(votes.get(l)) + 1);
			} else {
				votesAccumulated.put(votes.get(l), 1);
			}
		}
		return votesAccumulated;
	}
	
	public void castVote(long votee, Option option) throws BallotVoteException {
		if(isClosed) {
			throw new BallotVoteException("This ballot is closed.");
		}
		for(long l: legalVoters) {
			if(l == votee) {
				if(allowChanges) {
					if(votes.containsKey(votee)) {
						votes.replace(votee, option);
						voteChangedCode.run(module.botInstance, this);
						return;
					} else {
						votes.put(votee, option);
						voteAddedCode.run(module.botInstance, this);
						return;
					}
				} else {
					if(votes.containsKey(votee)) {
						throw new BallotVoteException("You already voted on this ballot.");
					} else {
						votes.put(votee, option);
						voteAddedCode.run(module.botInstance, this);
						return;
					}
				}
			}
		}
		throw new BallotVoteException("You are not allowed to vote on this ballot.");
	}
	
	public boolean removeVote(long votee) throws BallotVoteException {
		if(isClosed) {
			throw new BallotVoteException("This ballot is closed.");
		}
		if(allowChanges) {
			votes.remove(votee);
			voteRemovedCode.run(module.botInstance, this);
			return true;
		} else throw new BallotVoteException("You are not allowed to remove your vote on this ballot.");
	}
	
	public void startBallot(long channelID) {
		if(isClosed) {
			ballotOpenedCode.run(module.botInstance, this, String.valueOf(channelID));
			votes = new HashMap<>();
		}
		isClosed = false;
	}
	
	public void endBallot() {
		isClosed = true;
		ballotEndCode.run(module.botInstance, this);
	}
	
	public Option[] getOptions() {
		return options;
	}
	public boolean allowsRecasting() {
		return allowChanges;
	}
	public int getBallotID() {
		return ballotID;
	}
	public String getBallotName() {
		return ballotName;
	}
	public String getBallotDescription() {
		return ballotDescription;
	}
	public long[] getLegalVoters() {
		return legalVoters;
	}
	
	public void setRecastable(boolean recastable) {
		this.allowChanges = recastable;
	}
	public void setBallotName(String ballotName) {
		this.ballotName = ballotName;
	}
	public void setBallotDescription(String ballotDescription) {
		this.ballotDescription = ballotDescription;
	}
	public void setLegalVoters(long[] legalVoters) {
		this.legalVoters = legalVoters;
	}
	
	public static class BallotVoteException extends Exception {
		public BallotVoteException(String message) {
			super(message);
		}
	}
	
	public interface BallotEventHandler {
		
		void run(BotInstance bot, Ballot ballot, String... args);
		
	}
	
}
