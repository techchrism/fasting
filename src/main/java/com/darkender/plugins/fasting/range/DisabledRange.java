package com.darkender.plugins.fasting.range;

import jdk.internal.jline.internal.Nullable;
import org.bukkit.World;

public abstract class DisabledRange
{
    public abstract boolean in(@Nullable World world);
}
