package dungeonmania.goals;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.map.GameMap;

public class EnemyGoal implements Goal {
    private int enemyGoal;

    public EnemyGoal(int enemyGoal) {
        this.enemyGoal = enemyGoal;
    }

    @Override
    public boolean achieved(Game game) {
        GameMap map = game.getMap();
        List<ZombieToastSpawner> countZSpawners = map.getEntities(ZombieToastSpawner.class);
        return game.getCountEnemySlained() >= enemyGoal && countZSpawners.size() == 0;
    }

    @Override
    public String toString(Game game) {
        return achieved(game) ? "" : ":enemies";
    }

}
