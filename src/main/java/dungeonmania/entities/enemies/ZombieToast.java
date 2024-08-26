package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.enemies.strategies.MoveInvincibleStrategy;
import dungeonmania.entities.enemies.strategies.MoveInvisibleStrategy;
import dungeonmania.entities.enemies.strategies.MoveStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }

    @Override
    public void move(Game game) {
        if (isStuckInSwamp()) {
            return;
        }
        Position nextPos;
        GameMap map = game.getMap();
        if (map.getEffectivePlayerPotion() instanceof InvincibilityPotion) {
            MoveStrategy strategy = new MoveInvincibleStrategy();
            nextPos = strategy.nextPosition(this, map, getPosition());
        } else {
            MoveStrategy strategy = new MoveInvisibleStrategy();
            nextPos = strategy.nextPosition(this, map, getPosition());
        }
        game.moveEntity(this, nextPos);

    }

}
