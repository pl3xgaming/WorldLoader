package net.pl3x.bukkit.worldloader;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ThreadLocalRandom;

public class WorldSettings {
    private final String name;
    private final WorldType type;
    private final World.Environment environment;
    private final long seed;
    private final boolean generateStructures;
    private final boolean pvp;
    private final Difficulty difficulty;
    private final boolean spawnMonsters;
    private final boolean spawnAnimals;
    private final String onJoinTitle;
    private final String onJoinSubtitle;
    private final String generator;

    public WorldSettings(Plugin plugin, String name, ConfigurationSection config) {
        this.name = name;
        this.type = WorldType.valueOf(config.getString("type"));
        this.environment = World.Environment.valueOf(config.getString("environment", "NORMAL"));
        this.seed = config.getLong("seed", ThreadLocalRandom.current().nextLong());
        this.generateStructures = config.getBoolean("generate-structures", true);
        this.pvp = config.getBoolean("pvp", true);
        this.difficulty = Difficulty.valueOf(config.getString("difficulty", "NORMAL"));
        this.spawnMonsters = config.getBoolean("spawn-monsters", true);
        this.spawnAnimals = config.getBoolean("spawn-animals", true);
        this.onJoinTitle = config.getString("on-join.title", null);
        this.onJoinSubtitle = config.getString("on-join.subtitle", null);
        this.generator = config.getString("generator", null);

        World world = Bukkit.getWorld(name);

        if (world == null) {
            plugin.getLogger().info("Preparing level \"" + name + "\"");

            WorldCreator builder = new WorldCreator(name).type(type).environment(environment).seed(seed).generateStructures(generateStructures);

            if (generator != null && !generator.isEmpty()) {
                builder.generator(generator);
            }

            world = builder.createWorld();

            if (world == null) {
                throw new RuntimeException();
            }
        } else {
            plugin.getLogger().info("Updating level \"" + name + "\"");
        }

        world.setPVP(pvp);
        world.setDifficulty(difficulty);
        world.setSpawnFlags(spawnAnimals, spawnMonsters);
    }

    public String getName() {
        return name;
    }

    public WorldType getType() {
        return type;
    }

    public World.Environment getEnvironment() {
        return environment;
    }

    public long getSeed() {
        return seed;
    }

    public boolean generatedStructures() {
        return generateStructures;
    }

    public boolean isPvp() {
        return pvp;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public boolean isSpawnMonsters() {
        return spawnMonsters;
    }

    public boolean isSpawnAnimals() {
        return spawnAnimals;
    }

    public String getOnJoinTitle() {
        return onJoinTitle;
    }

    public String getOnJoinSubtitle() {
        return onJoinSubtitle;
    }

    public String getGenerator() {
        return generator;
    }
}
