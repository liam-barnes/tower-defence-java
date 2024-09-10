package WizardTD.infopanes;

import WizardTD.board.towers.Tower;
import WizardTD.utils.Drawable;
import processing.core.PApplet;

public class ToolTip implements Drawable {
    private int xPos;
    private int yPos;

    private boolean show;
    private float cost;

    /**
     * Determine which tool tip, if any, ought to be shown and determine the
     * contents of the text
     * 
     * @param player an object Player representing the player playing the current
     *               game.
     * @param button the button over which the player's mouse is hovering.
     */
    public ToolTip(Player player, Button button) {
        this.xPos = Button.getXPosition();
        this.yPos = button.getYPosition();

        if (button.getName() == "BUILD TOWER") {
            this.cost = Tower.getBuildCost();
            this.show = true;
        } else if (button.getName() == "MANA POOL SPELL") {
            this.cost = player.getManaPoolCost();
            this.show = true;
        } else {
            this.show = false;
        }

    }

    /**
     * @return A boolean indicating whether the tool tip is currently being drawn.
     */
    public boolean isShowing() {
        return show;
    }

    public void updateLogic() {
        // nothing required
    }

    /**
     * Draw objects to the window.
     * 
     * @param app the PApplet representing the game window.
     */
    public void draw(PApplet app) {
        if (!show) {
            return;
        }
        int widthAdjustment = 0;

        if (cost > 999) {
            widthAdjustment = 5;
        }

        app.fill(255);
        app.strokeWeight(1);
        app.rect(xPos - 65 - widthAdjustment, yPos, 55 + (widthAdjustment * (float) 1.25), 15);

        app.fill(0);
        app.textSize(11);
        app.text("Cost: " + (int) cost, xPos - 65 + 2 - widthAdjustment, yPos + 12);
    }
}
