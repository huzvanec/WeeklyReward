# **Weekly Reward**

## **Table of contents**

[What is Weekly Reward?](#what-is-weekly-reward)

[What platforms are supported?](#what-platforms-are-supported)

[Configuration](#configuration)

[How it works?](#how-it-works)

[Build](#Build)

## **What is Weekly Reward?**

It's a simple minecraft plugin for giving custom rewards to players every week.

## **What platforms are supported?**

Paper based minecraft servers 1.19.4+<br>
**Spigot and Bukkit is not supported!**

## **Configuration**

All the configuration is in `WeeklyReward/config.yml`<br>
[Default config.yml](https://github.com/Mandlemankiller/WeeklyReward/blob/master/src/main/resources/config.yml)

### Rewards:

```yml
    { item: <item_name>, amount: <amount>, chance: <chance> }
```
For example:

```yml
rewards:
  - { item: diamond, amount: 20, chance: 1 }
  - { item: emerald, amount: 10, chance: 5 } # Five times more common than diamonds
  - { item: iron_ingot, amount: 128, chance: 5 } # Same chance as emeralds
```

The chance works based on **`bigger number => bigger chance of gaining`**

To apply your configuration, restart your server or use ```/weeklyreward reload```

## **How it works?**

### Reward check

When player logs on the server, the plugin checks the last week number in the year, the player logged on and got the
reward (read from the file `WeeklyReward/reward-log.yml`, **I do not recommend writing in this file manually**). If the
current week number is different from the saved one, it gives the player his random reward and overwrites the week
number in weekslog.yml.

### Random

The "chance number" is actually a number of times the item will be added to a list from which it will be randomly
chosen. That means if we create:

````yml
rewards:
  - { item: diamond, amount: 3, chance: 1 }
  - { item: emerald, amount: 20, chance: 5 }
````

It will create a list with **1× 3 diamonds** and **5× 20 emeralds** like this:

````yml
3× diamond
20× emerald
20× emerald
20× emerald
20× emerald
20× emerald
````

... and then it chooses random one of these (that means we have 5× more chance of receiving emeralds, than diamonds).

## Build
Requirements: [Git](https://git-scm.com/), [Maven](https://maven.apache.org/)<br>
Clone the repository
```bash
git clone https://github.com/Mandlemankiller/WeeklyReward.git
```
Move to the WeeklyReward folder
```bash
cd WeeklyReward
```
Package with Maven
```bash
mvn package
```
Done! The built jar is now located in ```target``` directory, it's called ```WeeklyReward-1.0-SNAPSHOT.jar```