# Better Shulker Boxes _Rewritten_

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5d6138bdec574a39a22286567d92256a)](https://app.codacy.com/gh/lMartin3/BetterShulkerBoxesRewritten?utm_source=github.com&utm_medium=referral&utm_content=lMartin3/BetterShulkerBoxesRewritten&utm_campaign=Badge_Grade_Settings)

Better Shulker Boxes is a Spigot plugin for Minecraft that allows
players to open shulker boxes more easily, by right-clicking while holding them
or just right-clicking them in the inventory.

The plugin is 100% customizable, you can change or disable the
shulker open/close sounds and messages.

This project currently supports Spigot and PaperSpigot versions 1.13 and above,
only the latest builds are supported. The plugin will most probably work on custom forks,
but it is not guaranteed.

For more information, please visit the [Spigot resource page](https://www.spigotmc.org/resources/bsb-better-shulker-boxes-1-12-1-16.58837/).

## Installation
1- Download or compile the JAR<br>
2- Drop the JAR in your server's plugin folder<br>
3- Restart your server<br>

##Configuration
You can tweak the plugin's settings in the config.yml file, it will be generated
once the plugin runs for the first time, and it should be located in /plugins/BetterShulkerBoxes.<br>
You can go to [this link](https://github.com/lMartin3/BetterShulkerBoxesRewritten/blob/master/src/main/resources/config.yml)
to see the default configuration settings, every option has an explanation of what it does.

## Permissions
| Permission                        | Description                                                                                                                                                                               |
|-----------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| bettershulkerboxes.use            | Allows the player to use the plugin's features                                                                                                                                            |
| bettershulkerboxes.admin          | Allows the player to reload the plugin's configuration and check for updates. Players with this permission will also get alerted if a new version is available when they join the server. |
| bettershulkerboxes.bypasscooldown | Allows the player to bypass the configured cooldown                                                                                                                                       |
| bettershulkerboxes.*              | Grants all permissions                                                                                                                                                                    |

## Commands
| Command     | Description              |
|-------------|--------------------------|
| /bsb reload | Reload the configuration |
| /bsb check  | Check for updates        |

## Contact info
Discord: [link to server](https://discord.gg/DnAHWMG) <br>
SpigotMC Forums: [Rektb](https://www.spigotmc.org/members/rektb.384908/) <br>
GitHub: [Issues page](https://github.com/lMartin3/BetterShulkerBoxesRewritten/issues)

## FAQ

### I found a bug, what should I do?
If you find a bug, please open an issue on GitHub or reach out to me via Discord or Spigot DM.

### I have a feature suggestion
Please open a GitHub issue or reach out to me.

### Can I submit a translation?
Of course! Please contact me.

### Where can I download the plugin from?
Download it from the [Spigot resource page](https://www.spigotmc.org/resources/bsb-better-shulker-boxes-1-12-1-16.58837/),
this repository or compile it yourself.
For security reasons, please avoid downloading the plugin from any other site.

### Can I fork/modify your plugin?
Yes, you can, as long as you comply with the license (AGPLv3)
