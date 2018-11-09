package fr.mrmicky.ultimateparty.displayname.providers;

import fr.mrmicky.ultimateparty.displayname.PartyNameProvider;
import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.PermissionsManager;
import net.alpenblock.bungeeperms.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePermsProvider implements PartyNameProvider {

	private PermissionsManager permissionManager;

	public BungeePermsProvider() {
		permissionManager = BungeePerms.getInstance().getPermissionsManager();
	}
	
	@Override
	public String getName() {
		return "BungeePerms";
	}

	@Override
	public String getDisplayName(ProxiedPlayer p) {
		User user = permissionManager.getUser(p.getUniqueId());
		String prefix = user.getPrefix();
		String suffix = user.getSuffix();

		return (prefix != null ? prefix : "") + p.getName() + (suffix != null ? suffix : "");
	}
}
