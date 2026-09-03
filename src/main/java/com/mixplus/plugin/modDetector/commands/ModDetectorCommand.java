package com.mixplus.plugin.modDetector.commands;


import com.mixplus.library.paper.Sounds;
import com.mixplus.plugin.modDetector.ModDetector;
import com.mixplus.plugin.modDetector.check.sign.SignChecker;
import com.mixplus.plugin.modDetector.config.ConfigManager;
import com.mixplus.library.paper.command.TabCompleteUtil;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModDetectorCommand implements CommandExecutor, TabCompleter {
    private final ModDetector instance;
    private final ConfigManager configManager;

    public ModDetectorCommand() {
        this.instance = ModDetector.getInstance();
        this.configManager = instance.getConfigManager();
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String @NotNull [] args
    ) {
        if (args.length == 0) {
            return true;
        }


        String subCommand = args[0];


        switch (subCommand) {
            case "reload" -> {
                configManager.reload();
                sender.sendMessage(
                        Component.text(
                                "Reloaded.",
                            NamedTextColor.GREEN
                        )
                );

                if (sender instanceof Player player) {
                    Sounds.success(player);
                }
            }
            case "opBypass" -> {
                if (args.length == 2) {

                    if (!args[1].equalsIgnoreCase("true")
                            && !args[1].equalsIgnoreCase("false")) {

                        sender.sendMessage(
                                Component.text(
                                        "Invalid argument. Please specify true or false.",
                                        NamedTextColor.RED
                                )
                        );

                        if (sender instanceof Player player) {
                            Sounds.failure(player);
                        }
                        return true;
                    }
                    boolean opBypass = Boolean.parseBoolean(args[1]);



                    configManager.setOpBypass(opBypass);
                    sender.sendMessage(
                            Component.text("OP Bypass has been ")
                                    .append(Component.text(opBypass ? "enabled" : "disabled"))
                                    .append(Component.text("."))
                                    .color(NamedTextColor.GREEN)
                    );
                    if (sender instanceof Player player) {
                        Sounds.success(player);
                    }
                    return true;
                }
            }
            case "check" -> {
                return check(args, sender, true);
            }
            case "checkOnly" -> {
                return check(args, sender, false);
            }
            default -> {
                sender.sendMessage(
                        Component.text(
                                "Please use /moddetector [reload|opBypass|check|checkOnly] [boolean|username].",
                                NamedTextColor.YELLOW
                        )
                );

                if (sender instanceof Player player) {
                    Sounds.failure(player);
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String @NotNull [] args
    ) {
        if (args.length == 1) {
            return TabCompleteUtil.filter(
                    List.of("check", "checkOnly", "reload", "opBypass"),
                    args[0]
            );
        }

        if (args.length == 2) {
            return switch (args[0]) {
                case "opBypass" -> TabCompleteUtil.filter(
                        List.of("true", "false"),
                        args[1]
                );

                case "check", "checkOnly" -> TabCompleteUtil.filter(
                        Bukkit.getOnlinePlayers().stream()
                                .map(Player::getName)
                                .toList(),
                        args[1]
                );

                default -> List.of();
            };
        }
        return List.of();
    }

    private boolean check(String[] args, CommandSender sender, boolean executeAction) {
        if (args.length == 2) {

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(
                        Component.text(
                                "Player is not online.",
                                NamedTextColor.RED
                        )
                );
                if (sender instanceof Player player) {
                    Sounds.failure(player);
                }
                return true;
            }

            target.closeDialog();
            target.closeInventory();

            new SignChecker(target, sender, executeAction).start();

            if (sender instanceof Player player) {
                Sounds.success(player);
            }
        }
        return true;
    }
}
