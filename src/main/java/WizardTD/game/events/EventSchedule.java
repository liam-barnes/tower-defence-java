package WizardTD.game.events;

import java.util.LinkedList;
import java.util.Queue;

public class EventSchedule {
    private Queue<Event> schedule = new LinkedList<>();

    private int numberOfEvents;

    /**
     * Initalises the event schedule.
     */
    public EventSchedule() {
        this.numberOfEvents = 0;
    }

    /**
     * Adds final event of functionally infinite duration to event schedule to allow
     * the game to continue more easily even if there are no further events.
     */
    public void finaliseSchedule() {
        schedule.add(new LastEvent());
        numberOfEvents = schedule.size();
    }

    public void updateNumberOfEvents(int count) {
        numberOfEvents = count;
    }

    /**
     * Adds a specified event to occur after all existing events to the event
     * schedule.
     * 
     * @param event the event to be added to the schedule.
     */
    public void scheduleEvent(Event event) {
        schedule.add(event);
        numberOfEvents = schedule.size();
    }

    /**
     * @return An integer representing the number of events that will take place
     *         throughout the course of the game.
     */
    public int getNumberOfEvents() {
        return numberOfEvents - 1;
    }

    /**
     * @return The next event in the schedule.
     */
    public Event getNextEvent() {
        if (schedule.size() > 0) {
            return schedule.remove();
        } else {
            return null;
        }

    }

    /**
     * Retrive the current event.
     * 
     * @return The event taking place or if no event is currently taking place (ie
     *         pre-event pause), the event that is about to begin.
     */
    public Event getEvent() {
        if (schedule.size() > 0) {
            return schedule.peek();
        } else {
            return null;
        }
    }

    /**
     * Finds the time between the end of the previous event and the beginning of the
     * current/next event.
     * 
     * @return the duration of the pause between the last two events.
     */
    public int getNextPauseDuration() {
        if (schedule.size() > 0) {
            return schedule.peek().getPauseDuration();
        } else {
            return 0;
        }
    }

    public int getPauseDuration() {
        if (schedule.size() != 0) {
            return schedule.peek().getPauseDuration();
        } else {
            return 0;
        }
    }

    /**
     * @return The duration of the current event or if there is no current event the
     *         event next to begin.
     */
    public int getEventDuration() {
        if (schedule.size() != 0) {
            return schedule.peek().getDuration();
        } else {
            return 0;
        }

    }

    /**
     * Exposes the queue of events that represents the event schedule.
     * 
     * @return The queue of events that is the core of the event schedule.
     */
    public Queue<Event> getSchedule() {
        return schedule;
    }

}
