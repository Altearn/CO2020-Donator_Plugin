package creativeolympics.playerlist;

import org.bukkit.plugin.Plugin;

public class BanListManager extends PlayerListManager {

    public static final String URL = "";

    // Constructors ----------------------------------------------------------------------------------------------------

    public BanListManager(Plugin plugin) { this(plugin, -1); }
    public BanListManager(Plugin plugin, long intervalSeconds) {
        super(plugin, intervalSeconds, URL);
    }

    // Overridden methods ----------------------------------------------------------------------------------------------

    @Override
    protected void addPlayer(String playerName) {

    }

    @Override
    protected String[] getOldList() {

        return null;
    }
}
