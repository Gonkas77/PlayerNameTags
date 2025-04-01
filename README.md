This plugin allows a server admin to set any online or offline player's nameplate to whatever they like! It works as if you right clicked a player with a nametag! Unfortunately the plugin does not allow players to be on vanilla teams (`/team`) since in order to hide the actual player nameplate all players must be in a team with the `nametagVisibility` option set to `never`. _You can work around this by setting the new team's `nametagVisibility` to `never` as well._

You can color or format names using the `&` character instead of the usual `§` when using the `/nametag set` command. See more at [Formatting Codes](https://minecraft.wiki/w/Formatting_codes).

All of the commands' syntaxes and the config's parameters are at the bottom.

---

You can set a player's name using the `/nametag` command:


![/nametag <copy|get|reset|set|toggle>](https://cdn.modrinth.com/data/cached_images/4172730a7ec0283318654f58fa0ff2177b8a2529_0.webp)
![/nametag set name Gonkas77 &a&lHi there!](https://cdn.modrinth.com/data/cached_images/8d81b613f909a80473c776b917c65411e997add6_0.webp)
![Result of the previous action is a bold lime nametag above the player's head with the text "Hi there!".](https://cdn.modrinth.com/data/cached_images/f44a0fbcb6171e1086d335d2037deb70b23c96de_0.webp)
![The player's name is also changed in the player list (tab).](https://cdn.modrinth.com/data/cached_images/f4d6397c9e86ee0b64d8093c12e2d77c9e962a1f_0.webp)
![The player's name is also changed in chat.](https://cdn.modrinth.com/data/cached_images/5bb8d51829c9c87f4e58d33d245abed3db9ce79e_0.webp)

---

And you can change the plugin's config during runtime using the `pntconfig` command:


![/pntconfig set enable-colors <false|true>](https://cdn.modrinth.com/data/cached_images/f6c4571fdd359c311bc25dbfca78678b5d03642a_0.webp)
![/pntconfig set max-name-length <length>](https://cdn.modrinth.com/data/cached_images/81dcd7a5a7a88eed901949df6da84fb145409817_0.webp)
![/pntconfig get valid-name-characters](https://cdn.modrinth.com/data/cached_images/0ba76deced78bb57626760af150e805aab58bec2_0.webp)
![/pntconfig preset valid-name-characters <preset>](https://cdn.modrinth.com/data/cached_images/bff61405a7f8480e49876d17fe8a6420fa9e2aff_0.webp)

---

### Notes:
- Parameters surrounded by `<>` are mandatory command arguments
- Parameters surrounded by `[]` are optional command arguments
- `|` means "or". So, `<get|set>` means you can input `get` or `set`.

### Nametag command syntax:
- `/nametag <copy|get|reset|set|toggle>` are the sub-commands
- `/nametag copy <name|prefix|suffix> <source_player> <target_player>` will copy `source_player`'s name, prefix, or suffix onto `target_player`.
- `/nametag get <name|prefix|suffix> [player]` will send the person who executed the command the `player`'s name, prefix, or suffix. If this command is executed without the `player` argument it will assume the `player` argument as the one who executed the command.
- `/nametag reset <name|prefix|suffix> [player]` will reset the `player`'s name, prefix, or suffix. Meaning the prefix and the suffix will be removed and the name will be reset to the player's IGN.
- `/nametag set <name|prefix|suffix> <player> <text>` will set the `player`'s name, prefix, or suffix to `text`. By default, spaces are valid characters, which means anything after `<text>` will be treated as part of it (including the spaces).
- `/nametag toggle [player]` will turn the nametag of `player` on/off. Crouching or going invisible will not affect the nametag's state until toggled back on.

### Config command syntax:
- `/pntconfig <get|preset|reset|set>` are the sub-commands
- `/pntconfig get <config_option>` will send the person who executed the command the value stored for `config_option` in the config file. _The config file can be found in the `plugin` folder `PlayerNameTags`._
- `/pntconfig preset <config_option> <preset>` will set the `config_option`'s value to the `preset` chosen.
- `/pntconfig reset <config_option>` will set the `config_option`'s value to its default one.
- `/pntconfig set <config_option> <value>` will set the `config_option`'s value to the `value` given.

### Config options (in parenthesis are the types of values you can assign to it):
- `enable-colors` allows nameplates to be colored! _(Boolean)_
- `enable-formatting` allows nameplates to be bold, italic, underlined, obfuscated or strokethrough! _(Boolean)_
- `enable-plugin` sets the plugin as enabled or not. You can enable the plugin using the `/pntconfig` command even if this is set to false. _(Boolean)_
- `max-name-length` determines how long a player's **name** can be. Not the entire nametag, just their name. _(Number)_
- `max-prefix-length` determines how long a player's prefix can be. _(Number)_
- `max-suffix-length` determines how long a player's suffix can be. _(Number)_
- `valid-name-characters` determines which characters names, prefixes, and suffixes can be made of. _(Characters)_

---

In the future, I plan on adding "titles" to players above or below their nametags, functioning similarly to a prefix or suffix. Before that, however, I'd have to revamp the plugin which should take some time.

**Feel free to use this plugin on any server.**
