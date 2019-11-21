package tk.shanebee.nbt.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tk.shanebee.nbt.NBeeT;
import tk.shanebee.nbt.elements.objects.Bound;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BoundConfig {

    private NBeeT plugin;
    private File boundFile;
    private FileConfiguration boundConfig;
    private Map<String, Bound> boundsMap = new HashMap<>();

    public BoundConfig(NBeeT plugin) {
        this.plugin = plugin;
        loadBoundConfig();
    }

    private void loadBoundConfig() {
        if (boundFile == null) {
            boundFile = new File(plugin.getDataFolder(), "bounds.yml");
        }
        if (!boundFile.exists()) {
            plugin.saveResource("bounds.yml", false);
        }
        boundConfig = YamlConfiguration.loadConfiguration(boundFile);
        loadBounds();
    }

    private void loadBounds() {
        ConfigurationSection section = boundConfig.getConfigurationSection("bounds");
        if (section == null) return;
        for (String string : section.getKeys(true)) {
            if (section.get(string) instanceof Bound) {
                boundsMap.put(string, ((Bound) section.get(string)));
            }
        }
    }

    public void saveBound(Bound bound) {
        boundConfig.set("bounds." + bound.getId(), bound);
        boundsMap.put(bound.getId(), bound);
        saveConfig();
    }

    public void removeBound(Bound bound) {
        boundsMap.remove(bound.getId());
        boundConfig.set("bounds." + bound.getId(), null);
        saveConfig();
    }

    public Bound getBoundFromID(String id) {
        if (boundsMap.containsKey(id))
            return boundsMap.get(id);
        return null;
    }

    public void saveAllBounds() {
        for (Bound bound : boundsMap.values()) {
            saveBound(bound);
        }
    }

    private void saveConfig() {
        try {
            boundConfig.save(boundFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<Bound> getBounds() {
        return boundsMap.values();
    }

}
