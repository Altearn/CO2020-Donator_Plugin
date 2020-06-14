package creativeolympics;

import creativeolympics.playerlist.BanListManager;
import creativeolympics.playerlist.WhiteListManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        WhiteListManager whitelist = new WhiteListManager(this);
        BanListManager banlist = new BanListManager(this);

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
