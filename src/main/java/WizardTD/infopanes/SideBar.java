package WizardTD.infopanes;

import processing.core.PApplet;

import static WizardTD.utils.Dimensions.*;

public class SideBar {
    Button[] buttons;
    Player player;

    /**
     * Creates all of the required buttons with the appropriate descriptors.
     * 
     * @param player the current player of the game
     */
    public SideBar(Player player) {
        buttons = new Button[] {
                new Button(player, "FASTFORWARD", "FF", "2x Speed"),
                new Button(player, "PAUSE", "P", "PAUSE"),
                new Button(player, "BUILD TOWER", "T", "Build Tower"),
                new Button(player, "UPGRADE RANGE", "U1", "Upgrade Range"),
                new Button(player, "UPGRADE SPEED", "U2", "Upgrade Speed"),
                new Button(player, "UPGRADE DAMAGE", "U3", "Upgrade Damage"),
                new Button(player, "MANA POOL SPELL", "M", "Mana Pool Spell"),
        };

    }

    /**
     * Retrive the buttons array
     * 
     * @return All of the created buttons.
     */
    public Button[] getButtons() {
        return buttons;
    }

    public void updateLogic() {

    }

    /**
     * Draw objects to the window.
     * 
     * @param app the PApplet representing the game window.
     */
    public void draw(PApplet app) {
        // Create sidebar
        app.noStroke();
        app.fill(132, 115, 74); // app.fill(132, 115, 74);
        app.rect(WIDTH - SIDEBAR, TOPBAR, SIDEBAR, HEIGHT);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setIndex(i);
            buttons[i].draw(app);
        }
    }

}
