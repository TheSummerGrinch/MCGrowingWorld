package io.github.thesummergrinch.growingworld.worldborder;

import io.github.thesummergrinch.growingworld.GrowingWorld;
import io.github.thesummergrinch.growingworld.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorldBorderController {

    private static volatile WorldBorderController instance;

    private final Random random;
    private final AtomicBoolean isPeriodicallyExpanding;

    private WorldBorderController() {
        this.random = new Random();
        this.isPeriodicallyExpanding = new AtomicBoolean(false);
    }

    public static WorldBorderController getInstance() {
        if (instance == null) {
            synchronized (WorldBorderController.class) {
                instance = new WorldBorderController();
            }
        }
        return instance;
    }

    @Deprecated
    public void growWorldBorder(final Advancement advancement) {
        if (!advancement.getKey().getKey()
                .startsWith("recipes/") || Boolean.parseBoolean(Settings.getInstance()
                .getSetting("allow-recipe-advancements"))) {
            int growth = random.nextInt(Integer.parseInt(Settings.getInstance().getSetting(
                    "max" +
                            "-growth")));
            if (growth < Integer.parseInt(Settings.getInstance().getSetting("min" +
                    "-growth"))) growth =
                    Integer.parseInt(Settings.getInstance().getSetting("min" +
                            "-growth"));
            int finalGrowth = growth;
            Bukkit.getWorlds().forEach(world -> {
                world.getWorldBorder().setSize(world.getWorldBorder().getSize() + finalGrowth);
            });
        }
    }

    public void growOnAdvancement(final Advancement advancement) {
        final boolean isRecipeAdvancement =
                advancement.getKey().getKey().startsWith("recipes/");
        final boolean allowRecipeAdvancements =
                Boolean.parseBoolean(Settings.getInstance().getSetting("allow" +
                        "-recipe-advancements"));
        if (allowRecipeAdvancements || !isRecipeAdvancement) {
            final int growth =
                    (isRecipeAdvancement)
                            // random(maxRecipe - minRecipe) + minRecipe
                            ? random.nextInt(Integer.parseInt(
                            Settings.getInstance()
                                    .getSetting("max-recipe-growth"))
                            - Integer.parseInt(Settings.getInstance()
                            .getSetting("min-recipe-growth"))
                            + Integer.parseInt(Settings.getInstance()
                            .getSetting("min-recipe-growth")))

                            // random(maxAdvancement - minAdvancement) +
                            // minAdvancement
                            : random.nextInt(Integer.parseInt(
                            Settings.getInstance()
                                    .getSetting("max-advancement" +
                                            "-growth"))
                            - Integer.parseInt(Settings.getInstance()
                            .getSetting("min-advancement-growth"))
                            + Integer.parseInt(Settings.getInstance()
                            .getSetting("min-advancement-growth")));
            Bukkit.getWorlds().forEach(world -> {
                world.getWorldBorder().setSize(world.getWorldBorder().getSize()
                        + growth); //TODO replace with specific worlds.
            });
        }
    }

        Bukkit.getWorlds().forEach(world -> {
            world.getWorldBorder().setSize(world.getWorldBorder().getSize() + finalGrowth);
        });
    }

    public void startPeriodicallyExpanding() {
        this.isPeriodicallyExpanding.set(true);
        Settings.getInstance().setSetting("is-worldborder-expanding", "true");
        startGrowthRunnable();
    }

    public void stopPeriodicallyExpanding() {
        this.isPeriodicallyExpanding.set(false);
        Settings.getInstance().setSetting("is-worldborder-expanding", "false");
    }

    private void startGrowthRunnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isPeriodicallyExpanding.get()) return;
                growWorldBorder();
                startGrowthRunnable();
            }
        }.runTaskLater(GrowingWorld.getPlugin(GrowingWorld.class),
                Long.parseLong(Settings.getInstance().getSetting(
                "growth" +
                        "-interval-in-minutes")) * 60L * 20L);
    }

    public void setWorldBorderSize(final double newSize) {
        Bukkit.getWorlds().forEach(world -> {
            world.getWorldBorder().setCenter(world.getSpawnLocation());
            world.getWorldBorder().setSize(newSize);
        });
    }

}
