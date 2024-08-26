package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwampTile extends Entity {
    private int movementFactor;

    public SwampTile(Position position, int movementFactor) {
        super(position.asLayer(Entity.FLOOR_LAYER));
        this.movementFactor = movementFactor;
    }

    public int getMovementFactor() {
        return movementFactor;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        entity.setTurnsStopped(movementFactor);
    }
}
