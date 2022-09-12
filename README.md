[![Codacy Badge](https://app.codacy.com/project/badge/Grade/527a4c96fc3b4d3b8298769bce48071d)](https://www.codacy.com/gh/lMartin3/BetterShulkerBoxesRewritten/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=lMartin3/BetterShulkerBoxesRewritten&amp;utm_campaign=Badge_Grade)
# Better Shulker Boxes _Rewritten_
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

## Configuration
Tweak the plugin's settings in the config.yml file, it will be generated
once the plugin runs for the first time, and it should be located in /plugins/BetterShulkerBoxes.<br>
Go to [this link](https://github.com/lMartin3/BetterShulkerBoxesRewritten/blob/master/src/main/resources/config.yml)
to see the default configuration settings, every option has an explanation of what it does.

## Translations
To see all the available translations, please go to the [translations folder](https://github.com/lMartin3/BetterShulkerBoxesRewritten/tree/master/translations). <br>
Available translations:
-  [English](https://github.com/lMartin3/BetterShulkerBoxesRewritten/blob/master/translations/en_us.yml)
-  [Spanish](https://github.com/lMartin3/BetterShulkerBoxesRewritten/blob/master/translations/es_es.yml)
-  [Simplified Chinese](https://github.com/lMartin3/BetterShulkerBoxesRewritten/blob/master/translations/zh_cn.yml) (by [@YuanYuanOwO](https://github.com/YuanYuanOwO))
-  [Traditional Chinese](https://github.com/lMartin3/BetterShulkerBoxesRewritten/blob/master/translations/zh_tw.yml) (by [@jack444410](https://github.com/jack444410))
-  [German](https://github.com/lMartin3/BetterShulkerBoxesRewritten/blob/master/translations/de_DE.yml) (by [@Hutch79](https://github.com/Hutch79))
-  [Italian](https://github.com/lMartin3/BetterShulkerBoxesRewritten/blob/master/translations/it_it.yml) (by [@iVillager](https://github.com/ivillager))
-  [Bulgarian](https://github.com/lMartin3/BetterShulkerBoxesRewritten/blob/master/translations/bu_bu.yml) (by [@ItzLazerX](https://github.com/ItzLazerX))
-  [French](https://github.com/lMartin3/BetterShulkerBoxesRewritten/blob/master/translations/fr_fr.yml) (by MrQuentin#0309)
-  [Czech](https://github.com/lMartin3/BetterShulkerBoxesRewritten/blob/master/translations/cz_cz.yml) (by [lOlkOmOx](https://github.com/lOlkOmOx))
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

## Using hex colors, gradients, rainbows and more
BetterShulkerBoxes supports regular color encodings (`& + letter`) as well as hex colors. These can be used
with xml tags,

| Pattern                                              | Tag                        | Example                                          |
|------------------------------------------------------|----------------------------|--------------------------------------------------|
| Fixed hex color                                      | `<#COLOR>`                 | `<#FF0000>This is red`                           |
| Color gradient (2 hex values)<br/>(Currently broken) | `<g#STARTCOLOR:#ENDCOLOR>` | `<g#FF0000:#00FF00> this goes from red to green` |
| Rainbow                                              | `\<r>`                     | `\<r>This is a rainbow `                         |
| Stop pattern                                         | `\<r> or \<g> `            | `<#FFFFFF>This is white\<r> and this is not`     |
## Contact info
Discord: [link to server](https://discord.gg/DnAHWMG) <br>
SpigotMC Forums: [Rektb](https://www.spigotmc.org/members/rektb.384908/) <br>
GitHub: [Issues page](https://github.com/lMartin3/BetterShulkerBoxesRewritten/issues)

## FAQ

### How do I disable the shulker box messages?
Set the open and close messages to empty quotes, like this:
```yaml
open_message: ''
close_message: ''
```

### I found a bug, what should I do?
If 
find a bug, please open an issue on GitHub or reach out to me via Discord or Spigot DM.

### I have a feature suggestion
Please open a GitHub issue or reach out to me.

### Can I submit a translation?
Of course! Please open a pull request or contact me

### Where can I download the plugin from?
Download it from the [Spigot resource page](https://www.spigotmc.org/resources/bsb-better-shulker-boxes-1-12-1-16.58837/),
this repository or compile it yourself.
For security reasons, please avoid downloading the plugin from any other site.

### Can I fork/modify your plugin?
Yes, you can, as long as you comply with the license (AGPLv3)
