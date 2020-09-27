package net.herospvp.antigrief;

import net.herospvp.antigrief.database.Store;
import net.herospvp.heroscore.objects.CorePlayer;
import net.herospvp.heroscore.utils.builders.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Utils {

    private static final Message.Builder builder = Main.getBuilder();

    public static String checkIfIsPlayer(CommandSender commandSender, String translatedString, String elseReturn) {
        if (commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();
            return CorePlayer.of(player, Main.getInstance()).getStringTranslated(translatedString);
        } else {
            return elseReturn;
        }
    }

    public static List<String> checkIfIsPlayer(CommandSender commandSender, String translatedStringList, List<String> elseReturn) {
        if (commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();
            return CorePlayer.of(player, Main.getInstance()).getListTranslated(translatedStringList);
        } else {
            return elseReturn;
        }
    }

    public static void sendMessage(Player player, Message.Builder.Result result, String string) {
        builder.sendMessage(player,
                CorePlayer.of(player.getName(), Main.getInstance()).getStringTranslated(string), result);
    }

    public static void runConsoleCommand(String command) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    public static void giveOP(Player player) {
        String playerName = player.getName();
        Utils.runConsoleCommand("lp user " + playerName + " permission set *");
        Utils.runConsoleCommand("lp user " + playerName + " parent set " + Store.getStoredPlayers().get(playerName).get(0));
    }

    public static void removeOP(Player player) {
        String playerName = player.getName();
        Utils.runConsoleCommand("lp user " + playerName + " parent set default");
        Utils.runConsoleCommand("lp user " + playerName + " permission unset *");
        player.setOp(false);
    }

    public static String format(String string) {
        return "\"" + string + "\"";
    }

}
