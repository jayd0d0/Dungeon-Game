Task 1) Code Analysis and Refactoring ⛏️

a) From DRY to Design Patterns
Links to your merge requests
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/15
i. Look inside src/main/java/dungeonmania/entities/enemies. Where can you notice an instance of repeated code? Note down the particular offending lines/methods/fields.

In Mercenary.java and ZombieToast.java, the move method had instances of repeated code, namely when the player has an invincibility and invisibility potion. 

Also embedded in the invisibility portion of both piece of code, getting the entity to have the greatest displacement in the next move also has repeated code.

ii. What Design Pattern could be used to improve the quality of the code and avoid repetition? Justify your choice by relating the scenario to the key characteristics of your chosen Design Pattern.
Since the repeated code within Mercenary.java and ZombieToast.java is in an if-else structure, using the Strategy Pattern can be used to separate out the difference cases of how move can be performed. It also ensures the reusablity of Movements such as being able to be used in difference classes such as in both Mercenary.java and Zombie.java.

iii. Using your chosen Design Pattern, refactor the code to remove the repetition.

- Create strategy pattern for different movement cases
    - Create MoveStrategy interface to get nextPosition
    - 4 if-elses = 4 strategies
- Ensure the Stratgies are compatible for other entities, not just Mercenary for extensiblity.
- Replace the portions of code with the created strategies 

b) Observer Pattern
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/14
Identify one place where the Observer Pattern is present in the codebase, and outline how the implementation relates to the key characteristics of the Observer Pattern.

The switch class serves as the subject in this implementation. It is responsible for maintaining a list of observers (subscribed Bomb objects) and notifying if the switch is activated. Bomb objects can subscribe to a Switch using the subscribe(Bomb bomb, GameMap map) method, and they can unsubscribe using the unsubscribe(Bomb b) method. This makes the subject and observers loosely coupled a characteristic of the Observer Pattern. 

The Bomb class is the observer in this implementation. It is interested in changes in the state of the Switch objects. When a Switch is activated (usually when a Boulder overlaps with it), the Bomb objects need to respond to this event by explode(map).

c) Inheritance Design
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/6

i. Name the code smell present in the above code. Identify all subclasses of Entity which have similar code smells that point towards the same root cause.
The code smell present in the above code is dispensables but to be more specific dead code. Boulder.java, door.java, entity.java, player.java, portal.java, switch.java, wall.java, buildable.java, arrow.java, bomb.java, key.java, sword.java, treasure.java, wood.java, potion.java, enemy.java and zombietoastspawner.java. These are all the subclasses of Entity which have similar code smells. 

ii. Redesign the inheritance structure to solve the problem, in doing so remove the smells.
In order to solve the problem, I must first change the three methods that is within Entity.java from being an abstract class method to a class method so that subclasses do not have to always inherit these methods. I will then remove all of the similar code in subclasses. 


d) More Code Smells
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/8
// NOTE: Another change was made after this merge request // 
i. What design smell is present in the above description?
The code smell present in the description is Shotgun Surgery. Shotgun Surgery occurs multiple changes are required in different places when making a single kind of change.

ii. Refactor the code to resolve the smell and underlying problem causing it.
In order to resolve the smell, we will incorporate Extract Superclass refactoring technique which is done by creating a new shared superclass and moving all of the identical methods into it. (The identical method being canMoveOnto method and onOverlap method). The subclasses will now extend from the new shared superclass (Collectable.java).

e) Open-Closed Goals
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/5

i. Do you think the design is of good quality here? Do you think it complies with the open-closed principle? Do you think the design should be changed?

I don't think the design is of good quality because the design causes the code to be not maintainable and extendable since the code only allows specific goals to have proper behaviour that related to the goal. If there is a new goal, the behaviour of the method needs to be modified to accomadate for that new goal. This in turn does not comply with the open-closed principle since entities should be open for extension, but closed for modification. In Goal.java, the achieve() and toString() method use a 'type' of goal for certain goals but when a new goal is introduced, the method will need to be modified which changes exisitng behavior, violating the open-closed principle. Similarly in GoalFactory.java, the string "goal" in createGoal may not be the expected "exit", "AND", "OR", "boulders" and "treasure". Adding new behaviour with a new case will result in modifications on the method.

Since games are always changing and require updates to keep them fresh, it is necessary for new goals to be created to maintain engagement, the design of goal should be changed. This is also evident in  Task 2a) where a new goal is introduced.

ii. If you think the design is sufficient as it is, justify your decision. If you think the answer is no, pick a suitable Design Pattern that would improve the quality of the code and refactor the code accordingly.
Plan:
- Combination of Strategy Pattern to separate the different conditions/goals in Goal.java and Composite Pattern for the Boolean goals
- New classes for each goal (And, Or, Boulders, Treasuer, Exit)
- Goal will be an interface because Goal isn't storing any fields
- GoalFactory can remain relatively the same since it implements the Factory Pattern and any additional goals can be easily added/accounted for here via cases.


f) Open Refactoring
Merge Request 1
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/9
By carefully looking through all the java files I searched for violations of Law of Demeter. Violations consisted of hgih coupling so to resolve this, all methods that had feature envy were moved to the class it was created on.

Merge Request 2
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/12
- Buildable entities need to be built through a factory so that new craftable items can be easily completed by just adjusting
- Buildable property class can be used to store durability and defence as well as a new property if there is one that is added in the future.
- Durability is common across both Bow and Shield so durability and its usages can be moved to their superclass Buildable 

Merge Request 3
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/13
As a result of the ineffective use of the state pattern, it became unorganised and hard to understand. In order to reduce the complexity of the design, I had removed the redundant classes and introduced new methods to encapsulate the state transitions in a single class. This had eliminated code duplication present in the other classes and simplified the design. 


Task 2) Evolution of Requirements 👽


a) Microevolution - Enemy Goal
v1: https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/17
v2: https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/18
Design
- Creating the EnemiesGoal file implementing Goal within the Goals folder
- Similar to other goal files, will include achieved and toString methods with the goal of "Destroying a certain number of enemies (or more) AND all spawners;"
- The goal:
    - count of enemy kills must be over the kill goal (enemy goal)
    - count of spawners must be equal to 0
    - In order to find the number of enemies killed, we can use the battle method within Game.java and whenever an enemy is destroyed we can increase the number.
    - Within the Game.java, we will also collect the no. of kills using a getter.
    - In order to destroy the spawner, interact() method will be altered to remove the spawner from the map. 
Changes after review
- Added more complex tests and removed unecessary test
- Changing my toString method in EnemyGoal.java to be more concise. 
- added files to frontend to do further testing

Test list
- achieving enemy goal (no spawner)
- failing enemy goal
- achieving enemy goal with a spawner/enemies
- achieving enemy goal and exit goal
- achieving enemy goal or treasure goal (Chose treasure goal)
Other notes
[Any other notes]

Choice 1 - Swamp Tile
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/16
Assumptions
- There will be only 1 swamp tile in the same grid.
- Normal tiles and entities won't have a movement factor so they won't slow down so they have a default movement factor of 0 
- When intialising a game, an enemy will never spawn on top of a swamp tile
Design
- Create a SwampTile class which extends entity
- Create a movementFactor for the swamp tile
- Create test for a Swamp tile entity
- Update player and enemy movement to cater for the swamp tile
    - Have a turnStopper to keep track of how many ticks a hostile enemy is stuck for.
Changes after review
[Design review/Changes made]
Test list
- Player does NOT get slowed down
- Hostile Mercenary gets slowed down
- Allied Mercenary does NOT get slowed down
- Spider gets slowed down
Other notes
[Any other notes]

Choice 2 - Sun Stone & More Buildables
SunStone merge: https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/19
SunStone Test cases: https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/20
Sceptre merge: https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/23
Midnight Armour Testing Merge: https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/22
Midnight Armour Implementation Merge: https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/24
Assumptions
[Any assumptions made]
Design
-=Sunstone=- 
- Sunstone will extend and be a sub-class of collectables hence is able to be picked up by a player
- Sunstone can be used a key for a door as specified, so we must make sure we either have a key or a sunstone in our inventory to open the door.
- Sunstone is now also apart of the "treasure" goal so whenever sunstone is picked up it will be incremented in our collectTreasureCount.
- Sunstone cannot be used to bribe a mercenary so no changes are made. 
-=Sceptre=-
- Create new Sceptre buildable, extending Buildable
- Sceptre only mind controls enemies - namely in Mercernaries, if and only if the player has a sceptre using interact()
- An enemy can be interacted with, if Player has a sceptre using isInteractable() in Mercenary.java
- Added new criteria to craft a sceptre in BuildableFactory.java
- Expanded on Buildable property incorporating the sceptre mindcontrolduration property
-=Midnight Armour=-
- Created a MidnightArmour buildable which is also a subclass of Buildable.java
- Also extended BuildableFactory for a MidnightArmour buildable
- Also expanded on BuildableProperty.java to include midnight_armour_attack and midnight_armour_defence
- Armour crafting implemented
- Game will produce an error when trying to craft midnight armour if there are zomebies in the dungeon seen in Game.java.
- Added criteria if there are noZombies in the dungeon using Player.java 
Test list - SunStone
- test if we can pick up sunstone
- test if sunstone is kept after opening doors
- test if picking up sunstone count towards treasure goal
- test stunstone cannot be used to bribe a mercenary 
Test list - Sceptre
- testing if a single sunstone can create a sceptre
- testing if two sunstone (replace treasure) can create a sceptre
- testing if sceptre controls mercenary

Test list - MidnightArmour
- testing if midnight armour can be crafted with a sunstone while there are no zombies
- testing fail for midnight armour crafting with zombie
Other notes
[Any other notes]


Task 3) Investigation Task ⁉️
Merge Request 1
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/17
Within the spec, it says that the method interact() "Interacts with a mercenary (where the Player bribes the mercenary) or a zombie spawner, where the Player destroys the spawner." However in ZombieTest.java (the MVP test provided), when the method interact() is used on line 170 it is cardinally adjacent and has a sword and valid id but the next line says that it expected the spawner to still be there. Hence, it does not satisfy the requirements and I changed it to be assertTrue when it is 0 meaning that the zombie spawner is no longer there as it was destroyed. 

Merge Request 2: 2 CHANGES 
https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/H14A_CATERPIE/assignment-ii/-/merge_requests/22
Within the spec, it says that the bribe radius is "formed by the diagonally and cardinally adjacent cells".  However, the bribe method within mercenary.java does not take in  to account of this as it just checks if the bribeRadius >= 1. Hence, it does not satisfy the requirements and I have changed it so that the horizontal distance and vertical distance between the player and the mercenary must be less than bribeRadius. 

When I had refactored this, a test failed in MercenaryTest.java on line 136 it says that it does not throw an error as it attempts to bribe the merc. However, it should as the bribe radius is 2, but the horizontal distance between them is 5 which is not in the bribe radius and hence it should fail instead. Hence, I changed this to throw an error instead. 

