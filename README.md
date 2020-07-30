
# Welcome to TopTeams API
Using [Google Functions](https://cloud.google.com/functions) and [Google Cloud Run](https://cloud.google.com/run), the module TopTeams API allows you to execute different functions in order to **optimize your team sport activities**.

## Swagger
https://app.swaggerhub.com/apis-docs/TopTeams/TopTeamsAPIv3/1.2.0
(Thanks Alex Faria !)

## Team composition generator
**Generate balanced teams** from players, each of them identified by a unique id and having an individual rating value between 0 & 100, representing his level. 

### Several ways to generate compositions

#### Classic way (ratings only)

Giving a list of players and their ratings, you will be able to get a simple balanced composition 
of equal teams.
<details>
  <summary>See json</summary>
   <p>
   
```
{
    "availablePlayers":[
   {
      "id":"David",
      "rating":54
   },
   {
      "id":"Bilal",
      "rating":77
   },
   {
      ...
   }
  ]
}
```

</p>
</details>

#### Adding positions (ratings + positions)

If pitch positions and profiles are important to you, you can additionally define a position to several players. 
The engine will take this info into account and first split players regarding their position, then regarding their level.

<details>
  <summary>See json</summary>
   <p>
   
```
{
    "availablePlayers":[
   {
      "id":"Joachim",
      "rating":44.5,
      "position": DEF
   },
   {
      "id":"Rafik",
      "rating":74,
      "position": ATT
   },
   {
      ...
   }
  ]
}
```

   </p>
   </details>
   
#### Filling existing teams (ratings + positions optionnally)

Let's imagine that some players want to play together. No problem! Insert the pre-created teams and 
the non-affected player list. The engine will fill the teams and give you the best possible composition.

<details>
  <summary>See json</summary>
   <p>

```
{
   "team_A":{
      "players":[
         {
            "id":"Benjamin",
            "rating":54,
            "position":"GK"
         },
         {
            "id":"Mario",
            "rating":77
         }
      ]
   },
   "team_B":{
      "players":[]
   },
   "availablePlayers":[
      {
         "id":"Redwan",
         "rating":59,
         "position":"GK"
      },
      {
         "id":"Oscar",
         "rating":81
      },
      {
         ...
      }
   ]
}
```

   </p>
   </details>
   
### Additionnal parameters
| Parameter  | Definition |
| ------------- | ------------- |
| `split_best_players`  | To separate the two best players in each team |
| `split_worst_players`  | To separate the two worst players in each team  |
| `split_goal_keepers`  | To seperate the players with the position GK in each team  |
| `split_defenders`  | To seperate the players with the position DEF in each team  |
| `split_strikers`  | To seperate the players with the position ATT in each team  |
| `expected_team_number`  | The number of teams you would like to generate  |
| `expected_composition_number`  | The number of compositions you woulk like to generate |
| `composition_type`  | `REGULAR`, `SUBSTITUTION` or `ODD` |

### Composition type

- `REGULAR` : teams with same number of players (ex: 5vs5)

- `SUBSTITUTION` : teams with same number of players on the pitch but with substitutes (ex: 5 + 1 sub vs 5)

- `ODD` : teams with different number of players on the pitch (ex: 6vs5)

## Rating updates calculator
**Calculate the player rating modification** to apply after a game from a given composition and a score.
This algorithm takes into account the global level of the two teams, the final goal average, and the number of games played by each player.

<details>
  <summary>See json</summary>
   <p>
   
```
{
   "team_A":{
      "players":[
         {
            "id":"Thomas",
            "rating":54,
            "nb_games_played":31
         },
         {
            "id":"Greg",
            "rating":77,
            "nb_games_played":80
         },
         {
            ...
         }
      ]
   },
   "team_B":{
      "players":[
         {
            "id":"Simon",
            "rating":84,
            "nb_games_played":102
         },
         {
            "id":"Walid",
            "rating":55,
            "nb_games_played":42
         },
         {
             ...
         }
      ]
   }
}
```

   </p>
   </details>
   
### Additionnal parameters
| Parameter  | Definition |
| ------------- | ------------- |
| `score_A`  | score of Team A |
| `score_B`  | score of Team B |
| `relative_distribution`  | `NONE`, `LOW`, `MEDIUM` or `HIGH` |
| `split_points_by_team`  | To split equally the points between the two teams (if false, the points will be splitted globally using the relative_distribution same logic)  |
| `composition_type`  | `REGULAR`, `SUBSTITUTION`, `ODD`  |
| `kf`  | Multiplier coefficient which will increase or decrease the awarded points. Should be >0|

### Relative distribution 

- `HIGH`: the players with less games will be much more impacted than others (positively as negatively)
- `MEDIUM` : less differences than with `HIGH`
- `LOW` : less differences than with `MEDIUM`
- `NONE` : all the players will be affected by the rating modification equally

# Process Summary
![Process Summary](https://i.ibb.co/mhHFccT/process-summary.png)
## Player definition 
- `id` (String) : must be unique
- `rating` (double) : must be between 0 & 100. 

### Recommended values
```
Novice : 40
Beginner : 50
Intermediate : 60
Advance : 70
Professionnal : 80
```
- `position` (enum) : is optionnal.
```
Goal Keeper : GK
Defender : DEF
Attacker : ATT
```
- `nb_games_played` (int) : is optionnal. Only used for *Rating updates calculator* function.

