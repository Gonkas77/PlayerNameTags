# PlayerNameTags
 This plugin allows Server Operators to change any player's name. This change is visible on Tab list, chat and above the player's head.

---
**Commands:**
- `/name <player> <name>` to change a player's name.
- `/removename [player]` to remove a player's nickname.

**Config:**
- `valid-name-characters` is a string containing all characters that can be used to make a name (spaces can also be used).
- `max-name-length` determines the maximum length a name can have.

**Notes:**
This plugin has been tested, but not nearly enough to fully confirm it has no bugs.
One of the more common bugs would be the name appearing significantly higher than the player,
which can be fixed by simply relogging.