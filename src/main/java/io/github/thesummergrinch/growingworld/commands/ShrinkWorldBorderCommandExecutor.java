package io.github.thesummergrinch.growingworld.commands;

import io.github.thesummergrinch.growingworld.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
                World world;
                for (int index = 0; index < args.length; index++) {
                    world = Bukkit.getWorld(args[index]);
                    if (world != null) {
                        if (world.getEnvironment().equals(World.Environment.NORMAL)) {
                            teleportPlayersToWorldSpawn(world);
                        }
                        world.getWorldBorder().setSize(
                                Double.parseDouble(
                                        Settings.getInstance()
                                                .getSetting("starting-size")
                                )
                        );
                    }
                }
            return true;
        }
    }
        return false;
    }

    private void teleportPlayersToWorldSpawn(final World world) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.teleport(world.getSpawnLocation(),
                    PlayerTeleportEvent.TeleportCause.PLUGIN);
        });
    }

}
