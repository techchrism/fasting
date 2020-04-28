package com.darkender.plugins.fasting;

import com.darkender.plugins.fasting.range.DisabledRange;
import com.darkender.plugins.fasting.range.ServerTimeRange;
import com.darkender.plugins.fasting.range.WorldTimeRange;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class Fasting extends JavaPlugin implements Listener
{
    private Set<String> enabledWorlds;
    private boolean enabledToDisabled;
    private boolean preventEating;
    private boolean preventDrinking;
    private Set<DisabledRange> disabledRanges;
    private String disabledMessage;
    private boolean disableStarvationWhileFasting;
    private boolean preventSprintLoss;
    
    @Override
    public void onEnable()
    {
        reload(getServer().getConsoleSender());
        
        FastingCommand fastCommand = new FastingCommand(this);
        getCommand("fasting").setExecutor(fastCommand);
        getCommand("fasting").setTabCompleter(fastCommand);
        
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    public boolean isEnabledWorld(World world)
    {
        return (enabledWorlds.contains(world.getName()) != enabledToDisabled);
    }
    
    public boolean isInTimeRange(@Nullable World world)
    {
        for(DisabledRange range : disabledRanges)
        {
            if(range.in(world))
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean isDisabled(@Nullable World world)
    {
        return (isEnabledWorld(world) && isInTimeRange(world));
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        boolean disabled = isDisabled(event.getPlayer().getWorld());
        if(disabled)
        {
            if(event.getItem().getType().isEdible() && preventEating)
            {
                event.setCancelled(true);
            }
            else if(preventDrinking)
            {
                event.setCancelled(true);
            }
            
            if(event.isCancelled() && !disabledMessage.isEmpty())
            {
                event.getPlayer().sendMessage(disabledMessage);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event)
    {
        if(disableStarvationWhileFasting && event.getCause() == EntityDamageEvent.DamageCause.STARVATION)
        {
            if(isDisabled(event.getEntity().getWorld()))
            {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        if(preventSprintLoss && event.getFoodLevel() < 6 && isDisabled(event.getEntity().getWorld()))
        {
            event.setCancelled(true);
        }
    }
    
    public void reload(CommandSender sender)
    {
        saveDefaultConfig();
        reloadConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        
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
        disableStarvationWhileFasting = getConfig().getBoolean("prevent-starvation-damage");
        preventSprintLoss = getConfig().getBoolean("prevent-sprint-loss");
    }
    
    public boolean shouldPreventEating()
    {
        return preventEating;
    }
    
    public boolean shouldPreventDrinking()
    {
        return preventDrinking;
    }
}
