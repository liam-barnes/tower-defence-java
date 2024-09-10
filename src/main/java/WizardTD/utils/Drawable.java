package WizardTD.utils;

import processing.core.PApplet;

public interface Drawable {
    /**
     * Draw objects to the window.
     * 
     * @param app the PApplet representing the game window.
     */
    public void draw(PApplet app);

    /**
     * Modify the logic and position of drawable objects to simulate movement and
     * motion.
     */
    public void updateLogic();
}
