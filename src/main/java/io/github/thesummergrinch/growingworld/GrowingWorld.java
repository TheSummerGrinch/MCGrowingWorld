package io.github.thesummergrinch.growingworld;

import io.github.thesummergrinch.growingworld.commands.ClearAdvancementsCommandExecutor;
import io.github.thesummergrinch.growingworld.commands.SetAllowRecipeAdvancementsCommandExecutor;
import io.github.thesummergrinch.growingworld.commands.ShrinkWorldBorderCommandExecutor;
import io.github.thesummergrinch.growingworld.commands.StartWorldBorderExpandingCommandExecutor;
import io.github.thesummergrinch.growingworld.commands.StopWorldBorderExpandingCommandExecutor;
import io.github.thesummergrinch.growingworld.listeners.OnPlayerAdvancementDoneEventHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrowingWorld extends JavaPlugin {

    private FileConfiguration fileConfiguration;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.fileConfiguration = this.getConfig();

        registerCommands();
        registerEventHandlers();
        enableMetrics();
        checkForUpdate();

    }

    @Override
    public void onDisable() {

        this.saveConfig();

    }

    private void registerCommands() {

        this.getCommand("clearadvancements")
                .setExecutor(new ClearAdvancementsCommandExecutor());
        this.getCommand("startworldborderexpanding")
                .setExecutor(new StartWorldBorderExpandingCommandExecutor());
        this.getCommand("stopworldborderexpanding")
                .setExecutor(new StopWorldBorderExpandingCommandExecutor());
        this.getCommand("shrinkworldborder")
                .setExecutor(new ShrinkWorldBorderCommandExecutor(this.fileConfiguration));
        this.getCommand("setallowrecipeadvancements")
                .setExecutor(new SetAllowRecipeAdvancementsCommandExecutor(this.fileConfiguration));

    }

    private void registerEventHandlers() {

        this.getServer().getPluginManager()
                .registerEvents(new OnPlayerAdvancementDoneEventHandler(), this);

    }

    private void enableMetrics() {
        //TODO add localization.
        if (this.fileConfiguration.getBoolean("first-run")) {

            this.fileConfiguration.set("first-run", false);
            getLogger().info("Metrics will be enabled on the next start.");

        } else if (this.fileConfiguration.getBoolean("allow-metrics")) {

            final int pluginID = 10258;
            new Metrics(this, pluginID);
            getLogger().info("Metrics enabled.");

        } else {

            getLogger().info("Metrics disabled.");

        }
    }

    private void checkForUpdate() {
        if (this.fileConfiguration.getBoolean("enable-update-checking")) {
            new UpdateChecker(this, 88787).getVersion(version -> {
                String[] publishedVersion = version.split("\\.");
                String[] currentVersion = this.getDescription().getVersion().split("\\.");
                if (publishedVersion.length == currentVersion.length) {
                    for (int i = 0; i < publishedVersion.length; i++) {
                        if (Integer.parseInt(publishedVersion[i]) > Integer.parseInt(currentVersion[i])) {
                            getLogger().warning("A new version is available: " + version);
                            return;
                        }
                    }
                } else if (publishedVersion.length < (currentVersion).length) {
                    for (int i = 0; i < publishedVersion.length; i++) {
                        if (Integer.parseInt(publishedVersion[i]) > Integer.parseInt(currentVersion[i])) {
                            getLogger().warning("A new version is available: " + version);
                            return;
                        }
                    }
                } else {
                    for (int i = 0; i < currentVersion.length; i++) {
                        if (Integer.parseInt(publishedVersion[i]) > Integer.parseInt(currentVersion[i])) {
                            getLogger().warning("A new version is available: " + version);
                            return;
                        }
                    }
                }
            });
        }
    }

    public FileConfiguration getFileConfiguration() {
        return this.fileConfiguration;
    }

}
