This JavaFX program seeks to visualize the best path a character can take to avoid an encounter from a source to a goal in the Pokemon world, using Dijkstra's algorithm.
In the Pokemon games, tiles the character steps on have a chance of generating an encounter with a Pokemon, unless there is a global event blocking wild encounters 
(ie. story events, safe zones). For areas that can generate wild encounters, different terrain types have base probability rates. For example , Tall Grass terrain has a higher probability of triggering a wild encounter compared to Short Grass. 

In this program, the user can create their own map from a preset of terrain types and also add walls, allowing for a wide variety of map configurations.
Once a user searches for the best path, the program will highlight every tile on the best path the character can take to minimize the chance of an encounter. 
The length of the path and encounter probability (percentage) are also displayed for every search.

NOTE: this program does not guarantee a character will be able to completely avoid an encounter (not possible in the wild without modifiers such as Repels) but only minimizes the path it needs to take to get to the goal. 

Images:

![1](https://user-images.githubusercontent.com/22176656/182719131-0a1fdf2c-cc15-4b47-9e1d-46f8c5ce2b02.png)
![2](https://user-images.githubusercontent.com/22176656/182719133-1b262395-be72-441b-81ee-37ef9160a980.png)
![3](https://user-images.githubusercontent.com/22176656/182719136-acb59bba-5705-49f7-be7c-fa91a507787b.png)
![4](https://user-images.githubusercontent.com/22176656/182719140-b5d7424c-9fe0-4721-bbb4-1e8845e673fa.png)
