package WizardTD.game.events;

import WizardTD.game.Game;
import processing.core.PApplet;

public abstract class Event {
    private static Game game;

    private int duration;
    private int pause;

    /**
     * Initialises the event, establish the key attributes of the event.
     * 
     * @param pause    the duration of the pause prior to the event beginning.
     * @param duration the length of the event itself.
     */
    public Event(int pause, int duration) {
        this.pause = pause;
        this.duration = duration;
    }

    /**
     * @param currentGame The game within which all future events ought to take
     *                    place.
     */
    public static void setGame(Game currentGame) {
        game = currentGame;
    }

    /**
     * @return The game within which all events take place.
     */
    public static Game getGame() {
        return game;
    }

    /**
     * @return An integer representing the length of the pause begore an event
     *         begins.
     */
    public int getPauseDuration() {
        return pause;
    }

    /**
     * @return An integer representing the length of the event after the pre-event
     *         pause.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * The behaviour of the event.
     * 
     * @param app the PApplet representing the application within which the game is
     *            running.
     */
    public abstract void action(PApplet app);

}
