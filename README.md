# **Weekly Reward**
## WARNING!
### This was my first ever java plugin! It contains many bugs and code imperfectins, which I am aware of. It is awaiting a complete makeover. 
## **Table of contents**
[What is Weekly Reward?](#what-is-weekly-reward)

[What platforms are supported?](#what-platforms-are-supported)

[Configuration](#configuration)

[How it works?](#how-it-works)
## **What is Weekly Reward?**
It's a simple minecraft plugin for giving custom rewards to players every week.
## **What platforms are supported?**
Spigot based minecraft servers **1.16+**
Bukkit is **not** and will **not** be supported!
## **Configuration**
All the configuration is in `WeeklyReward/rewards-config.yml`

The configuration is in format:
```yml
    <item_name> <count>:<chance>
```
For example:
```yml
Rewards:
    diamond 20: 1
    emerald 10: 5 #five times more common than diamonds
    iron 128: 5 #same rarity as emeralds
```
The chance works based on **`bigger number => bigger chance of gaining`**

### Default configuration:
```yml
Rewards:
    diamond 1: 15 #the rarest
    emerald 20: 20
    gold_ingot 15: 25
    iron_ingot 25: 30 #the most common
    coal_block 5: 15 #the rarest
```
To apply the configuration, restart your server. **Reloading the server will probably not help!**
## **How it works?**
### Reward check
When player logs on the server, the plugin checks the last week number in the year, the player logged on and got the reward (read from the file `WeeklyReward/weekslog.yml`, **I do not recommend writing in this file manually**). If the current week number is different from the saved one, it gives the player his random reward and overwrites the week number in weekslog.yml.
### Random
The "chance number" is actually a number of times the item will be added to a list from which it will be randomly chosen. That means if we create:
````yml
Rewards:
    diamond 3: 1
    emerald 20: 5
````
It will create a list with **1× 3 diamonds** and **5× 20 emeralds**...
````yml
diamond 3
emerald 20
emerald 20
emerald 20
emerald 20
emerald 20
````
... and then choose random one of these (that means we have 5× more chance of receiving emeralds, than diamonds).
