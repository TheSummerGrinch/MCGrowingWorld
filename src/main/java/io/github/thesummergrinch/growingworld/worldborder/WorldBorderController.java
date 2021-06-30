package io.github.thesummergrinch.growingworld.worldborder;

import io.github.thesummergrinch.growingworld.GrowingWorld;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorldBorderController {

    private static volatile WorldBorderController instance;

    private final Random random;
    private final AtomicBoolean isPeriodicallyExpanding;

    private final GrowingWorld plugin;
    private final FileConfiguration fileConfiguration;

    private WorldBorderController() {
        this.plugin = GrowingWorld.getPlugin(GrowingWorld.class);
        this.fileConfiguration = this.plugin.getFileConfiguration();
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
                .startsWith("recipes/") || this.fileConfiguration
                .getBoolean("allow-recipe-advancements")) {
            int growth = random.nextInt(this.fileConfiguration.getInt(
                    "max-growth"));
            if (growth < this.fileConfiguration.getInt("min-growth"))
                growth = this.fileConfiguration.getInt("min-growth");
            int finalGrowth = growth;
            Bukkit.getWorlds().forEach(world -> world.getWorldBorder()
                    .setSize(world.getWorldBorder().getSize() + finalGrowth));
        }
    }

    public void growOnAdvancement(final Advancement advancement) {

        final boolean isRecipeAdvancement =
                advancement.getKey().getKey().startsWith("recipes/");

        final boolean allowRecipeAdvancements =
                this.fileConfiguration.getBoolean("allow-recipe-advancements");

        if (allowRecipeAdvancements || !isRecipeAdvancement) {

            int growth = (isRecipeAdvancement)
                    // Looking at this code months later, thank goodness for the formula.
                    // random(maxRecipe - minRecipe) + minRecipe
                            ? random.nextInt(this.fileConfiguration.getInt("max-recipe-growth")
                            - this.fileConfiguration.getInt("min-recipe-growth"))
                            + this.fileConfiguration.getInt("min-recipe-growth")

                            // random(maxAdvancement - minAdvancement) +
                            // minAdvancement
                            : random.nextInt(this.fileConfiguration.getInt("max-advancement-growth")
                            - this.fileConfiguration.getInt("min-advancement-growth"))
                            + this.fileConfiguration.getInt("min-advancement-growth");

            if (isRecipeAdvancement
                    && growth < this.fileConfiguration.getInt("min-recipe-growth")) {

                growth = this.fileConfiguration.getInt("min-recipe-growth");

            } else if (!isRecipeAdvancement
                    && growth < this.fileConfiguration.getInt("min-advancement-growth")) {

                growth = this.fileConfiguration.getInt("min-advancement-growth");

            }
            int finalGrowth = growth;
            Bukkit.getWorlds().forEach(world -> {
                world.getWorldBorder().setSize(world.getWorldBorder().getSize()
                        + finalGrowth); //TODO replace with specific worlds.
            });
        }
    }

    private void growPassively() {
        // random(maxPassive - minPassive) + minPassive
        int growth = random.nextInt(
                this.fileConfiguration.getInt("max-passive-growth")
                - this.fileConfiguration.getInt("min-passive-growth")
                        + this.fileConfiguration.getInt("min-passive-growth"));
        int finalGrowth =
                Math.max(growth, this.fileConfiguration.getInt("min-passive-growth"));
        Bukkit.getWorlds().forEach(world -> world.getWorldBorder().setSize(world.getWorldBorder().getSize()
                + finalGrowth));
    }

    public void startPeriodicallyExpanding() {
        this.isPeriodicallyExpanding.set(true);
        this.fileConfiguration.set("is-worldborder-expanding", true);
        startGrowthRunnable();
    }

    public void stopPeriodicallyExpanding() {
        this.isPeriodicallyExpanding.set(false);
        this.fileConfiguration.set("is-worldborder-expanding", false);
    }

    private void startGrowthRunnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isPeriodicallyExpanding.get()) return;
                growPassively();
                startGrowthRunnable();
            }
        }.runTaskLater(GrowingWorld.getPlugin(GrowingWorld.class),
                fileConfiguration.getLong("growth-interval-in-minutes") * 60L * 20L);
    }

    public void setWorldBorderSize(final double newSize) {
        Bukkit.getWorlds().forEach(world -> {
            world.getWorldBorder().setCenter(world.getSpawnLocation());
            world.getWorldBorder().setSize(newSize);
        });
    }

}
