package com.darkender.plugins.disableeat.range;

import org.bukkit.World;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;

public class ServerTimeRange extends DisabledRange
{
    private long from, to;
    
    public ServerTimeRange(int hoursFrom, int minutesFrom, int hoursTo, int minutesTo)
    {
        this.from = convertToSeconds(hoursFrom, minutesFrom);
        this.to = convertToSeconds(hoursTo, minutesTo);
    }
    
    private long convertToSeconds(int hours, int minutes)
    {
        return (hours * 60 * 60) + (minutes * 60);
    }
    
    private long getSecondsSinceMidnight()
    {
        // From https://stackoverflow.com/questions/4389500/how-can-i-find-the-amount-of-seconds-passed-from-the-midnight-with-java
        ZonedDateTime nowZoned = ZonedDateTime.now();
        Instant midnight = nowZoned.toLocalDate().atStartOfDay(nowZoned.getZone()).toInstant();
        Duration duration = Duration.between(midnight, Instant.now());
        return duration.getSeconds();
    }
    
    @Override
    public boolean in(World world)
    {
        long secondsSinceMidnight = getSecondsSinceMidnight();
        return (secondsSinceMidnight >= from && secondsSinceMidnight <= to);
    }
}
