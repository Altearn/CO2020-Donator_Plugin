package creativeolympics;

import creativeolympics.playerlist.BanListManager;
import creativeolympics.playerlist.WhiteListManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        saveDefaultConfig();
        FileConfiguration config = getConfig();

        WhiteListManager whitelist = new WhiteListManager(this, config);
        BanListManager banlist = new BanListManager(this, config);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            whitelist.tick();
            banlist.tick();
        }, 0L, 1); //0 tick initial delay, 1 tick between repeats

        getCommand("updatewhitelist").setExecutor(whitelist);
        getCommand("updateblacklist").setExecutor(banlist);
    }

    @Override
    public void onDisable() {
    }
}
