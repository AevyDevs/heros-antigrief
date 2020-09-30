package net.herospvp.antigrief.database;

import lombok.Getter;
import net.herospvp.antigrief.Conf;
import net.herospvp.antigrief.Main;
import net.herospvp.antigrief.utils.Utils;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Store {

    @Getter
    private static final Map<String, ArrayList<String>> storedPlayers = new HashMap<>();
    // 0 = rank; 1 = pin; 2 = logged_ip;

    public static void setEveryThing(String playerName, String rank, String pin, String ip) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(rank);
        arrayList.add(pin);
        arrayList.add(ip);
        storedPlayers.put(playerName, arrayList);
    }

    public static void setPlayerIP(String playerName, String ip) {
        storedPlayers.get(playerName).set(2, ip);
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> Hikari.executeUpdate(playerName,
                "UPDATE " + Conf.databaseTable + " SET ip = " + Utils.format(ip) + " WHERE username = "));
    }

    public static boolean contains(String playerName) {
        return storedPlayers.containsKey(playerName);
    }

    public static boolean ipAreEquals(String playerName, String hostname) {
        if (contains(playerName)) {
            return storedPlayers.get(playerName).get(2).equals(hostname);
        } else {
            return false;
        }
    }

}