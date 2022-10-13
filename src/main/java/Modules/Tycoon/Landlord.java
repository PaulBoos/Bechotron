package Modules.Tycoon;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.requests.restaction.CacheRestAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Landlord {
	
	private final long id;
	private List<Facility> ownedFacilities;
	private List<User> virtualUsers = new ArrayList<>();
	
	public Landlord(long id) {
		this.id = id;
		this.ownedFacilities = new ArrayList<>();
		virtualUsers.add(new User() {
					@NotNull
					@Override
					public String getName() {
						return "Testuser";
					}
					
					@NotNull
					@Override
					public String getDiscriminator() {
						return "0001";
					}
					
					@Nullable
					@Override
					public String getAvatarId() {
						return null;
					}
					
					@NotNull
					@Override
					public String getDefaultAvatarId() {
						return null;
					}
					
					@NotNull
					@Override
					public CacheRestAction<Profile> retrieveProfile() {
						return null;
					}
					
					@NotNull
					@Override
					public String getAsTag() {
						return "Testuser#0001";
					}
					
					@Override
					public boolean hasPrivateChannel() {
						return false;
					}
					
					@NotNull
					@Override
					public CacheRestAction<PrivateChannel> openPrivateChannel() {
						return null;
					}
					
					@NotNull
					@Override
					public List<Guild> getMutualGuilds() {
						return null;
					}
					
					@Override
					public boolean isBot() {
						return false;
					}
					
					@Override
					public boolean isSystem() {
						return false;
					}
					
					@NotNull
					@Override
					public JDA getJDA() {
						return null;
					}
					
					@NotNull
					@Override
					public EnumSet<UserFlag> getFlags() {
						return null;
					}
					
					@Override
					public int getFlagsRaw() {
						return 0;
					}
					
					@NotNull
					@Override
					public String getAsMention() {
						return null;
					}
					
					@Override
					public long getIdLong() {
						return 0;
					}
				});
		virtualUsers.add(new User() {
			@NotNull
			@Override
			public String getName() {
				return "Testuser2";
			}
			
			@NotNull
			@Override
			public String getDiscriminator() {
				return "0002";
			}
			
			@Nullable
			@Override
			public String getAvatarId() {
				return null;
			}
			
			@NotNull
			@Override
			public String getDefaultAvatarId() {
				return null;
			}
			
			@NotNull
			@Override
			public CacheRestAction<Profile> retrieveProfile() {
				return null;
			}
			
			@NotNull
			@Override
			public String getAsTag() {
				return "Testuser2#0002";
			}
			
			@Override
			public boolean hasPrivateChannel() {
				return false;
			}
			
			@NotNull
			@Override
			public CacheRestAction<PrivateChannel> openPrivateChannel() {
				return null;
			}
			
			@NotNull
			@Override
			public List<Guild> getMutualGuilds() {
				return null;
			}
			
			@Override
			public boolean isBot() {
				return false;
			}
			
			@Override
			public boolean isSystem() {
				return false;
			}
			
			@NotNull
			@Override
			public JDA getJDA() {
				return null;
			}
			
			@NotNull
			@Override
			public EnumSet<UserFlag> getFlags() {
				return null;
			}
			
			@Override
			public int getFlagsRaw() {
				return 0;
			}
			
			@NotNull
			@Override
			public String getAsMention() {
				return null;
			}
			
			@Override
			public long getIdLong() {
				return 0;
			}
		});
		virtualUsers.add(new User() {
			@NotNull
			@Override
			public String getName() {
				return "Testuser3";
			}
			
			@NotNull
			@Override
			public String getDiscriminator() {
				return "0003";
			}
			
			@Nullable
			@Override
			public String getAvatarId() {
				return null;
			}
			
			@NotNull
			@Override
			public String getDefaultAvatarId() {
				return null;
			}
			
			@NotNull
			@Override
			public CacheRestAction<Profile> retrieveProfile() {
				return null;
			}
			
			@NotNull
			@Override
			public String getAsTag() {
				return "Testuser3#0003";
			}
			
			@Override
			public boolean hasPrivateChannel() {
				return false;
			}
			
			@NotNull
			@Override
			public CacheRestAction<PrivateChannel> openPrivateChannel() {
				return null;
			}
			
			@NotNull
			@Override
			public List<Guild> getMutualGuilds() {
				return null;
			}
			
			@Override
			public boolean isBot() {
				return false;
			}
			
			@Override
			public boolean isSystem() {
				return false;
			}
			
			@NotNull
			@Override
			public JDA getJDA() {
				return null;
			}
			
			@NotNull
			@Override
			public EnumSet<UserFlag> getFlags() {
				return null;
			}
			
			@Override
			public int getFlagsRaw() {
				return 0;
			}
			
			@NotNull
			@Override
			public String getAsMention() {
				return null;
			}
			
			@Override
			public long getIdLong() {
				return 0;
			}
		});
	}
	
	protected void registerFacility(Facility facility) {
		ownedFacilities.add(facility);
	}
	
	protected void deregisterFacility(Facility facility) {
		ownedFacilities.remove(facility);
	}
	
	protected void deregisterFacility(int facility) {
		ownedFacilities.removeIf(f -> f.getID() == facility);
	}
	
	public Facility[] getOwnedFacilities() {
		return ownedFacilities.toArray(new Facility[0]);
	}
	
	public User getUser() {
		return virtualUsers.get(0);
	}
	
}
