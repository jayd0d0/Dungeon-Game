package dungeonmania.entities.buildables;

public class BuildableFactory {
    public static Buildable createBuildable(String buildItem, BuildableProperties properties) {
        switch (buildItem) {
        case "Bow":
            return new Bow(properties.getDurability());
        case "Shield":
            return new Shield(properties.getDurability(), properties.getDefence());
        case "Sceptre":
            return new Sceptre(properties.getMindControlledDuration());
        case "MidnightArmour":
            return new MidnightArmour(properties.getArmourAttack(), properties.getArmourDefence());
        default:
            throw new IllegalArgumentException("Not buildable!!");
        }
    }
}
