package io.github.thesummergrinch.growingworld.commands;

import io.github.thesummergrinch.growingworld.config.Settings;
import io.github.thesummergrinch.growingworld.worldborder.WorldBorderController;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class ShrinkWorldBorderCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender.isOp()) {
            if (args.length >= 1) {
                double newSize;
                try {
                    newSize = Double.parseDouble(args[0]);
                    teleportPlayersToWorldSpawn();
                    WorldBorderController.getInstance()
                            .setWorldBorderSize(Math.abs(newSize));
                    sender.sendMessage("[GrowingWorld] WorldBorder shrunk to " +
                            "specified size.");
                } catch (NumberFormatException exception) {
                    sender.sendMessage("[GrowingWorld] Invalid argument. " +
                            "Please only use numbers as arguments.");
                }
            } else {
                teleportPlayersToWorldSpawn();
                WorldBorderController.getInstance()
                        .setWorldBorderSize(Double.parseDouble(Settings
                                .getInstance().getSetting("starting-size")));
                sender.sendMessage("[GrowingWorld] WorldBorder shrunk to " +
                        "starting-size.");
            }
            return true;
        }

        return false;

    }

    private void teleportPlayersToWorldSpawn() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.teleport(Bukkit
                            .getWorld("world").getSpawnLocation(),
                    PlayerTeleportEvent.TeleportCause.PLUGIN);
        });
    }

}
