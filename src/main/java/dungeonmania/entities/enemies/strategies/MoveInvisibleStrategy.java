package dungeonmania.entities.enemies.strategies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class MoveInvisibleStrategy implements MoveStrategy {
    @Override
    public Position nextPosition(Entity entity, GameMap map, Position currPosition) {
        Position nextPos;
        Random randGen = new Random();
        List<Position> pos = entity.getCardinalAdjPositions();
        pos = pos.stream().filter(p -> map.canMoveTo(entity, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = entity.getPosition();
            map.moveTo(entity, nextPos);
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
            map.moveTo(entity, nextPos);
        }
        return nextPos;
    }
}
