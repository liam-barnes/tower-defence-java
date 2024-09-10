package WizardTD.game.events;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

import WizardTD.entities.monsters.MonsterType;

public class EndlessEventSchedule extends EventSchedule {
    Queue<Event> schedule;

    private double meanQuantity;
    private double meanHealth;
    private double meanSpeed;
    private double meanArmour;
    private double meanManaGain;

    private double sdQuantity;
    private double sdHealth;
    private double sdSpeed;
    private double sdArmour;
    private double sdManaGain;

    private double wormAdjustment;
    private double beetleAdjustment;

    private double statMultiplier;
    private int waveCounter;

    /**
     * Establishs some reasonable starting points for each randomly generated
     * monster type.
     */
    public EndlessEventSchedule() {
        super();
        this.schedule = super.getSchedule();

        this.meanQuantity = 2;
        this.meanHealth = 100;
        this.meanSpeed = 1;
        this.meanArmour = 4;
        this.meanManaGain = 20;

        this.sdQuantity = 0.7;
        this.sdHealth = 20;
        this.sdSpeed = 0.2;
        this.sdArmour = 2;
        this.sdManaGain = 5;

        this.wormAdjustment = 2;
        this.beetleAdjustment = 5;

        this.statMultiplier = 1.3;

        this.waveCounter = 0;

        generateWave();
        generateWave();
        generateWave();
    }

    @Override
    public void finaliseSchedule() {
        return;
    }

    /**
     * @param val any double.
     * @return a whole number similar to the inputted value.
     */
    private int whole(double val) {
        return (int) val;
    }

    /**
     * Increases the base values from which the random monster characteristics are
     * generated.
     */
    private void updateMultipliers() {
        meanQuantity = 3 * statMultiplier;
        meanHealth = 100 * statMultiplier;
        meanSpeed = 1 * statMultiplier;
        meanArmour = 4 * statMultiplier;
        meanManaGain = 20 * statMultiplier;

        sdQuantity = 0.5 * statMultiplier;
        sdHealth = 20 * statMultiplier;
        sdSpeed = 0.2 * statMultiplier;
        sdArmour = 2 * statMultiplier;
        sdManaGain = 5 * statMultiplier;

        if (waveCounter % 5 == 0) {
            statMultiplier += 0.1;
        }
    }

    /**
     * Creates a wave with monsters which have randomly generated characteristics
     * and appends this wave to the end of the current schedule.
     */
    public void generateWave() {
        ArrayList<MonsterType> monsters = new ArrayList<>();
        Random random = new Random();

        int quantityGremlin = whole(random.nextGaussian() * sdQuantity + meanQuantity);
        float speedGremlin = (float) (random.nextGaussian() * sdSpeed + meanSpeed);
        float maxHealthGremlin = (float) (random.nextGaussian() * sdHealth + meanHealth);
        float armourGremlin = (float) (random.nextGaussian() * sdArmour + meanArmour);
        float manaGainGremlin = whole(random.nextGaussian() * sdManaGain + meanManaGain);

        MonsterType gremlin = new MonsterType("gremlin", quantityGremlin, maxHealthGremlin, speedGremlin, armourGremlin,
                manaGainGremlin);

        int quantityWorm = whole(random.nextGaussian() * sdQuantity + meanQuantity - wormAdjustment);
        float speedWorm = (float) (random.nextGaussian() * sdSpeed + meanSpeed);
        float maxHealthWorm = (float) (random.nextGaussian() * sdHealth + meanHealth);
        float armourWorm = (float) (random.nextGaussian() * sdArmour + meanArmour);
        float manaGainWorm = whole(random.nextGaussian() * sdManaGain + meanManaGain);

        MonsterType worm = new MonsterType("worm", quantityWorm, maxHealthWorm, speedWorm, armourWorm, manaGainWorm);

        int quantityBeetle = whole(random.nextGaussian() * sdQuantity + meanQuantity - beetleAdjustment);
        float speedBeetle = (float) (random.nextGaussian() * sdSpeed + meanSpeed);
        float maxHealthBeetle = (float) (random.nextGaussian() * sdHealth + meanHealth);
        float armourBeetle = (float) (random.nextGaussian() * sdArmour + meanArmour);
        float manaGainBeetle = whole(random.nextGaussian() * sdManaGain + meanManaGain);

        MonsterType beetle = new MonsterType("beetle", quantityBeetle, maxHealthBeetle, speedBeetle, armourBeetle,
                manaGainBeetle);

        monsters.add(gremlin);
        monsters.add(worm);
        monsters.add(beetle);

        WaveEvent wave = new WaveEvent(1, 3, monsters);
        super.scheduleEvent(wave);
    }

    @Override
    public Event getNextEvent() {
        if (schedule.size() > 0) {
            waveCounter++;
            generateWave();
            updateMultipliers();
            super.updateNumberOfEvents(waveCounter + 3);
            return super.getSchedule().remove();
        } else {
            generateWave();
            generateWave();
            return super.getSchedule().remove();
        }

    }

}
