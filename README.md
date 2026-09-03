# ModDetector

A client-side mod detection plugin for Minecraft Paper servers.

ModDetector detects installed client mods by displaying virtual signs to players and analyzing the information returned by the client.

## Features

* Client mod detection
* Warmup checks
* Sequential mod checks
* Randomized Warmup checks
* Action execution when a mod is detected
* Check-only mode without executing actions
* Player-specific checks
* Configurable check delay
* Action placeholders

## Commands

### `/moddetector check`

Starts a normal mod check.

Actions configured for detected mods will be executed.

### `/moddetector checkonly`

Starts a mod check without executing actions.

This is useful when you only want to detect installed mods without taking action against the player.

### Player-specific checks

```text
/moddetector check <player>
/moddetector checkonly <player>
```

Checks the specified player.

### `/moddetector reload`

Reloads the plugin configuration.

### `/moddetector opBypass`

Manages the OP bypass setting.

## Check Flow

A normal check follows this flow:

```text
Start
  ↓
Warmup
  ↓
Warmup
  ↓
CheckSign
  ↓
Next Mod
  ↓
CheckSign
  ↓
...
  ↓
Finish
```

During the Warmup phase, a mod is randomly selected from the registered mods.

During the CheckSign phase, registered mods are checked sequentially.

## Check Only

The `checkonly` command performs mod detection without executing any actions.

```text
/moddetector checkonly
```

This mode only performs the detection process. Actions are not executed when a mod is detected.

## Configuration

Example configuration:

```yaml
warmup: 3
delay: 20

mods:
  freelook:
    name: "FreeLook"
    key: "freelook.key.activate"
    mode: "console"
    action: "kick $player$ You have been kicked for using $mod$."
```

### General Settings

| Option   | Description                                                            |
| -------- | ---------------------------------------------------------------------- |
| `warmup` | Number of random Warmup checks performed before the actual mod checks. |
| `delay`  | Delay between checks, in ticks.                                        |

### Mod Settings

Each mod can be registered under the `mods` section.

```yaml
mods:
  freelook:
    name: "FreeLook"
    key: "freelook.key.activate"
    mode: "console"
    action: "kick $player$ You have been kicked for using $mod$."
```

| Option   | Description                                            |
| -------- | ------------------------------------------------------ |
| `name`   | Display name of the mod.                               |
| `key`    | Translation key used to detect the mod.                |
| `mode`   | Determines how the action is executed.                 |
| `action` | Command or action to execute when the mod is detected. |

### Action Modes

The `mode` option determines how the configured action is executed.

| Mode      | Description                                |
| --------- | ------------------------------------------ |
| `console` | Executes the action as the server console. |
| `player`  | Executes the action as the checked player. |
| `none`    | Does not execute an action.                |

Example:

```yaml
mods:
  freelook:
    name: "FreeLook"
    key: "freelook.key.activate"
    mode: "console"
    action: "kick $player$ You have been kicked for using $mod$."
```

When `mode` is set to `none`, no action is executed when the mod is detected.

### Action Placeholders

Actions support the following placeholders:

| Placeholder | Description               |
| ----------- | ------------------------- |
| `$player$`  | The player being checked. |
| `$mod$`     | The detected mod.         |
| `$mods$`    | The detected mods.        |

Example:

```yaml
action: "kick $player$ You have been kicked for using $mod$."
```

## Requirements

* [Minecraft Java Edition](https://www.minecraft.net/)
* [Paper 1.21.8](https://fill-ui.papermc.io/projects/paper/version/1.21.8)
* [Java 21+](https://www.azul.com/downloads/?version=java-21-lts&os=windows&package=jdk#zulu)
* [PacketEvents API](https://github.com/retrooper/packetevents)
* [mixplus Paper Core](https://github.com/mixplus-main/mixplus-papercore/)