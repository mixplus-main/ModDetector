package com.mixplus.plugin.modDetector;



import com.mixplus.plugin.modDetector.check.CheckManager;
import com.mixplus.plugin.modDetector.commands.ModDetectorCommand;
import com.mixplus.plugin.modDetector.config.ConfigManager;
import com.mixplus.plugin.modDetector.check.impl.CheckMod;
import com.mixplus.plugin.modDetector.commands.ModCheckCommand;
import com.mixplus.plugin.modDetector.listener.PacketListener;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.retrooper.packetevents.PacketEvents;

import java.util.Objects;
import java.util.logging.Logger;

public final class ModDetector extends JavaPlugin {
    private static JavaPlugin plugin;
    private static ModDetector instance;
    private Logger logger;

    private ConfigManager configManager;

    private CheckManager checkManager;

    private CheckMod checkMod;


    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        instance = this;

        saveResource("config.yml", false);
        saveDefaultConfig();
        this.configManager = new ConfigManager();
        this.configManager.load();



        this.logger = getLogger();

        Objects.requireNonNull(getCommand("modcheck"))
                .setExecutor(new ModCheckCommand());

        Objects.requireNonNull(getCommand("moddetector"))
                .setExecutor(new ModDetectorCommand());

        this.checkMod = new CheckMod();
        this.checkManager = new CheckManager();



        PacketEvents.getAPI().getEventManager()
                .registerListener(new PacketListener());
        PacketEvents.getAPI().init();



        logger.info("ModDetector enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        logger.info("ModDetector disabled!");
    }

    public CheckMod getCheckMod() {
        return checkMod;
    }

    public CheckManager getCheckManager() {
        return this.checkManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static ModDetector getInstance() {
        return instance;
    }

}
