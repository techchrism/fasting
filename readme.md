A plugin that disables eating/drinking during certain time ranges

Permissions:
 - fasting.cmd.reload\
   Permission to reload the config
 - fasting.cmd.check\
   Permission to check if a player can eat/drink

Commands:
 - /fasting reload or /fast reload\
   Reloads the config
 - /fasting check (world) or /fast check (world)\
   Checks if a player can eat in the current world or the specified world

Config:
```yaml
# Worlds to enable the plugin in
enabled-worlds:
  - world
# If this is true, the plugin will work in all worlds EXCEPT for those in "enabled-worlds"
enabled-to-disabled: false

# If this is true, the plugin will prevent eating during disabled ranges
prevent-eating: true
# If this is true, the plugin will prevent drinking during disabled ranges
prevent-drinking: true

# World time ranges to disable eating/drinking
# Times are in ticks from 0 to 24000
# Two ranges are needed if the time range goes over sunrise
disabled-world-times:
  - '13000-23000'

# Server time ranges to disable eating/drinking
# Times are in hours:minutes from 00:00 to 24:00
# Two ranges are needed if the time range goes over midnight
disabled-server-times:
# - '21:00-24:00'
# - '00:00-6:00'

# Message to send to the player if they are in a disabled range
# If this is an empty string (''), the player won't be sent anything
disabled-message: '&cYou cannot eat/drink during this time!'
```