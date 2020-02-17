package fr.mrmicky.ultimateparty.displayname.providers;

import fr.mrmicky.ultimateparty.displayname.PartyNameProvider;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LuckPermsProvider implements PartyNameProvider {

    private final LuckPerms luckPerms;

    public LuckPermsProvider() {
        luckPerms = net.luckperms.api.LuckPermsProvider.get();
    }

    @Override
    public String getName() {
        return "LuckPerms";
    }

    @Override
    public String getDisplayName(ProxiedPlayer player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        if (user == null) {
            return player.getName();
        }

        QueryOptions queryOptions = luckPerms.getContextManager().getQueryOptions(player);
        CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);

        String prefix = metaData.getPrefix();
        String suffix = metaData.getSuffix();

        return PartyNameProvider.addPrefixSuffix(player, prefix, suffix);
    }
}
