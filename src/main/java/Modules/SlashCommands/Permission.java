package Modules.SlashCommands;

public enum Permission {
	
	CLEAR("CLEAR"), SHUTDOWN("SHUTDOWN"), PLAY("PLAY"), ADMIN("ADMIN", PLAY, CLEAR), GOD("GOD", ADMIN, SHUTDOWN);
	public static final Permission[] allPermissions = new Permission[] {CLEAR, SHUTDOWN, PLAY, ADMIN, GOD};
	
	final String sign;
	final Permission[] perms;
	
	Permission(String string, Permission... permissions) {
		sign = string;
		perms = permissions;
	}
	
	public static Permission getPermission(String permission) {
		for(Permission p: allPermissions)
			if(p.sign.equalsIgnoreCase(permission))
				return p;
		return null;
	}
	
	public boolean includesPermission(String permission) {
		return includesPermission(getPermission(permission));
	}
	
	public boolean includesPermission(Permission permission) {
		if(permission == this) return true;
		for(Permission p: this.perms) {
			if(p.includesPermission(permission)) return true;
		}
		return false;
	}
	
	public static class NoPermissionException extends CommandException {
		NoPermissionException() {
			super();
		}
		NoPermissionException(String message) {
			super(message);
		}
		NoPermissionException(String message, Throwable reason) {
			super(message, reason);
		}
		NoPermissionException(Throwable reason) {
			super(reason);
		}
	}
}
