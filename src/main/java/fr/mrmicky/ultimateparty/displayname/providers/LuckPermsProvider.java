package fr.mrmicky.ultimateparty.displayname.providers;

import fr.mrmicky.ultimateparty.displayname.PartyNameProvider;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Optional;

public class LuckPermsProvider implements PartyNameProvider {

    private final LuckPermsApi api;

    public LuckPermsProvider() {
        api = LuckPerms.getApi();
    }

    @Override
    public String getName() {
        return "LuckPerms";
    }

    @Override
    public String getDisplayName(ProxiedPlayer player) {
        api.getUser(player.getName());
        Optional<User> user = api.getUserSafe(player.getUniqueId());
        if (user.isPresent()) {
            Optional<Contexts> contexts = api.getContextForUser(user.get());
            if (contexts.isPresent()) {
                MetaData metaData = user.get().getCachedData().getMetaData(contexts.get());
                String prefix = metaData.getPrefix();
                String suffix = metaData.getSuffix();

                return (prefix != null ? prefix : "") + player.getName() + (suffix != null ? suffix : "");
            }
        }
        return player.getName();
    }
}
