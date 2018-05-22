package me.therealdan.worldmanager.world;

import me.therealdan.worldmanager.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class World {

    private static File file;
    private static FileConfiguration data;
    private static String path = "worlds.yml";

    private static HashSet<World> worlds = new HashSet<>();

    private String name;
    private UUID uuid;

    public World(String name) {
        org.bukkit.World world = new WorldCreator(name).environment(org.bukkit.World.Environment.NORMAL).createWorld();

        this.name = world.getName();
        this.uuid = world.getUID();

        worlds.add(this);
    }

    private void save() {
        getData().set("Worlds." + getName(), "");
    }

    public boolean delete() {
        org.bukkit.World world = getWorld();

        if (world == null) return false;
        if (world.getPlayers().size() > 0) return false;

        Bukkit.unloadWorld(world, false);

        String path = Bukkit.getWorldContainer().getAbsolutePath();
        delete(new File(path.substring(0, path.length() - 1), getName()));

        worlds.remove(this);
        return true;
    }

    private void delete(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File each : contents) {
                delete(each);
            }
        }
        file.delete();
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public org.bukkit.World getWorld() {
        return Bukkit.getWorld(getUUID());
    }

    public static void load() {
        if (getData().contains("Worlds"))
            for (String name : getData().getConfigurationSection("Worlds").getKeys(false))
                new World(name);
    }

    public static void unload() {
        getData().set("Worlds", null);

        for (World world : values())
            world.save();

        saveData();
    }

    public static World byName(String name) {
        for (World world : values())
            if (world.getName().equalsIgnoreCase(name))
                return world;
        return null;
    }

    public static World byUUID(UUID uuid) {
        for (World world : values())
            if (world.getUUID().equals(uuid))
                return world;
        return null;
    }

    public static List<World> values() {
        return new ArrayList<>(worlds);
    }

    private static void saveData() {
        try {
            getData().save(getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FileConfiguration getData() {
        if (data == null) data = YamlConfiguration.loadConfiguration(getFile());
        return data;
    }

    private static File getFile() {
        if (file == null) file = new File(WorldManager.getInstance().getDataFolder(), path);
        return file;
    }
}