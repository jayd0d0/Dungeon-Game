package dungeonmania.entities.buildables;

public class BuildableProperties {
    private int durability;
    private double defence;
    private int mindControlledDuration;
    private double armourAttack;
    private double armourDefence;

    public BuildableProperties(int durability, double defence, int mindControlledDuration, double armourAttack,
            double armourDefence) {
        this.durability = durability;
        this.defence = defence;
        this.mindControlledDuration = mindControlledDuration;
        this.armourAttack = armourAttack;
        this.armourDefence = armourDefence;
    }

    public int getDurability() {
        return durability;
    }

    public double getDefence() {
        return defence;
    }

    public int getMindControlledDuration() {
        return mindControlledDuration;
    }

    public double getArmourAttack() {
        return armourAttack;
    }

    public double getArmourDefence() {
        return armourDefence;
    }
}
