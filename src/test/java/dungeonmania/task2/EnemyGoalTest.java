package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class EnemyGoalTest {
    @Test
    @DisplayName("Test fail basic enemy goal")
    public void testFailEnemyGoal() {
        //2 |__  __  __  __  __ __ //
        //1 |__  MC  __  MC  __ PL //
        //0 |__  __  __  __  __ __ //
        //    0   1   2   3   4  5 //
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicEnemyGoalTest", "c_EnemyGoalsTest");

        // no enemies are killed, hence failing
        assertEquals(":enemies", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Test a basic enemy goal with no spawner")
    public void testEnemyGoalNoSpawner() {
        //2 |__  __  __  __  __ __ //
        //1 |__  MC  __  MC  __ PL //
        //0 |__  __  __  __  __ __ //
        //    0   1   2   3   4  5 //
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicEnemyGoalTest", "c_EnemyGoalsTest");

        // move player to left, removing 1 enemy
        res = dmc.tick(Direction.LEFT);
        // assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player left again, removing last enemy
        res = dmc.tick(Direction.LEFT);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Test a basic enemy goal with a spawner")
    public void testEnemyGoalSpawner() {
        //2 |__  MC  __  MC  __ SW PL //
        //1 |__  SP  __  __  __ __ __ //
        //0 |__  __  __  __  __ __ __ //
        //    0   1   2   3   4  5  6 //
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicEnemyGoalTest_Spawner", "c_EnemyGoalsTest");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // move player to left to pick up sword
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // move player to left to kill enemy
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, res.getBattles().size());
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player to left again to kill enemy
        res = dmc.tick(Direction.LEFT);
        assertEquals(2, res.getBattles().size());
        assertEquals(1, TestUtils.countType(res, "zombie_toast_spawner"));

        // move to be cardinally adjacent with spawner and d3str0y
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Test enemy goal and returning to exit goal")
    public void testEnemyGoalnExit() {
        //2 |SP  __  __  MC  __ SW PL EX //
        //1 |__  __  __  __  __ __ __ __ //
        //0 |__  __  __  __  __ __ __ __ //
        //    0   1   2   3   4  5  6  7 //
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_Exit", "c_EnemyGoalsTest_Exit");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        // move to exit before battling any enemies
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = dmc.tick(Direction.LEFT);
        // move player to left to pick up sword and battle
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, res.getBattles().size());

        // move to be cardinally adjacent with spawner and d3str0y spawner
        for (int i = 0; i < 4; i++) {
            res = dmc.tick(Direction.LEFT);
        }
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));

        // move back to the exit
        for (int i = 0; i < 6; i++) {
            res = dmc.tick(Direction.RIGHT);
        }
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Test enemy goal or treasure but choose treasure")
    public void testTreasureOrEnemy() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_Treasure", "c_EnemyGoalsTest_Treasure");

        // Move right to get the treasure as the goal
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals("", TestUtils.getGoals(res));
    }

}
