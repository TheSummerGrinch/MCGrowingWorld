package io.github.thesummergrinch.growingworld.listeners;

import io.github.thesummergrinch.growingworld.config.Settings;
import io.github.thesummergrinch.growingworld.worldborder.WorldBorderController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class OnPlayerAdvancementDoneEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerAdvancementDoneEvent(final PlayerAdvancementDoneEvent event) {
        if (!event.getAdvancement().getKey().getKey().substring(0, 8)
                .equals("recipes/") && !Boolean.parseBoolean(Settings.getInstance()
                .getSetting("allow-recipe-advancements"))) {
            WorldBorderController.getInstance().growWorldBorder();
        }
    }

}
