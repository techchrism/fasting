package com.darkender.plugins.disableeat.range;

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
    public boolean in(World world)
    {
        return (world.getTime() >= from && world.getTime() <= to);
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
