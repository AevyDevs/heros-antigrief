package net.herospvp.antigrief.commands;

import net.herospvp.antigrief.Main;
import net.herospvp.antigrief.Utils;
import net.herospvp.antigrief.database.Store;
import net.herospvp.heroscore.utils.TelegramRequest;
import net.herospvp.heroscore.utils.builders.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Pin implements CommandExecutor {

    private static final Message.Builder builder = Main.getBuilder();

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (builder.checkPermission(commandSender, "pin")) {
                Player player = (Player) commandSender;

                if (strings.length == 1) {
                    String playerName = player.getName();
                    String hostname = player.getAddress().getHostName();
                    if (Store.getStoredPlayers().containsKey(playerName)) {

                        ArrayList<String> arrayList = Store.getStoredPlayers().get(playerName);
                        if (arrayList.get(1).equals(strings[0])) {

                            if (!arrayList.get(2).equals(player.getAddress().getHostName())) {
                                Store.setPlayerIP(playerName, player.getAddress().getHostName());
                                Utils.giveOP(player);
                                Utils.sendMessage(player, Message.Builder.Result.OK, "antigrief-pin-1");
                            } else {
                                Utils.sendMessage(player, Message.Builder.Result.ERR, "antigrief-pin-5");
                            }

                        } else {
                            Utils.sendMessage(player, Message.Builder.Result.ERR, "antigrief-pin-2");
                            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () ->
                                    new TelegramRequest("-1001293327695", "[ANTIGRIEF] [" + Bukkit.getServerName() + "]%0A"
                                            + playerName + " ha sbagliato il PIN. %0AIP: " + hostname,
                                            "1316821738:AAGtdENJQ26FfK8S9pz4wNXGpwzAI9wKDaU", null).sendMessage());
                        }

                    } else {
                        Utils.sendMessage(player, Message.Builder.Result.ERR, "antigrief-pin-4");
                        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () ->
                                new TelegramRequest("-1001293327695", "[ANTIGRIEF] [" + Bukkit.getServerName() + "]%0A"
                                        + playerName + " ha provato a pinnarsi ma non e' presente nella lista degli admin. %0AIP: "
                                        + hostname,
                                        "1316821738:AAGtdENJQ26FfK8S9pz4wNXGpwzAI9wKDaU", null).sendMessage());
                    }

                } else {
                    Utils.sendMessage(player, Message.Builder.Result.ERR, "antigrief-pin-3");
                    builder.printHelpMessage(commandSender);
                }
            }
        } else {
            builder.denyConsoleCommand(commandSender);
        }
        return false;
    }

}
