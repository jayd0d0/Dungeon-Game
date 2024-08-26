package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class Sceptre extends Buildable {
    private int mindControlledDuration;

    public Sceptre(int mindControlledDuration) {
        super(null, 0);
        this.mindControlledDuration = mindControlledDuration;
    }

    @Override
    public void use(Game game) {
        return;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return origin;
    }

    public int getMindControlledDuration() {
        return mindControlledDuration;
    }
}
