package tk.shanebee.nbt;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.nbt.config.BoundConfig;
import tk.shanebee.nbt.elements.objects.Bound;
import tk.shanebee.nbt.listener.BoundBorderListener;
import tk.shanebee.nbt.nms.NBTApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"deprecation", "SpellCheckingInspection"})
public class NBeeT extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(Bound.class, "Bound");
    }

    private static NBeeT instance;
    private static NBTApi nbtApi;
    private PluginDescriptionFile desc = getDescription();
    private List<tk.shanebee.nbt.elements.objects.OldBound> bounds = new ArrayList<>();
    private BoundConfig boundConfig;

    @Override
    public void onEnable() {
        instance = this;

        if ((Bukkit.getPluginManager().getPlugin("Skript") != null) && (Skript.isAcceptRegistrations())) {
            long start = System.currentTimeMillis();
            this.boundConfig = new BoundConfig(this);
            int boundSize = boundConfig.getBounds().size();
            if (boundSize > 0) {
                sendColConsole("Loaded &b" + boundSize + "&7 bounds in " + (System.currentTimeMillis() - start) + " milliseconds.");
            }

            String nms = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            SkriptAddon addon = Skript.registerAddon(this);
            try {
                nbtApi = (NBTApi) Class.forName(NBeeT.class.getPackage().getName() + ".nms.NBT_" + nms).newInstance();
                try {
                    addon.loadClasses("tk.shanebee.nbt", "elementsNBT");
                } catch (IOException e) {
                    e.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
                sendColConsole("&bCompatible NMS version: " + nms);
                sendColConsole("&7  - All NBT related syntaxes loaded.");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                sendColConsole("&cIncompatible NMS version found: &7" + nms);
                sendColConsole("&7  - NBT syntaxes not supported on your server version.");
                sendColConsole("&7  - The plugin will still work for all non NBT related syntaxes.");
            }
            try {
                addon.loadClasses("tk.shanebee.nbt", "elements");
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            sendColConsole("&aSuccessfully enabled v" + desc.getVersion());
            if (desc.getVersion().contains("Beta")) {
                sendColConsole("&eThis is a BETA build, things may not work as expected, please report any bugs on GitHub");
                sendColConsole("&ehttps://github.com/ShaneBeee/Sk-NBeeT/issues");
            }
        } else {
            sendColConsole("&cDependency Skript was not found, plugin disabling");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        Bukkit.getPluginManager().registerEvents(new BoundBorderListener(this), this);
    }

    @Override
    public void onDisable() {
    }

    public static NBeeT getInstance() {
        return instance;
    }

    public static NBTApi getNBTApi() {
        return nbtApi;
    }

    public List<tk.shanebee.nbt.elements.objects.OldBound> getBounds() {
        return this.bounds;
    }

    public BoundConfig getBoundConfig() {
        return boundConfig;
    }

    public void sendColConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7[&bSk-NBeeT&7] " + message));
    }

}