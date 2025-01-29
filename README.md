# AntiP2W Tools

## About

AntiP2W Tools is a rolling release addon for Meteor Client that provides utilities against pay to win servers.

This README is still WIP, it will be completed later.

## Dependencies

- Minecraft (1.21.4)
- Fabric API (any)
- Meteor Client (0.6.0-SNAPSHOT)

## Features

### Commands

- `.foreach`: Explained below

---

**Definitions**:

**Time range**:
- Minimum time, see time syntax below
- Optional ".." and maximum time. If not given the minimum time will be the maximum too.

Examples for time span: 3..5, 3s..5s, 60t..100t, 4, 4s, 80t, 0

**Time syntax**:
- Time: integer
- Optional time unit: "t" for ticks or "s" for seconds. Defaults to seconds.

**Parameters**:
- \<iterations\> \<delayBetweenCommands\> \<command\>:
- \<iterations\> \<command\>: Runs a command multiple times. If the command contains %iteration%, it will be replaced with the current iteration.
- players \<delayBetweenCommands\> \<command\>:
- players \<command\>: Runs a command for every player on the server. If the command contains %player%, it will be replaced with the player's name.

DelayBetweenCommands is a time range because the implementation supports random delays.

---

- `.hologram`, `.holo`: Loads an image into the world. (requires creative mode)
- `.loverfella-dupe`, `.lf-dupe`: Does the Loverfella dupe.
- `.offhand-crash`: Attempts to crash the server by swapping your offhand. Requires many players nearby and an item with a big size. (bytes)
- `.purpur-crash`, `.funny-crash`: Sends custom payload packets that causes the server to generate chunks at random locations. Only works on 1.18.2 and below.
- `.reconnect`, `.rejoin`: Reconnects to the server you are playing on, optionally with another name.

### Modules

- Anti Exploit: Attempts to block packets that mess up the game.
- Auto Auth: Automatically authenticates you on server join where supported.
- Better Macros: Allows macros to be used while a screen is open.
- Better Toasts: Makes toasts more configurable.
- Book Colors: Replaces "\&" with the color code chars in books.
- Free Interact: Removes interaction limits.

## Legal

This addon uses [textures from Mojang](https://www.minecraft.net/en-us/usage-guidelines).

This addon is not approved by or associated with Mojang or Microsoft.
