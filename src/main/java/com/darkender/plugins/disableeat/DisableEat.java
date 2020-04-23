package com.darkender.plugins.disableeat;

import com.darkender.plugins.disableeat.range.DisabledRange;
import com.darkender.plugins.disableeat.range.ServerTimeRange;
import com.darkender.plugins.disableeat.range.WorldTimeRange;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class DisableEat extends JavaPlugin
{
    private Set<String> enabledWorlds;
    private boolean enabledToDisabled;
    private boolean preventEating;
    private boolean preventDrinking;
    private Set<DisabledRange> disabledRanges;
    private String disabledMessage;
    
    @Override
    public void onEnable()
    {
        reload(getServer().getConsoleSender());
    }
    
    public void reload(CommandSender sender)
    {
        saveDefaultConfig();
        reloadConfig();
        
        enabledWorlds = new HashSet<>();
        enabledWorlds.addAll(getConfig().getStringList("enabled-worlds"));
        enabledToDisabled = getConfig().getBoolean("enabled-to-disabled");
        
        preventEating = getConfig().getBoolean("prevent-eating");
        preventDrinking = getConfig().getBoolean("prevent-drinking");
        
        disabledRanges = new HashSet<>();
        for(String value : getConfig().getStringList("disabled-world-times"))
        {
            try
            {
                String[] parts = value.split("-");
                disabledRanges.add(new WorldTimeRange(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            }
            catch(Exception e)
            {
                sender.sendMessage(ChatColor.RED + "\"" + value + "\" is not a valid world time!");
            }
        }
    
        for(String value : getConfig().getStringList("disabled-server-times"))
        {
            try
            {
                String[] parts = value.split("-");
                String[] fromParts = parts[0].split(":");
                String[] toParts = parts[1].split(":");
                disabledRanges.add(new ServerTimeRange(Integer.parseInt(fromParts[0]), Integer.parseInt(fromParts[1]),
                                                       Integer.parseInt(toParts[0]), Integer.parseInt(toParts[1])));
            }
            catch(Exception e)
            {
                sender.sendMessage(ChatColor.RED + "\"" + value + "\" is not a valid server time!");
            }
        }
        
        disabledMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("disabled-message"));
    }
}
