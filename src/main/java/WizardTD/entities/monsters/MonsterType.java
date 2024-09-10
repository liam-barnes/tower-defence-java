package WizardTD.entities.monsters;

public class MonsterType {
    private String monsterRace;

    private float maxHealth;
    private float speed;
    private float armour;
    private float manaGainOnKill;
    private int quantity;

    public MonsterType(String monsterRace, int quantity, float maxHealth, float speed, float armour,
            float manaGainOnKill) {
        this.monsterRace = monsterRace;
        this.maxHealth = maxHealth;
        this.speed = speed;
        this.armour = armour;
        this.manaGainOnKill = manaGainOnKill;
        this.quantity = quantity;
    }

    public MonsterType(MonsterType monsterType) {
        this.monsterRace = monsterType.getMonsterRace();
        this.maxHealth = monsterType.getMaxHealth();
        this.speed = monsterType.getSpeed();
        this.armour = monsterType.getArmour();
        this.manaGainOnKill = monsterType.getManaGainOnKill();
        this.quantity = monsterType.getQuantity();
    }

    public void decrementQuantity() {
        quantity = quantity - 1;
    }

    public String getMonsterRace() {
        return monsterRace;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getSpeed() {
        return speed;
    }

    public float getArmour() {
        return armour;
    }

    public float getManaGainOnKill() {
        return manaGainOnKill;
    }

}
