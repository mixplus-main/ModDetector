package com.mixplus.plugin.modDetector.config;

import com.mixplus.plugin.modDetector.ModDetector;
import com.mixplus.plugin.modDetector.action.ActionMode;
import com.mixplus.plugin.modDetector.mod.ModInfo;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;


public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    private final Logger logger;


    private final Map<String, ModInfo> mods = new LinkedHashMap<>();
    private long checkRate;
    private long checkDelay;
    private long warmup;
    private boolean opBypass;

    public ConfigManager() {
        this.plugin = ModDetector.getPlugin();
        this.logger = plugin.getLogger();
        config = plugin.getConfig();


    }

    public void load() {
        plugin.reloadConfig();
        config = plugin.getConfig();

        ConfigurationSection section = config.getConfigurationSection("mods");
        if (section == null) {
            logger.warning("mods key not found.");
            return;
        }

        mods.clear();
        for (String id : section.getKeys(false)) {
            String name = section.getString(id + ".name");
            String key = section.getString(id + ".key");
            String action = section.getString(id + ".action");

            ActionMode mode = ActionMode.NONE;
            String modeString = section.getString(id + ".mode");
            if (modeString != null) {
                try {
                    mode = ActionMode.valueOf(modeString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    logger.warning("Unknown action mode: " + modeString);
                }
            }

            ModInfo info = new ModInfo(id, name, key, mode, action);

            logger.info("put to " + name);
            mods.put(id, info);
        }

        this.checkRate = config.getLong("check-rate", 3L);
        this.checkDelay = config.getLong("check-delay", 2L);
        this.opBypass = config.getBoolean("opBypass", true);
        this.warmup = config.getLong("warmup", 3);
    }

    public void reload() {
        load();
    }

    public Map<String, ModInfo> getMods() {
        return new LinkedHashMap<>(mods);
    }

    public long getCheckRate() {
        return this.checkRate;
    }

    public long getCheckDelay() {
        return this.checkDelay;
    }

    public long getWarmup() {
        return this.warmup;
    }

    public boolean isOpBypass() {
        return this.opBypass;
    }

    public void setOpBypass(boolean value) {
        this.opBypass = value;
        this.config.set("opBypass", value);
        this.plugin.saveConfig();
    }
}
