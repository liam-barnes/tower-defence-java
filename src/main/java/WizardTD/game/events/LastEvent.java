package WizardTD.game.events;

import processing.core.PApplet;

public class LastEvent extends Event {

    /**
     * Create event of maximum possible duration, during which noting occurs in
     * order to allow the game to continue without any interuption despite there not
     * being any future events..
     */
    public LastEvent() {
        super(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public void action(PApplet app) {
        return;
    }

}
