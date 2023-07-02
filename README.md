```text
 _    __      __       ____                 
| |  / /___  / /____  / __ \____ ___________
| | / / __ \/ __/ _ \/ /_/ / __ `/ ___/ ___/
| |/ / /_/ / /_/  __/ ____/ /_/ (__  |__  ) 
|___/\____/\__/\___/_/    \__,_/____/____/      
```

README LANGUAGES [ [**English**](README.md) | [中文](README.md)  ]

![CodeSize](https://img.shields.io/github/languages/code-size/ArtformGames/VotePass)
[![Download](https://img.shields.io/github/downloads/ArtformGames/VotePass/total)](https://github.com/ArtformGames/VotePass/releases)
[![Java CI with Maven](https://github.com/ArtformGames/VotePass/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/ArtformGames/VotePass/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.8--Latest-green)~~~~

# **VotePass** _(WIP)_

> You have been **"Passed by Vote"** !

An efficient and fair verification plugin for whitelisted servers,
which supports players to submit whitelist requests by themselves
and be reviewed by all players who have passed.

## Features & Advantages

## Commands

The main command is `/VotePass` or `/vp`.

### Lobby side

```text

# request <serverID>
@ Player command
- Submit a whitelist request for a specific server.
- If the server has configured the rules, the rule agreement will be opened first;
- If not, the question-related GUI will be popped up directly for the player to fill in.

# accept <serverID>
@ Player command
- Agree to the rules of the specified server and start filling in the answers to the questions.

# deny <serverID>
@ Player command
- Refuse the rules of the specified server.

# reload
@ Admin command
- Reload the configuration file.

# toggle <server>
@ Admin command
- Enable/Disable the vote application for the corresponding server.

# status <server>
@ Admin command
- Check the status of the corresponding server.
- Including the number of pending applications, the number of passed applications, and the number of failed applications, etc.

```

### Game side

```text

# requests
@ Player command
- View all whitelist requests that have not been participated in.

# handle <requestID>
@ Player command
- (Continue to) process the specified whitelist request.

# admin
@ Admin command
- Open the administrator operation GUI to directly operate the whitelist.

# reload
@ Admin command
- Reload the configuration file.

# sync
@ Admin command
- Synchronize the whitelist of the corresponding server to the database.

# migrate
@ Admin command
- Migrate the whitelist of the server from the "whitelist.json".

# list
@ Admin command
- List all players in whitelist.

# add <uuid> [username]
@ Admin command
- Add a player to the whitelist.

# remove <username/uuid>
@ Admin command
- Remove a player from the whitelist.

```

## Configurations

### Plugin Configuration ([`config.yml`]() .

Will be generated on the first boot up.

### Messages Configuration ([`messages.yml`]())

Will be generated on the first boot up.

### Server Application Configuration ([`servers/*.yml`](lobby/plugin/src/main/resources/servers/survival.yml))

All server's application configuration are separate configuration files,
stored in the lobby's `<Data Folder>/servers/` for easy management.

You can edit the storage path of the server configuration file in the plugin configuration file.

## Permissions

## Statistics

[![bStats](https://bstats.org/signatures/bukkit/VotePass.svg)](https://bstats.org/plugin/bukkit/VotePass/18946)

## Open Source Licence

The source code of this project adopts the [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0).
