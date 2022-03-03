package dev.martinl.bsbrewritten.util;

import dev.martinl.bsbrewritten.BSBRewritten;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Getter
public class UpdateChecker {
    private final BSBRewritten instance;
    private final int project;
    private URL checkURL;
    private URL changelogURL;
    private boolean newerVersionAvailable = false;
    private String newVersion;
    private ArrayList<String> latestChangelog = new ArrayList<>();


    public UpdateChecker(BSBRewritten instance, int projectID) {
        this.instance = instance;
        this.newVersion = instance.getDescription().getVersion();
        this.project = projectID;
        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectID);
            this.changelogURL = new URL("https://raw.githubusercontent.com/lMartin3/BetterShulkerBoxes/master/CHANGELOG.txt");
        } catch (MalformedURLException localMalformedURLException) {
            Bukkit.getServer().getConsoleSender().sendMessage("Error: MalformedURLException, please send this to the developer");
        }

    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + this.project;
    }

    public String getActualVersion() {
        return this.instance.getDescription().getVersion();
    }

    //GET request to a spigot api
    public boolean checkForUpdates() {
        try {
            URLConnection con = this.checkURL.openConnection();
            this.newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            boolean result = !this.instance.getDescription().getVersion().equals(this.newVersion);
            newerVersionAvailable = result;
            getChangelog();
            return result;
        } catch (IOException ioex) {
            instance.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error! BSB could not check for updates:");
            instance.getServer().getConsoleSender().sendMessage(ioex.toString());
            return false;
        }
    }

    //GET request to a (raw) file on the github repo
    public ArrayList<String> getChangelog() {
        ArrayList<String> lines = new ArrayList<>();
        try {
            URLConnection con = this.changelogURL.openConnection();
            InputStreamReader inSR = new InputStreamReader(con.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inSR);
            String inputLine;
            //StringBuffer content = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null) {
                lines.add(inputLine);
            }
            bufferedReader.close();
            latestChangelog = lines;
            return lines;
        } catch (IOException ioex) {
            instance.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error! BSB could not retrieve the changelog:");
            instance.getServer().getConsoleSender().sendMessage(ioex.toString());
            latestChangelog = lines;
            return lines;
        }
    }

    public List<String> getUpdateMessages() {
        ArrayList<String> changes =  new ArrayList<>();
        changes.add(ChatColor.YELLOW + "[BSB] A new version is available! You are currently running " +
                ChatColor.GOLD + instance.getDescription().getVersion() + ChatColor.YELLOW + ", the newest version is " + ChatColor.GREEN + newVersion);
        changes.add(ChatColor.YELLOW + "[BSB] New version changes: ");
        latestChangelog.forEach(ch->changes.add(ChatColor.GRAY + "-> " + ch));
        return changes;
    }

}