package com.darkender.plugins.fasting.range;

import jdk.internal.jline.internal.Nullable;
import org.bukkit.World;

public class WorldTimeRange extends DisabledRange
{
    private int from, to;
    
    public WorldTimeRange(int from, int to)
    {
        this.from = from;
        this.to = to;
    }
    
    @Override
    public boolean in(@Nullable World world)
    {
        return (world != null && (world.getTime() >= from && world.getTime() <= to));
    }
    
    public int getFrom()
    {
        return from;
    }
    
    public int getTo()
    {
        return to;
    }
}
