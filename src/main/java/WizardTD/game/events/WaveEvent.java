package WizardTD.game.events;

import processing.core.PApplet;
import WizardTD.entities.monsters.Monster;
import WizardTD.entities.monsters.MonsterType;
import WizardTD.entities.monsters.movement.Path;

import static WizardTD.utils.Dimensions.FPS;

import java.util.ArrayList;

public class WaveEvent extends Event {
    private int time;
    private int interval;

    ArrayList<MonsterType> monsterTypes = new ArrayList<>();

    /**
     * Establishs the timing and contents of the monster wave.
     * 
     * @param pause        the duration of the pause before the event begins.
     * @param duration     the length of the event.
     * @param monsterTypes an ArrayList containing all of the types of monsters to
     *                     be spawned during the wave.
     */
    public WaveEvent(int pause, int duration, ArrayList<MonsterType> monsterTypes) {
        super(pause * FPS, duration * FPS);

        this.monsterTypes = monsterTypes;
        int quantity = 0;
        for (MonsterType mt : monsterTypes) {
            quantity += mt.getQuantity();
        }

        if (quantity == 0) {
            quantity = 1;
        }

        this.interval = Math.floorDiv(super.getDuration(), quantity);
    }

    /**
     * Retrive all of the monster types that still need to be spawned during the
     * remaining portion of the event.
     * 
     * @return An ArrayList of MonsterType containing monsters stil to be spawned.
     */
    public ArrayList<MonsterType> getMonsterTypes() {
        return monsterTypes;
    }

    /**
     * Updates the internal event time.
     */
    private void tick() {
        time++;
    }

    private int i = 0;

    public void action(PApplet app) {
        tick();

        if (time % interval == 0) {

            if (monsterTypes.get(i).getQuantity() < 1) {
                if (monsterTypes.size() - 1 == i) {
                    return;
                }
                i++;
            }

            Path tmpPath = new Path(super.getGame().getBoard(), super.getGame().getBoard().getRandomPath());
            Monster monster = new Monster(monsterTypes.get(i), tmpPath);

            super.getGame().addMonster(monster);

            monsterTypes.get(i).decrementQuantity();
        }

    }

}
