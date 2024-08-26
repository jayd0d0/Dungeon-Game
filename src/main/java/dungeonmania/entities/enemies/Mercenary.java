package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.enemies.strategies.MoveAlliedStrategy;
import dungeonmania.entities.enemies.strategies.MoveHostileStrategy;
import dungeonmania.entities.enemies.strategies.MoveInvincibleStrategy;
import dungeonmania.entities.enemies.strategies.MoveInvisibleStrategy;
import dungeonmania.entities.enemies.strategies.MoveStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;
    private boolean isAdjacentToPlayer = false;
    private int turnsControlled = 0;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return checkBribeRadius(player, this) && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    public boolean checkBribeRadius(Player player, Entity entity) {
        Position playerPos = player.getPosition();
        Position entityPos = entity.getPosition();
        int horizontalDiff = Math.abs(playerPos.getX() - entityPos.getX());
        int verticalDiff = Math.abs(playerPos.getY() - entityPos.getY());
        if (horizontalDiff > this.bribeRadius || verticalDiff > this.bribeRadius) {
            return false;
        }
        return true;
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }

    }

    @Override
    public void interact(Player player, Game game) {
        if (player.hasSceptre() && !allied) {
            Sceptre sceptre = player.getInventory().getFirst(Sceptre.class);
            setTurnsControlled(sceptre.getMindControlledDuration());
            this.allied = true;
            return;
        }
        this.allied = true;
        bribe(player);
        if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
            isAdjacentToPlayer = true;
    }

    @Override
    public void move(Game game) {
        if (turnsControlled == 0 && game.sceptreInInv()) {
            this.allied = false;
        }
        if (!allied || (allied && !isAdjacentToPlayer)) {
            if (isStuckInSwamp()) {
                return;
            }
        }
        Position nextPos;
        GameMap map = game.getMap();
        if (allied) {
            if (turnsControlled > 0) {
                turnsControlled--;
            }
            MoveStrategy strategy = new MoveAlliedStrategy();
            nextPos = strategy.nextPosition(this, map, getPosition());
        } else if (map.getEffectivePlayerPotion() instanceof InvisibilityPotion) {
            MoveStrategy strategy = new MoveInvisibleStrategy();
            nextPos = strategy.nextPosition(this, map, getPosition());
        } else if (map.getEffectivePlayerPotion() instanceof InvincibilityPotion) {
            MoveStrategy strategy = new MoveInvincibleStrategy();
            nextPos = strategy.nextPosition(this, map, getPosition());
        } else {
            // Follow hostile
            MoveStrategy strategy = new MoveHostileStrategy();
            nextPos = strategy.nextPosition(this, map, getPosition());
        }
        map.moveTo(this, nextPos);
    }

    @Override
    public boolean isInteractable(Player player) {
        return (!allied) && (canBeBribed(player) || player.hasSceptre());
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!allied)
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }

    public boolean getIsAdjacentToPlayer() {
        return isAdjacentToPlayer;
    }

    public void setIsAdjacentToPlayer(boolean isAdjacentToPlayer) {
        this.isAdjacentToPlayer = isAdjacentToPlayer;
    }

    public void setTurnsControlled(int turnsControlled) {
        allied = true;
        this.turnsControlled = turnsControlled;
    }
}
