package io.github.thesummergrinch.growingworld.commands;

import io.github.thesummergrinch.growingworld.config.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetAllowRecipeAdvancementsCommandExecutor implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender.isOp() && args.length >= 1) {

            if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase(
                    "false")) {
                Settings.getInstance().setSetting("allow-recipe-advancements"
                        , (args[0].equalsIgnoreCase("true")
                                ? "true"
                                : "false"));
                return true;
            }

        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<String>() {
            {
                add("true");
                add("false");
            }
        };
    }
}
