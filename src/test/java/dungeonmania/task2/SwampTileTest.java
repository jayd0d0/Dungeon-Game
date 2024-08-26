package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTileTest {
    @Test
    @DisplayName("Player does NOT get slowed down")
    public void playerMovesNormally() {
        //2 |__  PL  __  __  ST //
        //1 |__  __  __  __  __ //
        //0 |__  __  __  __  __ //
        //    0   1   2   3   4 //
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_playerNoEffect", "c_swampTileTest_playerNoEffect");
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // move player to swamp tile
        res = dmc.tick(Direction.RIGHT);

        Position prevPosition = TestUtils.getPlayerPos(res);
        // move player out of swamp tile
        res = dmc.tick(Direction.RIGHT);
        // test that swamp tile doesn't affect player
        assertNotEquals(TestUtils.getPlayerPos(res), prevPosition);
    }

    @Test
    @DisplayName("Hostile mercenary gets slowed down")
    public void hostileMercenaryStops() {
        //2 |__  __  __  __  __  WL  WL  WL  WL  WL  WL //
        //1 |__  PL  __  __  __  __  __  SW  MC  __  WL //
        //0 |__  __  __  __  __  WL  WL  WL  WL  WL  WL //
        //    0   1   2   3   4   5   6   7   8   9  10 //
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_hostileMercSlowed", "c_swampTileTest_hostileMercSlowed");
        assertEquals(new Position(8, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), getMercPos(res));
    }

    @Test
    @DisplayName("Allied mercenary does NOT get slowed down")
    public void alliedMercenaryMovedNormally() {
        /**
         * 2 |__  WL         WL  __  WL  WL  WL  WL  WL  __  __
         * 1 |PL  (TR + ST)  WL  __  __  __  __  MC  WL  __  __
         * 0 |WL  WL         WL  __  WL  WL  WL  WL  WL  EX  __
         *     0   1          2   3   4   5   6   7   8   9  10
         * bribe_radius = 100
         * bribe_amount = 1
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_alliedMercNoEffect", "c_swampTileTest_alliedMercNoEffect");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(6, 1), getMercPos(res));

        // achieve bribe - success
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(5, 1), getMercPos(res));

        // Mercenary uses dijkstra to find the player
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(4, 1), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(3, 1), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(3, 2), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(3, 3), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(2, 3), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(1, 3), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(0, 3), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(0, 2), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(0, 1), getMercPos(res));

        // Ally follows the player
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(0, 1), getPlayerPos(res));
        assertEquals(new Position(1, 1), getMercPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(0, 2), getPlayerPos(res));
        assertEquals(new Position(0, 1), getMercPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(0, 3), getPlayerPos(res));
        assertEquals(new Position(0, 2), getMercPos(res));
    }

    @Test
    @DisplayName("Spider gets slowed down")
    public void spiderStops() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_spiderSlowed", "c_swampTileTest_spiderSlowed");
        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x + 1, y - 1));
        // move into swamp tile
        movementTrajectory.add(new Position(x + 1, y));
        // stuck in swamp for 2 ticks
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x + 1, y));
        // unstuck, continue moving
        movementTrajectory.add(new Position(x + 1, y + 1));
        movementTrajectory.add(new Position(x, y + 1));
        movementTrajectory.add(new Position(x - 1, y + 1));
        movementTrajectory.add(new Position(x - 1, y));
        movementTrajectory.add(new Position(x - 1, y - 1));

        // Assert Circular Movement of Spider
        System.out.println("Starting Pos: " + TestUtils.getEntities(res, "spider").get(0).getPosition());
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            System.out.println(i + "Pos: " + TestUtils.getEntities(res, "spider").get(0).getPosition());
            assertEquals(movementTrajectory.get(nextPositionElement),
                    TestUtils.getEntities(res, "spider").get(0).getPosition());
            nextPositionElement++;
            if (nextPositionElement == 10) {
                nextPositionElement = 0;
            }
        }
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntityPos(res, "mercenary");
    }
}
