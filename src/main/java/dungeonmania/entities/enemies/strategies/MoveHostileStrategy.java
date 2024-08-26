package dungeonmania.entities.enemies.strategies;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class MoveHostileStrategy implements MoveStrategy {
    @Override
    public Position nextPosition(Entity entity, GameMap map, Position currPosition) {
        Position nextPos;
        Player player = map.getPlayer();
        nextPos = map.dijkstraPathFind(entity.getPosition(), player.getPosition(), entity);
        return nextPos;
    }
}
