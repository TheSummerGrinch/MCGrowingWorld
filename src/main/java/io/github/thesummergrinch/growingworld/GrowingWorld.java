package io.github.thesummergrinch.growingworld;

import io.github.thesummergrinch.growingworld.commands.ClearAdvancementsCommandExecutor;
import io.github.thesummergrinch.growingworld.commands.ShrinkWorldBorderCommandExecutor;
import io.github.thesummergrinch.growingworld.commands.StartWorldBorderExpandingCommandExecutor;
import io.github.thesummergrinch.growingworld.commands.StopWorldBorderExpandingCommandExecutor;
import io.github.thesummergrinch.growingworld.config.FileConfigurationLoader;
import io.github.thesummergrinch.growingworld.config.Settings;
import io.github.thesummergrinch.growingworld.listeners.OnPlayerAdvancementDoneEventHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrowingWorld extends JavaPlugin {

    static {

        ConfigurationSerialization.registerClass(Settings.class);

    }

    @Override
    public void onEnable() {

        FileConfigurationLoader.getInstance().loadSettings();
        saveConfig();
        registerCommands();
        registerEventHandlers();
        enableMetrics();

    }

    @Override
    public void onDisable() {

        FileConfigurationLoader.getInstance().saveSettings();
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
                .setExecutor(new ShrinkWorldBorderCommandExecutor());

    }

    private void registerEventHandlers() {

        this.getServer().getPluginManager()
                .registerEvents(new OnPlayerAdvancementDoneEventHandler(), this);

    }

    private void enableMetrics() {
        //TODO add localization.
        if (Settings.getInstance().getSetting("first-run").equals("true")) {

            Settings.getInstance().setSetting("first-run", "false");
            getLogger().info("Metrics will be enabled on the next start.");
            //getLogger().log(Level.INFO,
                    //LanguageFileLoader.getInstance().getString("metrics" +
                    //"-enabled-on-next-launch"));

        } else if (Settings.getInstance().getSetting("allow-metrics").equals("true")) {

            final int pluginID = 10258;
            new Metrics(this, pluginID);
            getLogger().info("Metrics enabled.");
            //getLogger().log(Level.INFO,
                    //LanguageFileLoader.getInstance().getString("metrics" +
                    //"-enabled"));

        } else {

            getLogger().info("Metrics disabled.");
            //getLogger().log(Level.INFO, LanguageFileLoader
                    //.getInstance().getString("metrics-disabled"));

        }
    }



}
