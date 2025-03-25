# PlayerNameTags
 This plugin allows Server Operators to change any player's name tag. This change is visible on Tab list, chat and above the player's head.

---
**Commands:**
- `/nametag <get> <name|prefix|suffix> <player>` to lookup a player's name, prefix or suffix.
- `/nametag <set> <name|prefix|suffix> <player> <text>` to set a player's name, prefix or suffix to "text".
- `/nametag <reset> <name|prefix|suffix> [player]` to reset a player's name, prefix or suffix to its default state. Default for prefixes and suffixes is an empty string and default for name is the player's IGN.
- `/nametag <toggle> [player]` to toggle on/off a player's nametag. This will simply make the nametag disappear and not restore the vanilla name tag.

- `/pntconfig` to update the plugin's config in runtime. The command itself suggests all possibilities when typing which makes it very easy to use.

**Config:**
- `enable-plugin` toggles the plugin on/off. This is also toggleable in runtime using `/config set enable-plguin <true|false>` and some other ways.
- `valid-name-characters` is a string containing all characters that can be used to make a name (spaces can also be used).
- `max-name-length` determines the maximum length a name can have.
- `max-prefix-length` determines the maximum length a prefix can have.
- `max-suffix-length` determines the maximum length a suffix can have.
- `enable-colors` determines if colors are allowed in the name tag.
- `enable-formatting` determines if the font of the text can be modified, i.e. bold, italic, underlined, etc. See https://minecraft.wiki/w/Formatting_codes for more info.
- `game-masters` meant to be for a mini game I was helping design which ended up being cancelled. It is deprecated as this branch will eventually be generalized for public use and become the main branch.

**Notes:**
This plugin has been tested, but not nearly enough to fully confirm it has no bugs.
While I have tried significantly hard to ensure it has as little bugs as possible, it is common that the latest commit isn't fully tested.
