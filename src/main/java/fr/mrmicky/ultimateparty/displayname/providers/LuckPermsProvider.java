package fr.mrmicky.ultimateparty.displayname.providers;

import fr.mrmicky.ultimateparty.displayname.PartyNameProvider;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
        User user = api.getUser(player.getUniqueId());

        if (user != null) {
            Contexts contexts = api.getContextManager().getApplicableContexts(player);

            MetaData metaData = user.getCachedData().getMetaData(contexts);
            String prefix = metaData.getPrefix();
            String suffix = metaData.getSuffix();

            return PartyNameProvider.addPrefixSuffix(player, prefix, suffix);
        }

        return player.getName();
    }
}
