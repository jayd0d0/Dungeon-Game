package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Switch;

public class BouldersGoal implements Goal {
    @Override
    public boolean achieved(Game game) {
        return game.getMapEntities(Switch.class).stream().allMatch(s -> s.isActivated());
    }

    @Override
    public String toString(Game game) {
        return achieved(game) ? "" : ":boulders";
    }
}
