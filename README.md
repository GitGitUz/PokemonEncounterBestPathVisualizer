This JavaFX program seeks to visualize the best path a character can take to avoid an encounter from a source to a goal in the Pokemon world, using Dijkstra's algorithm.
In the Pokemon games, tiles the character steps on have a chance of generating an encounter with a Pokemon, unless there is a global event blocking wild encounters 
(ie. story events, safe zones). For areas that can generate wild encounters, different terrain types have base probability rates. For example , Tall Grass terrain has a higher probability of triggering a wild encounter compared to Short Grass. 

In this program, the user can create their own map from a preset of terrain types and also add walls, allowing for a wide variety of map configurations.
Once a user searches for the best path, the program will highlight every tile on the best path the character can take to minimize the chance of an encounter. 
The length of the path and encounter probability (percentage) are also displayed for every search.

NOTE: this program does not guarantee a character will be able to completely avoid an encounter (not possible in the wild without modifiers such as Repels) but only minimizes the path it needs to take to get to the goal. 
