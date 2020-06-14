package creativeolympics.playerlist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

abstract class PlayerListManager implements CommandExecutor {

    protected final Plugin plugin;

    private long lastUpdate;
    private long intervalMS;

    // This value is used by the getUpdatedList function, and becomes useless if this method is overridden
    protected String updatedListURL;

    // Constructors ----------------------------------------------------------------------------------------------------

    protected PlayerListManager(Plugin plugin) { this(plugin, -1, ""); }
    protected PlayerListManager(Plugin plugin, long intervalSeconds) { this(plugin, intervalSeconds, ""); }
    protected PlayerListManager(Plugin plugin, String updatedListURL) { this(plugin, -1, updatedListURL); }

    protected PlayerListManager(Plugin plugin, long intervalSeconds, String updatedListURL) {
        this.plugin = plugin;
        this.updatedListURL = updatedListURL;
        setInterval(intervalSeconds);
    }

    // Scheduled updates handling --------------------------------------------------------------------------------------

    /*** Updates the internal task scheduler to do automatic updates */
    public final void tick() {

        // intervalMS < 0 => no auto update
        if(intervalMS < 0) return;

        if(System.currentTimeMillis() - lastUpdate > intervalMS) {
            update();
            lastUpdate = System.currentTimeMillis();
        }
    }

    /*** Sets the interval (seconds) between 2 auto updates. Set it to -1 to disable auto updates
     * Don't forget to call tick() at a regular interval or the auto updates won't work */
    public final void setInterval(long intervalSeconds) {
        intervalMS = intervalSeconds*1000;
        lastUpdate = System.currentTimeMillis();
    }

    // List updating ---------------------------------------------------------------------------------------------------

    public void update() {

        String[] newList = getUpdatedList();
        if(newList == null) return;

        String[] oldList = getOldList();
        if(oldList == null) return;

        for(String newListPlayer : newList) {
            if(Arrays.stream(oldList).noneMatch(oldListPlayer -> oldListPlayer.contentEquals(newListPlayer)))
                addPlayer(newListPlayer);
        }
    }

    /*** Adds a player to the list */
    protected abstract void addPlayer(String playerName);

    // Old list --------------------------------------------------------------------------------------------------------

    /*** Gets the old player list to know what players must be updated (LOWERCASE)
     * You can return null if the result cannot be retrieved. But you should log something */
    protected abstract String[] getOldList();

    // New list to be applied ------------------------------------------------------------------------------------------

    /*** Gets the new player list. This method can be overridden for custom behaviour
     * The names should be LOWERCASE */
    protected String[] getUpdatedList() {

        StringBuilder response = new StringBuilder();
        try {
            //https://www.journaldev.com/7148/java-httpurlconnection-example-java-http-request-get-post
            URL obj = new URL(updatedListURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String buff;
                while ((buff = in.readLine()) != null)
                    response.append(buff);
                in.close();
            }
            else throw new Exception();
        } catch(Exception e) {
            plugin.getLogger().warning("Couldn't access resource at location: " + updatedListURL);
            return null;
        }

        return decodeUpdatedList(response.toString());
    }

    /*** Decodes the new player list. This method can be overridden for custom
     * behaviour (useful if you don't change getUpdatedList) */
    protected String[] decodeUpdatedList(String rawResponse) {
        return Arrays.stream(rawResponse.split("<br>"))
                .filter(s -> s.length() > 0)
                .map(String::toLowerCase)
                .toArray(String[]::new);
    }

    // Commands ---------------------------------------------------------------------------------------------------------

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // Updates the ban list
        update();
        return false;
    }
}
