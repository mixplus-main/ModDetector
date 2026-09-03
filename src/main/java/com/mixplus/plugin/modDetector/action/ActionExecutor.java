package com.mixplus.plugin.modDetector.action;


import com.mixplus.plugin.modDetector.ModDetector;
import com.mixplus.plugin.modDetector.mod.ModInfo;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public final class ActionExecutor {
    private ActionExecutor() {

    }

    public static void execute(Player player, List<ModInfo> mods) {
        boolean opBypass = ModDetector.getInstance().getConfigManager().isOpBypass();
        if (player.hasPermission("mixplus.moddetector.actionBypass") && opBypass) {
            player.sendMessage(
                    Component.text(
                            "Action bypassed",
                            NamedTextColor.GREEN
                    )
            );
            return;
        }

        String names = mods.stream()
                .map(ModInfo::name)
                .collect(Collectors.joining(", "));

        for (ModInfo mod : mods) {
            ActionMode mode = mod.mode();
            if (mode == null) {
                return;
            }

            String action = mod.action();
            if (
                    action.equals("null")
                            || action.equals("none")
                            || action.isEmpty()
            ) {
                continue;
            }

            String command =
                    action
                            .replace("$player$", player.getName())
                            .replace("$mod$", mod.name())
                            .replace("$mods$", names);


            switch (mode) {
                case NONE -> {}
                case CONSOLE -> {
                    Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            command
                    );

                }
                case PLAYER -> player.performCommand(command);
            }

        }

    }
}
