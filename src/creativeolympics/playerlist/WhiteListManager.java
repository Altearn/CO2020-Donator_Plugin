package creativeolympics.playerlist;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class WhiteListManager extends PlayerListManager {

    // Config values
    public static final String WHITELIST_UPDATE_INTERVAL = "whitelist-update-interval";
    public static final String WHITELIST_URL = "whitelist-url";

    // Constructors ----------------------------------------------------------------------------------------------------

    public WhiteListManager(Plugin plugin, FileConfiguration config) {
        super(plugin, config.getLong(WHITELIST_UPDATE_INTERVAL), config.getString(WHITELIST_URL));
    }

    // Overridden methods ----------------------------------------------------------------------------------------------

    @Override
    protected void addPlayer(String playerName) {
        plugin.getLogger().info("Adding " + playerName + " to the white list");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "whitelist add " + playerName);
    }

    @Override
    protected String[] getOldList() {
        return Bukkit.getWhitelistedPlayers().stream()
                .filter(player -> player.getName() != null)
                .map(player -> player.getName().toLowerCase())
                .toArray(String[]::new);
    }
}
