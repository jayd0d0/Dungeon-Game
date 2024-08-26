package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class SunStoneTest {
    @Test
    @DisplayName("test if sunStone is in inventory after picking it up")
    public void testInventorySunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_pickup", "c_basicSunStoneTest");

        // move player to sunstone
        res = dmc.tick(Direction.RIGHT);

        // check if sun stone is in invetory
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("test if sunStone is kept when opening a door")
    public void testSunStoneOpenDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_openDoor", "c_basicSunStoneTest");

        // move player to sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "sun_stone").size());

        // walk through door and check SunStone is still there
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // passed the door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("test if picking up sunStone counts towards treasure goal")
    public void testSunStoneTreasureGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_treasureGoal", "c_basicSunStoneTest");

        assertTrue(TestUtils.getGoals(res).contains("treasure"));
        // move player to sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // check if it counts towards treasure goal
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("test SunStone cannot be used to bribe a mercenary")
    public void testSunStoneBribeMercenary() {
        //2 | W   W   W   W   __  //
        //1 | W   MC  PL  ST  __  //
        //0 | W   W   W   W   __  //
        //    0   1   2   3   4   //

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_bribeMercenary", "c_basicSunStoneTest");

        // move player to sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // try to bribe merc and see if sunstone is still in inventory
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test shield can be created from SunStone, No Treasure")
    public void createShieldNoTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_BuildShieldWithNoTreasure",
                "c_sunStoneTest_BuildShieldWithNoTreasure");
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test shield can be created from SunStone, No Key")
    public void createShieldNoKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_BuildShieldWithNoKey", "c_sunStoneTest_BuildShieldWithNoKey");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }
}
