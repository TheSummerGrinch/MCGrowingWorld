package io.github.thesummergrinch.growingworld.config;

import io.github.thesummergrinch.growingworld.GrowingWorld;
import io.github.thesummergrinch.growingworld.worldborder.WorldBorderController;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class FileConfigurationLoader {

    private static volatile FileConfigurationLoader instance;

    private final FileConfiguration fileConfiguration;

    private FileConfigurationLoader() {

        this.fileConfiguration =
                GrowingWorld.getPlugin(GrowingWorld.class).getConfig();

    }

    public static FileConfigurationLoader getInstance() {

        if (instance != null) return instance;

        synchronized (FileConfigurationLoader.class) {

            if (instance == null) instance = new FileConfigurationLoader();

        }

        return instance;

    }

    protected Map<String, String> getDefaultSettings() {

        return new HashMap<String, String>() {
            {

                put("growth-interval-in-minutes", "2");
                put("min-growth", "1");
                put("max-growth", "2");
                put("allow-metrics", "true");
                put("first-run", "true");
                put("starting-size", "10.0");
                put("config-version", "3");
                put("allow-recipe-advancements", "false");
                put("is-worldborder-expanding", "false");

            }
        };
    }

    public void saveSettings() {

        this.fileConfiguration.set("settings", Settings.getInstance());

    }

    public Settings loadSettings() {

        Settings settings = this.fileConfiguration.getObject("settings",
                Settings.class);

        if (settings == null) {

            settings = Settings.getInstance();
            settings.setSettings(getDefaultSettings());

        }

        // Auto-updater for config.yml
        if (!settings.containsKey("config-version")
                || !settings.getSetting("config-version")
                .equals(getDefaultSettings().get("config-version"))) {

            Settings finalSettings = Settings.getInstance();

            getDefaultSettings().forEach((key, value) -> {

                if (!finalSettings.containsKey(key)) {

                    finalSettings.setSetting(key, value);

                }

            });
            settings.setSetting("config-version", getDefaultSettings().get(
                    "config-version"));
        }

        if (Boolean.parseBoolean(Settings.getInstance().getSetting("is" +
                "-worldborder-expanding"))) {
            WorldBorderController.getInstance().startPeriodicallyExpanding();
        }

        return Settings.getInstance();

    }

}
