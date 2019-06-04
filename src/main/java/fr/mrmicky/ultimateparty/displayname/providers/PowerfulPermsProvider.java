package fr.mrmicky.ultimateparty.displayname.providers;

import com.github.gustav9797.PowerfulPermsAPI.PermissionManager;
import com.github.gustav9797.PowerfulPermsAPI.PowerfulPermsPlugin;
import com.google.common.util.concurrent.ListenableFuture;
import fr.mrmicky.ultimateparty.displayname.PartyNameProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.ExecutionException;

public class PowerfulPermsProvider implements PartyNameProvider {

    private final PermissionManager powerfulPermsPlugin;

    public PowerfulPermsProvider() {
        Plugin powerfulPerms = ProxyServer.getInstance().getPluginManager().getPlugin("PowerfulPerms");

        powerfulPermsPlugin = ((PowerfulPermsPlugin) powerfulPerms).getPermissionManager();
    }

    @Override
    public String getName() {
        return "PowerfulPerms";
    }

    @Override
    public String getDisplayName(ProxiedPlayer player) {
        ListenableFuture<String> prefixFuture = powerfulPermsPlugin.getPlayerPrefix(player.getUniqueId());
        ListenableFuture<String> suffixFuture = powerfulPermsPlugin.getPlayerSuffix(player.getUniqueId());
        try {
            String prefix = prefixFuture.get();
            String suffix = suffixFuture.get();
            return (prefix != null ? prefix : "") + player.getName() + (suffix != null ? suffix : "");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
