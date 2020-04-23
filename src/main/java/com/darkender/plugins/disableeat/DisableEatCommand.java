package com.darkender.plugins.disableeat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DisableEatCommand implements CommandExecutor, TabCompleter
{
    private final List<String> empty = new ArrayList<>();
    private final DisableEat plugin;
    
    public DisableEatCommand(DisableEat plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length == 0)
        {
            sender.sendMessage(ChatColor.RED + "No argument specified!");
            return true;
        }
        
        if(args[0].equals("reload"))
        {
            if(sender.hasPermission("disableeat.cmd.reload"))
            {
                plugin.reload(sender);
                sender.sendMessage(ChatColor.GREEN + "Reloaded!");
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission!");
            }
            return true;
        }
        else if(args[0].equals("check"))
        {
            if(!sender.hasPermission("disableeat.cmd.check"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission!");
                return true;
            }
            
            World world = null;
            if(args.length == 1)
            {
                if(sender instanceof Player)
                {
                    world = ((Player) sender).getWorld();
                }
            }
            else
            {
                world = Bukkit.getWorld(args[1]);
            }
            
            if(world == null)
            {
                sender.sendMessage(ChatColor.YELLOW + "No world specified - check will be based on server time only");
            }
            
            boolean disabled;
            if(world == null)
            {
                disabled = plugin.isInTimeRange(null);
            }
            else
            {
                disabled = plugin.isEnabledWorld(world) && plugin.isInTimeRange(world);
            }
            
            sender.sendMessage(ChatColor.GOLD + "You " +
                    ((disabled && plugin.shouldPreventEating()) ? (ChatColor.RED + "cannot") : (ChatColor.GREEN + "can")) +
                    ChatColor.GOLD + " eat and " +
                    ((disabled && plugin.shouldPreventDrinking()) ? (ChatColor.RED + "cannot") : (ChatColor.GREEN + "can")) +
                    ChatColor.GOLD + " drink");
            ZonedDateTime nowZoned = ZonedDateTime.now();
            sender.sendMessage(ChatColor.GOLD + "Server time: " + ChatColor.BLUE + nowZoned.getHour() + ":" + nowZoned.getMinute());
            if(world != null)
            {
                sender.sendMessage(ChatColor.GOLD + "World time: " + ChatColor.BLUE + world.getTime());
            }
            return true;
        }
        return false;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length == 1)
        {
            List<String> tab = new ArrayList<>();
            if(sender.hasPermission("disableeat.cmd.check"))
            {
                tab.add("check");
            }
            if(sender.hasPermission("disableeat.cmd.reload"))
            {
                tab.add("reload");
            }
            return tab;
        }
        else if(args.length == 2 && args[0].equals("check") && sender.hasPermission("disableeat.cmd.check"))
        {
            List<String> worldNames = new ArrayList<>();
            for(World world : Bukkit.getWorlds())
            {
                worldNames.add(world.getName());
            }
            return worldNames;
        }
        return empty;
    }
}
