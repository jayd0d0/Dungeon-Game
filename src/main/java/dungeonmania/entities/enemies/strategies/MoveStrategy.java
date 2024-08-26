package dungeonmania.entities.enemies.strategies;

import dungeonmania.map.GameMap;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public interface MoveStrategy {
    Position nextPosition(Entity entity, GameMap map, Position currPosition);
}
