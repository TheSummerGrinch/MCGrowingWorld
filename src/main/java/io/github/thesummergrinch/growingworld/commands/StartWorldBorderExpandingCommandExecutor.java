package io.github.thesummergrinch.growingworld.commands;

import io.github.thesummergrinch.growingworld.worldborder.WorldBorderController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StartWorldBorderExpandingCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender.isOp()) {

            WorldBorderController.getInstance().startPeriodicallyExpanding();

            sender.sendMessage("[GrowingWorld] WorldBorder has started " +
                    "expanding periodically.");

            return true;

        }

        return false;

    }

}
