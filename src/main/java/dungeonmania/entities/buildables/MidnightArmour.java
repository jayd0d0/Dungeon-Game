package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class MidnightArmour extends Buildable {
    private double armourAttack;
    private double armourDefence;

    public MidnightArmour(double armourAttack, double armourDefence) {
        super(null, 0);
        this.armourAttack = armourAttack;
        this.armourDefence = armourDefence;
    }

    @Override
    public void use(Game game) {
        return;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, armourAttack, armourDefence, 1, 1));
    }

    public double getArmourAttack() {
        return armourAttack;
    }

    public double getArmourDefence() {
        return armourDefence;
    }
}
