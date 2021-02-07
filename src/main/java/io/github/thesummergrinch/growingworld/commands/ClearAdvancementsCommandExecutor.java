package io.github.thesummergrinch.growingworld.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ClearAdvancementsCommandExecutor implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender.isOp() && args.length >= 1) {

            if (args[0].equals("@a")) {

                Bukkit.getOnlinePlayers().forEach(player -> {

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "advancement revoke " + player.getName() +
                                    " everything");

                });

            } else {

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "advancement revoke " + args[0] +
                                " everything");

            }

            return true;

        }

        return false;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        List<String> playerNames = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(player -> playerNames.add(player.getName()));

        playerNames.add("@a");

        return playerNames;

    }
}
