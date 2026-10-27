# Well-Stocked Villagers

A server-side Fabric mod that lets every villager trade be used 12 times before it locks, and lets you raise how often villagers restock their trades, from vanilla's 2 times per day up to 10.

## Features

- **12 uses per trade.** This applies to every villager trade, including trades that have 3 or 16 uses in vanilla. Villagers that already exist in your world get the new limit the next time their trades are used, for example when a player opens the trading screen or the villager works at its workstation.
- **2–10 restocks per day.** The default is 2, the same as vanilla. Increase it with `/wsv restocks <2–10>`.
- **Vanilla restocking.** Villagers still restock by working at their workstation during their work hours, and only when at least one trade has been used. The minimum time between restocks is calculated from the number of restocks per day, so that all of them fit within the villager's work hours. It is never longer than vanilla's 2400 ticks.

## Commands

| Command | Permission | Description |
|---|---|---|
| `/wsv restocks` | Everyone | Shows the current number of restocks per day. |
| `/wsv restocks <2–10>` | Operators (level 2) | Sets the number of restocks per day. The change takes effect immediately and is saved to the config file. |

## Configuration

`config/wellstockedvillagers.json` is created with default values the first time the game or server starts:

```json
{
  "maxUses": 12,
  "restocksPerDay": 2
}
```

| Setting | Default | Description |
|---|---|---|
| `maxUses` | `12` | How many times each villager trade can be used before it locks. Minimum 1. |
| `restocksPerDay` | `2` | How many times per day a villager can restock. Clamped to 2–10. |

The file is read at startup, so changes made by editing it take effect after a restart. `/wsv restocks` changes the restock setting without a restart.

## Requirements

- Minecraft 26.3
- Fabric Loader 0.20.1 or newer
- Fabric API

## Server-side

The mod only needs to be installed on the server. Players can join with a vanilla client. It also works in singleplayer.

## Compatibility

- Wandering traders are not affected.
- Avoid using this mod together with other mods that change how villagers restock, such as Economical Villager Trading. They are likely to conflict.

## License

[MIT](LICENSE)
