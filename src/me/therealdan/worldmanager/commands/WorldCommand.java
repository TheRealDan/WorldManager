package me.therealdan.worldmanager.commands;

import me.therealdan.worldmanager.WorldManager;
import me.therealdan.worldmanager.world.World;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
        String firstArgument = args.length > 0 ? args[0].toLowerCase() : "";
        World world = World.byName(args.length > 1 ? args[1] : "");

        switch (firstArgument) {
            case "list":
                if (World.values().size() == 0) {
                    sender.sendMessage(WorldManager.MAIN + "There are no Worlds.");
                    return true;
                }
                StringBuilder worlds = new StringBuilder();
                for (World each : World.values())
                    worlds.append(WorldManager.MAIN).append(", ").append(WorldManager.SECOND).append(each.getName());
                sender.sendMessage(WorldManager.MAIN + "Worlds: " + worlds.toString().replaceFirst(", ", ""));
                break;
            case "tp":
                Player target = sender instanceof Player ? (Player) sender : null;
                if (args.length > 2) target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(WorldManager.MAIN + "Invalid Player.");
                    return true;
                }
                if (world == null) {
                    sender.sendMessage(WorldManager.MAIN + "No world with that name exists.");
                    return true;
                }
                target.teleport(world.getWorld().getSpawnLocation());
                sender.sendMessage(WorldManager.SECOND + target.getName() + WorldManager.MAIN + " sent to " + WorldManager.SECOND + world.getName());
                break;
            case "create":
                if (args.length == 1) {
                    sender.sendMessage(WorldManager.MAIN + "/World Create [World]");
                    return true;
                }
                if (world != null) {
                    sender.sendMessage(WorldManager.MAIN + "A world with that name already exists.");
                    return true;
                }
                world = new World(args[1]);
                sender.sendMessage(WorldManager.MAIN + "Created new world called " + WorldManager.SECOND + world.getName());
                break;
            case "delete":
                if (world == null) {
                    sender.sendMessage(WorldManager.MAIN + "No world exists with that name.");
                    return true;
                }
                boolean confirm = args.length > 2 && args[2].equalsIgnoreCase("confirm");
                if (confirm) {
                    if (world.delete()) {
                        sender.sendMessage(WorldManager.MAIN + "Permanently deleted world " + WorldManager.SECOND + world.getName());
                    } else {
                        sender.sendMessage(WorldManager.SECOND + world.getName() + WorldManager.MAIN + " cannot be deleted right now.");
                    }
                } else {
                    sender.sendMessage(WorldManager.MAIN + "Are you sure you want to Permanently delete world " + WorldManager.SECOND + world.getName() + WorldManager.MAIN + "?");
                    sender.sendMessage(WorldManager.MAIN + "/World Delete [World] Confirm");
                }
                break;
            default:
                sender.sendMessage(WorldManager.MAIN + "/World List " + WorldManager.SECOND + "List worlds");
                sender.sendMessage(WorldManager.MAIN + "/World TP [World] [Player] " + WorldManager.SECOND + "Teleport to a world");
                sender.sendMessage(WorldManager.MAIN + "/World Create [World] " + WorldManager.SECOND + "Create a new world");
                sender.sendMessage(WorldManager.MAIN + "/World Delete [World] " + WorldManager.SECOND + "Delete an existing world");
        }

        return true;
    }
}