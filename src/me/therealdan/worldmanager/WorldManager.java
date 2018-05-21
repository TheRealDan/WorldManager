package me.therealdan.worldmanager;

import me.therealdan.worldmanager.commands.WorldCommand;
import me.therealdan.worldmanager.world.World;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldManager extends JavaPlugin {

    private static WorldManager worldManager;
    public static String MAIN, SECOND;

    @Override
    public void onEnable() {
        worldManager = this;

        saveDefaultConfig();
        MAIN = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Color.Main"));
        SECOND = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Color.Secondary"));

        World.load();

        getCommand("World").setExecutor(new WorldCommand());
    }

    @Override
    public void onDisable() {
        World.unload();
    }

    public static WorldManager getInstance() {
        return worldManager;
    }
}