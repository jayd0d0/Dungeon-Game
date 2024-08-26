package dungeonmania.entities.enemies.strategies;

import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class MoveAlliedStrategy implements MoveStrategy {
    @Override
    public Position nextPosition(Entity entity, GameMap map, Position currPosition) {
        Position nextPos = currPosition;
        Player player = map.getPlayer();
        if (entity instanceof Mercenary) {
            Mercenary mercenary = (Mercenary) entity;
            boolean isAdjacentToPlayer = mercenary.getIsAdjacentToPlayer();
            nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                    : map.dijkstraPathFind(entity.getPosition(), player.getPosition(), mercenary);
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos))
                mercenary.setIsAdjacentToPlayer(true);
        }
        return nextPos;
    }
}
