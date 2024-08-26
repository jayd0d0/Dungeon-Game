package dungeonmania.entities.enemies.strategies;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MoveInvincibleStrategy implements MoveStrategy {
    @Override
    public Position nextPosition(Entity entity, GameMap map, Position currPosition) {
        Position nextPos;

        Position plrDiff = Position.calculatePositionBetween(map.getPlayerPosition(), entity.getPosition());

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(entity.getPosition(), Direction.RIGHT)
                : Position.translateBy(entity.getPosition(), Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(entity.getPosition(), Direction.UP)
                : Position.translateBy(entity.getPosition(), Direction.DOWN);
        Position offset = entity.getPosition();
        if (plrDiff.getY() == 0 && map.canMoveTo(entity, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && map.canMoveTo(entity, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            offset = largestAcceptableDisplacement(entity, map, moveX, moveY);
        } else {
            offset = largestAcceptableDisplacement(entity, map, moveY, moveX);
        }
        nextPos = offset;

        return nextPos;
    }

    public Position largestAcceptableDisplacement(Entity entity, GameMap map, Position pos1, Position pos2) {
        Position offset;
        if (map.canMoveTo(entity, pos1))
            offset = pos1;
        else if (map.canMoveTo(entity, pos2))
            offset = pos2;
        else
            offset = entity.getPosition();
        return offset;
    }
}
