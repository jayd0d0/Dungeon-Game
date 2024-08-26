package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class MidnightArmourTest {
    @Test
    @DisplayName("testing creating midnight armor using a sunstone with no zombies around")
    public void testBuildMArmour() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_noZombie", "c_midnightArmourTest_build");

        // Move left to pick up items and check inventory
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, TestUtils.getEntities(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // With items, build midnight armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        // check if midnight armour in inventory
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
    }

    @Test
    @DisplayName("test creating midnight armour with zombies")
    public void testBuildMArmourWithZombie() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombie", "c_midnightArmourTest_build");

        // Move left to pick up items and check inventory
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, TestUtils.getEntities(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // attempt to create the midnight armour
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));

        // check if midnight armour in inventory
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
    }
}
