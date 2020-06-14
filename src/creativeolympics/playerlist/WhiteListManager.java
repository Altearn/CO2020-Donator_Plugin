package creativeolympics.playerlist;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class WhiteListManager extends PlayerListManager {

    public static final String URL = "https://files.gunivers.net/creative-olympics-whitelist.html";

    // Constructors ----------------------------------------------------------------------------------------------------

    public WhiteListManager(Plugin plugin) { this(plugin, 60); }
    public WhiteListManager(Plugin plugin, long intervalSeconds) { super(plugin, intervalSeconds, URL); }

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
