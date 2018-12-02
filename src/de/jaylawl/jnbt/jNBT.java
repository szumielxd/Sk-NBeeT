package de.jaylawl.jnbt;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;

public class jNBT extends JavaPlugin {

    jNBT instance;
    SkriptAddon addon;

    @Override
    public void onEnable() {
        if ((Bukkit.getPluginManager().getPlugin("Skript") != null) && (Skript.isAcceptRegistrations())) {
            String nms = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            if (nms.equals("v1_13_R2")) {
                getLogger().info("Compatible NMS version: " + nms);
                instance = this;
                addon = Skript.registerAddon(this);
                try {
                    addon.loadClasses("de.jaylawl.jnbt", "elements");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getLogger().info("Successfully enabled");
            }
            else {
                getLogger().info("Incompatible NMS version: " + nms);
                getLogger().info("Please use Spigot 1.13.2");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
        else {
            getLogger().info("Dependency Skript was not found");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {}

}