package com.mixplus.plugin.modDetector.check.sign;

import com.mixplus.plugin.modDetector.ModDetector;
import com.mixplus.plugin.modDetector.action.ActionExecutor;
import com.mixplus.plugin.modDetector.config.ConfigManager;
import com.mixplus.plugin.modDetector.mod.ModInfo;
import com.mixplus.plugin.modDetector.check.impl.UpdateSignCheck;
import com.mixplus.library.paper.client.ClientDisplay;

import io.papermc.paper.math.Position;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SignChecker {
    private final JavaPlugin plugin;
    private final ModDetector instance;
    private final ConfigManager configManager;

    private final ClientDisplay clientDisplay;

    private final List<ModInfo> mods;

    private int index;
    private final Player player;
    private final CommandSender resultSender;
    private final long warmup;

    private final long chackRate;
    private final long delay;

    private long warmupCount = 0;
    private final boolean executeAction;

    public SignChecker(Player player, CommandSender resultSender, boolean executeAction) {
        this.plugin = ModDetector.getPlugin();
        this.instance = ModDetector.getInstance();
        this.configManager = instance.getConfigManager();

        this.clientDisplay = new ClientDisplay(player);

        this.player = player;
        this.resultSender = resultSender;
        this.executeAction = executeAction;
        this.mods = new ArrayList<>(configManager.getMods().values());
        this.chackRate = configManager.getCheckRate();
        this.delay = configManager.getCheckDelay();
        this.warmup = configManager.getWarmup();
    }

    public void start() {
        this.index = 0;
        if (mods.isEmpty()) {
            finish();
            return;
        }

        open(mods.get(index), "Warmup");
    }

    private void open(ModInfo mod, String checkName) {
        UpdateSignCheck.setTargetMod(player.getUniqueId(), mod);

        clientDisplay.openSign(
                List.of(
                        Component.text(""),
                        Component.translatable(mod.key()),
                        Component.translatable(mod.key()),
                        Component.text(checkName)
                )
        );

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            clientDisplay.closeSign();
            next();
        }, this.chackRate);

    }

    private void next() {
        warmupCount++;

        String checkName;
        ModInfo mod;

        if (warmupCount < warmup) {
            index = 0;
            checkName = "Warmup";
            mod = mods.get(ThreadLocalRandom.current().nextInt(mods.size()));
        } else {
            checkName = "CheckSign";

            if (index >= mods.size()) {
                finish();
                return;
            }

            mod = mods.get(index);
            index++;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            open(mod, checkName);
        }, this.delay);
    }

    private void finish() {
        List<ModInfo> detected =
                UpdateSignCheck.getDetected(player.getUniqueId());


        if (detected.isEmpty()) {
            resultSender.sendMessage("No mods were detected.");
        } else {
            String names = detected.stream()
                    .map(ModInfo::name)
                    .collect(Collectors.joining(", "));

            resultSender.sendMessage(
                    Component.text("Detection:\n" + names)
            );
        }

        UpdateSignCheck.removeDetected(player.getUniqueId());
        if (executeAction) {
            ActionExecutor.execute(player, detected);
        }
    }
}
