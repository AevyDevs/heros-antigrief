package net.herospvp.antigrief.commands;

import net.herospvp.antigrief.Main;
import net.herospvp.antigrief.utils.Utils;
import net.herospvp.antigrief.database.Hikari;
import net.herospvp.heroscore.utils.builders.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AntiGrief implements CommandExecutor {

    private static final Message.Builder builder = Main.getBuilder();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (builder.checkPermission(commandSender, "antigrief")) {
            if (strings.length == 1) {
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> Hikari.getInfo(commandSender, strings[0]));
            } else {
                String result = Utils.checkIfIsPlayer(commandSender, "antigrief-pin-3", "Devi specificare piu' argomenti, ecco la lista dei comandi:");

                builder.sendMessage(commandSender, result, Message.Builder.Result.ERR);
                builder.printHelpMessage(commandSender);
            }
        }
        return false;
    }

}
