package com.mixplus.plugin.modDetector.commands;


import com.mixplus.library.paper.Sounds;
import com.mixplus.library.paper.command.TabCompleteUtil;
import com.mixplus.plugin.modDetector.check.sign.SignChecker;
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


public class ModCheckCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String @NotNull [] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(
                    Component.text(
                            "Only players can execute this.",
                            NamedTextColor.RED
                    )
            );
            return true;
        }

        if (args.length == 0) {
            new SignChecker(player, sender, true).start();
            return true;
        }


        if (!args[0].equalsIgnoreCase("true")
                && !args[0].equalsIgnoreCase("false")) {

            sender.sendMessage(
                    Component.text(
                            "Invalid argument. Please specify true or false.",
                            NamedTextColor.RED
                    )
            );

            Sounds.failure(player);
            return true;
        }
        boolean value = Boolean.parseBoolean(args[0]);



        new SignChecker(player, sender, value).start();
        return true;
    }

    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String @NotNull [] args
    ) {
        if (args.length == 1) {
            return TabCompleteUtil.filter(
                    List.of("true","false"),
                    args[0]
            );
        }
        return List.of();
    }
}
