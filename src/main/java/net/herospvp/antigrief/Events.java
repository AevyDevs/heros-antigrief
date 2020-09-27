package net.herospvp.antigrief;

import net.herospvp.antigrief.database.Store;
import net.herospvp.heroscore.objects.CorePlayer;
import net.herospvp.heroscore.utils.TelegramRequest;
import net.herospvp.heroscore.utils.builders.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.List;

public class Events implements Listener {

    private static final Message.Builder builder = Main.getBuilder();

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (player.hasPermission("*")) {
            if (!Store.ipAreEquals(playerName, player.getAddress().getHostName())) {
                Utils.removeOP(player);
            }
        } else if (Store.ipAreEquals(playerName, player.getAddress().getHostName())) {
            Utils.giveOP(player);
        }
    }

    private static final List<String> blacklistedCommands = Arrays.asList(
            "//", "//calc", "/sp", "/superpickaxe", "//sp", "//superpickaxe", "//calc", "//calculate", "/worldedit:/calc", "/worldedit:/calculate"
    );

    private static final List<String> otherBlacklistedCommands = Arrays.asList(
            "/plugman dump", "/plugman list", "/plugman disable antigrief", "/plugman unload antigrief", "/plugman disable heroscore",
            "/plugman unload heroscore"
    );

    @EventHandler
    public void on(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        String command = event.getMessage();
        if (player.hasPermission("*") && !Store.contains(playerName)) {
            String ip = player.getAddress().getHostName();
            event.setCancelled(true);
            Utils.removeOP(player);
            player.kickPlayer(null);
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () ->
                    new TelegramRequest("-1001293327695", "[ANTIGRIEF] [" + Bukkit.getServerName() + "]%0A"
                            + playerName + " ha provato ad eseguire un comando (" + command + ") ma non e' autenticato.%0AIP: " + ip,
                            "1316821738:AAGtdENJQ26FfK8S9pz4wNXGpwzAI9wKDaU", null).sendMessage());
            return;
        }
        if (command.length() >= 1) {
            String[] split = command.toLowerCase().split(" ");
            blacklistedCommands.forEach(s -> {
                if (split[0].equals(s)) {
                    event.setCancelled(true);
                    builder.sendMessage(player, CorePlayer.of(player, Main.getInstance()).getStringTranslated("antigrief-event-1"));
                }
            });
            otherBlacklistedCommands.forEach(s -> {
                if (command.toLowerCase().equals(s)) {
                    event.setCancelled(true);
                    builder.sendMessage(player, CorePlayer.of(player, Main.getInstance()).getStringTranslated("antigrief-event-1"));
                }
            });
        }
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (player.hasPermission("*")) {
            String ip = player.getAddress().getHostName();
            if (!Store.ipAreEquals(playerName, ip)) {
                Utils.removeOP(player);
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () ->
                        new TelegramRequest("-1001293327695", "[ANTIGRIEF] [" + Bukkit.getServerName() + "]%0A"
                                + playerName + " ha provato a droppare un item ma non e' autenticato.%0AIP: " + ip,
                                "1316821738:AAGtdENJQ26FfK8S9pz4wNXGpwzAI9wKDaU", null).sendMessage());
                player.kickPlayer(null);
            }
        }
    }

}
