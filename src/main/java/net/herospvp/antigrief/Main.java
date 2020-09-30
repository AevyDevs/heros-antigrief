package net.herospvp.antigrief;

import lombok.Getter;
import net.herospvp.antigrief.commands.AntiGrief;
import net.herospvp.antigrief.commands.Pin;
import net.herospvp.antigrief.database.Hikari;
import net.herospvp.antigrief.monitor.Events;
import net.herospvp.heroscore.utils.builders.Message;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    @Getter
    private static Message.Builder builder;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        Message.addPlugin("ANTIGRIEF", Arrays.asList(
                "&c/pin <codice>&7\u2502 &fComando per l'autenticazione.",
                "&c/ag <player>&7\u2502 &fMostra le informazioni di <player>."));

        builder = Message.like("ANTIGRIEF");

        Bukkit.getScheduler().runTaskAsynchronously(this, Hikari::initializeDatabase);

        getCommand("pin").setExecutor(new Pin());
        getCommand("ag").setExecutor(new AntiGrief());

        getServer().getPluginManager().registerEvents(new Events(), this);

    }

    @Override
    public void onDisable() {
        Message.removePlugin("ANTIGRIEF");
    }

}
