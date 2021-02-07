package io.github.thesummergrinch.growingworld.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class Settings implements ConfigurationSerializable {

    private static volatile Settings instance;

    private final Map<String, String> settingsMap;

    private Settings() {
        this.settingsMap = new HashMap<>();
    }

    public static Settings getInstance() {

        if (instance == null) {

            synchronized (Settings.class) {

                instance = new Settings();

            }

        }

        return instance;

    }

    public void setSettings(final Map<String, String> settings) {

        this.settingsMap.putAll(settings);

    }

    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> settings = new HashMap<>();

        settingsMap.forEach(settings::put);

        return settings;

    }

    @NotNull
    public static Settings deserialize(@NotNull final Map<String, Object> serializedSettings) {

        Settings settings = Settings.getInstance();

        serializedSettings.forEach((key, value) -> {

            settings.setSetting(key, (String) value);

        });

        return Settings.getInstance();

    }

    public String getSetting(@NotNull final String key) {

        String value = this.settingsMap.get(key);

        if (value != null) return value;

        value = FileConfigurationLoader.getInstance().getDefaultSettings().get(key);

        if (value != null) setSetting(key, value);

        return value;

    }

    public void setSetting(@NotNull final String key,
                           @NotNull final String value) {

        this.settingsMap.put(key, value);

    }

    public boolean containsKey(@NotNull final String key) {
        return this.settingsMap.get(key) != null;
    }
}
