package creativeolympics.playerlist;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class BanListManager extends PlayerListManager {

    // Config values
    public static final String BLACKLIST_UPDATE_INTERVAL = "blacklist-update-interval";
    public static final String BLACKLIST_URL = "blacklist-url";

    // Constructors ----------------------------------------------------------------------------------------------------

    public BanListManager(Plugin plugin, FileConfiguration config) {
        super(plugin, config.getLong(BLACKLIST_UPDATE_INTERVAL), config.getString(BLACKLIST_URL));
    }

    // Overridden methods ----------------------------------------------------------------------------------------------

    @Override
    protected void addPlayer(String playerName) {
        plugin.getLogger().info("Banning " + playerName);
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "ban " + playerName);
    }

    @Override
    protected String[] getOldList() {
        return Bukkit.getBannedPlayers().stream()
                .filter(player -> player.getName() != null)
                .map(player -> player.getName().toLowerCase())
                .toArray(String[]::new);
    }
}
